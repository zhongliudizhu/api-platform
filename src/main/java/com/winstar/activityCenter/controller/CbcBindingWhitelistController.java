package com.winstar.activityCenter.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.CbcBinDingWhitelist;
import com.winstar.activityCenter.repository.CbcBinDingWhitelistRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//绑定交安卡活动10-50元优惠券
@RestController
@RequestMapping("/api/v1/cbc/binding")
@Slf4j
@AllArgsConstructor
public class CbcBindingWhitelistController {

    CbcBinDingWhitelistRepository cbcBinDingWhitelistRepository;

    RedisTools redisTools;

    AccountCouponService accountCouponService;

    AccountCouponRepository accountCouponRepository;

    AccountService accountService;

    /**
     * 根据电话号码验证 发券
     */
    @RequestMapping(value = "/binDingCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result giveCoupons(HttpServletRequest request, @RequestParam String mobile, @RequestParam String randCode) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        String code = (String) redisTools.get(mobile + "randCode");
        if (ObjectUtils.isEmpty(code) || !code.equals(randCode)) {
            return Result.fail("code_error", "短信验证码错误");
        }
        List<CbcBinDingWhitelist> list = cbcBinDingWhitelistRepository.findByPhoneAndValidTimeAfter(mobile, new Date());

        if (ObjectUtils.isEmpty(list)) {
            return Result.fail("user_not_here", "用户无法领取");
        }
        List<CbcBinDingWhitelist> unReceivedList = list.stream().filter(e -> "0".equals(e.getState())).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(unReceivedList)) {
            return Result.fail("user_received", "用户已领取");
        }
        List<AccountCoupon> recList = new ArrayList<>();
        for (CbcBinDingWhitelist whitelist : unReceivedList) {
            List<AccountCoupon> accountCoupons = sendCoupon(whitelist.getCouponId(), accountId, mobile);
            if (!ObjectUtils.isEmpty(accountCoupons)) {
                recList.addAll(accountCoupons);
                whitelist.setState("1");
                cbcBinDingWhitelistRepository.save(whitelist);
            }
        }
        return !ObjectUtils.isEmpty(recList) ? Result.success(recList) : Result.fail("send_error", "发券失败");
    }

    /**
     * 建行交安卡白名单发放优惠券
     */
    private List<AccountCoupon> sendCoupon(String templateId, String accountId, String phone) {
        log.info("给抢券成功的用户发放优惠券：accountId is {} and templateId is {}", accountId, templateId);
        ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, "1");
        Map map = responseEntity.getBody();
        if (MapUtils.getString(map, "code").equals("SUCCESS")) {
            log.info("绑定交安卡活动获取优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "ccb", accountId, null, null, phone);
            accountCouponRepository.save(accountCoupons);
            log.info("绑定交安卡活动发放优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            return accountCoupons;
        }
        return null;
    }
}
