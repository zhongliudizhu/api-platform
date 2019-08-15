package com.winstar.activityCenter.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.InstallmentWhitelist;
import com.winstar.activityCenter.repository.InstallmentRepository;
import com.winstar.activityCenter.service.SmsService;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//建行分期200元优惠券活动
@RestController
@RequestMapping("/api/v1/cbc/installment")
@Slf4j
@AllArgsConstructor
public class InstallmentController {
    InstallmentRepository installmentRepository;
    RedisTools redisTools;
    AccountCouponService accountCouponService;
    AccountCouponRepository accountCouponRepository;
    AccountService accountService;

    /**
     * 根据电话号码验证 发券
     */
    @RequestMapping(value = "/installmentCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result giveCoupons(HttpServletRequest request, @RequestParam String mobile, @RequestParam String randCode) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        String code = (String) redisTools.get(mobile + "randCode");
        if (ObjectUtils.isEmpty(code) || !code.equals(randCode)) {

            return Result.fail("code_error", "短信验证码错误");
        }
        List<InstallmentWhitelist> list = installmentRepository.findByPhoneAndValidTimeAfter(mobile, new Date());
        if (ObjectUtils.isEmpty(list)) {
            return Result.fail("user_not_here", "用户无法领取");
        }
        List<InstallmentWhitelist> unReceivedList = list.stream().filter(e -> "0".equals(e.getState())).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(unReceivedList)) {
            return Result.fail("user_received", "用户已领取");
        }
        List<AccountCoupon> recList = sendCoupon(unReceivedList.get(0).getCouponId(), accountId, mobile, unReceivedList.size() + "");
        if (!ObjectUtils.isEmpty(recList)) {
            list.forEach(e -> e.setState("1"));
            installmentRepository.save(list);
            return Result.success(recList);
        }
        return Result.fail("send_error", "发券失败");
    }

    /**
     * 建行交安卡白名单发放优惠券
     */
    private List<AccountCoupon> sendCoupon(String templateId, String accountId, String phone, String num) {
        log.info("给抢券成功的用户发放优惠券：accountId is {} and templateId is {}", accountId, templateId);
        ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, num);
        Map map = responseEntity.getBody();
        if (MapUtils.getString(map, "code").equals("SUCCESS")) {
            log.info("建行交安卡白名单发放优惠券获取优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "ccb", accountId, null, null, phone);
            accountCouponRepository.save(accountCoupons);
            log.info("建行交安卡白名单发放优惠券发放优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            return accountCoupons;
        }
        return null;
    }

    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE;

    static {
        REGEX_MOBILE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
    }

    /**
     * 校验手机号
     */
    private static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    SmsService smsService;

    @GetMapping(value = "/message")
    public Result sendMessage(@RequestParam String mobile) {
        log.info("sendMessage()mobile is {} ", mobile);
        if (!isMobile(mobile)) {
            return Result.fail("verify_code_is_error", "请输入正确手机号码");
        }
        String randCode = getRandCode();
        log.info("randCode is {}", randCode);
        if (!redisTools.set(mobile + "randCode", randCode, 300L)) {
            return Result.fail("verify_code_is_error", "发送短信失败！");
        }
        Boolean flag = smsService.sendSms(mobile, "尊敬的用户，您的验证码是：" + randCode + "。 有效期为5分钟。请勿向任何单位或个人泄露。");
        return flag ? Result.success(flag) : Result.fail("verify_code_is_error", "发送短信失败 ");
    }

    private static String getRandCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }


}
