package com.winstar.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.mobile.domain.ReceiveDomain;
import com.winstar.mobile.entity.ReceiveQualification;
import com.winstar.mobile.repository.ReceiveQualificationRepository;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.FansService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/noAuth/")
@AllArgsConstructor
public class ReceiveCouponController {

    private RedisTools redisTools;
    private AccountCouponService accountCouponService;
    private ReceiveQualificationRepository qualificationRepository;
    private AccountService accountService;
    private FansService fansService;

    @PostMapping("mobile/receiveCoupons")
    public Result receiveCoupons(@RequestBody @Valid ReceiveDomain receiveDomain) {
        log.info("入参信息为 {}", JSON.toJSONString(receiveDomain));
        Map fans = fansService.getFansInfo(receiveDomain.getOpenId(), false);
        if (ObjectUtils.isEmpty(fans)) {
            log.info("openId无效！");
            return Result.fail("openId_is_invalid", "openId无效！");
        }
        if ("0".equals(fans.get("subscribe").toString())) {
            log.info("用户未关注！");
            return Result.fail("user_not_subscribe", "用户未关注！");
        }
        Account account = accountService.getAccountOrCreateByOpenId(receiveDomain.getOpenId(), null, null);
        String validateKey = receiveDomain.getPhone();
        Object data = redisTools.get("122_" + validateKey);
        log.info("redis中的 data is {}", data);
        if (ObjectUtils.isEmpty(data) || !("YES").equalsIgnoreCase(data.toString())) {
            log.info("验证资格失败");
            return Result.fail("validate_failed", "验证资格失败");
        }
        String[] templates = receiveDomain.getTemplateId().split(",");
        for (String s : templates) {
            SendCouponDomain domain = new SendCouponDomain(s, account.getId(), "mobile_122", "1", null, null);
            log.info("发放优惠券 。。 templateId is{}", s);
            List<AccountCoupon> couponList = accountCouponService.sendCoupon(domain, null);
            if (CollectionUtils.isEmpty(couponList)) {
                return Result.fail("give_coupon_failed", "发券失败");
            }
        }
        ReceiveQualification qualification = new ReceiveQualification();
        qualification.setPhone(receiveDomain.getPhone());
        qualification.setReceiveTime(new Date());
        qualification.setPlatType("mobile");
        qualificationRepository.save(qualification);
        redisTools.remove(validateKey);
        return Result.success(true);
    }


}
