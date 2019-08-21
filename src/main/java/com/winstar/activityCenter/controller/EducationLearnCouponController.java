package com.winstar.activityCenter.controller;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2019/8/19
 * 违法教育学校获优惠券
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/noAuth/")
@Slf4j
public class EducationLearnCouponController {

    @Autowired
    RedisTools redisTools;

    @Autowired
    AccountCouponService accountCouponService;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "education/getCoupon", method = RequestMethod.POST)
    public Result getCoupon(@RequestBody Map map){
        String openId = MapUtils.getString(map, "openId");
        String verifyCode = MapUtils.getString(map, "verifyCode");
        if(StringUtils.isEmpty(openId)){
            log.info("openId参数为空！");
            return Result.fail("missing_parameter_openId", "openId参数为空！");
        }
        if(StringUtils.isEmpty(verifyCode)){
            log.info("verifyCode参数为空！");
            return Result.fail("missing_parameter_verifyCode", "verifyCode参数为空！");
        }
        if(!redisTools.exists(verifyCode)){
            log.info("verifyCode无效！");
            return Result.fail("verifyCode_is_invalid", "verifyCode无效！");
        }
        String templateId = (String) redisTools.get("education_coupon_templateId");
        log.info("templateId is " + templateId);
        Account account = accountService.getAccountOrCreateByOpenId(openId, null, null);
        SendCouponDomain domain = new SendCouponDomain(templateId, account.getId(), AccountCoupon.TYPE_YJX, "1", null, null);
        List<AccountCoupon> accountCoupons = accountCouponService.sendCoupon(domain, null);
        if(ObjectUtils.isEmpty(accountCoupons)){
            log.info("发券失败！");
            return Result.fail("send_coupon_fail", "发券失败！");
        }
        return Result.success(accountCoupons.get(0));
    }

}
