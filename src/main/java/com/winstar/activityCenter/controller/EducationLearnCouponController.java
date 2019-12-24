package com.winstar.activityCenter.controller;

import com.winstar.activityCenter.service.CommunalActivityService;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.FansService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    RedisTools redisTools;

    AccountCouponService accountCouponService;

    CommunalActivityService communalActivityService;

    AccountService accountService;

    FansService fansService;

    AccountCouponRepository accountCouponRepository;

    @RequestMapping(value = "education/getCoupon", method = RequestMethod.POST)
    public Result getCoupon(@RequestBody Map map){
        String openId = MapUtils.getString(map, "openId");
        String verifyCode = MapUtils.getString(map, "verifyCode");
        log.info("openId is {} and verifyCode is {}", openId, verifyCode);
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
        redisTools.remove(verifyCode);
        String templateIds = (String) redisTools.get("education_coupon_templateId");
        log.info("templateIds is " + templateIds);
        if (StringUtils.isEmpty(templateIds)) {
            log.info("templateId为空！");
            return Result.fail("templateId_is_null", "templateId为空！");
        }
        String[] ids = templateIds.split(",");
        Map fans = fansService.getFansInfo(openId, false);
        if (ObjectUtils.isEmpty(fans)) {
            log.info("openId无效！");
            return Result.fail("openId_is_invalid", "openId无效！");
        }
        if ("0".equals(fans.get("subscribe").toString())) {
            log.info("用户未关注！");
            return Result.fail("user_not_subscribe", "用户未关注！");
        }
        Account account = accountService.getAccountOrCreateByOpenId(openId, null, null);

        List<AccountCoupon> coupons = new ArrayList<>();
        for (String templateId : ids) {
            List<AccountCoupon> accountCouponList = accountCouponRepository.findByTemplateIdAndAccountIdOrderByCreatedAtDesc(templateId, account.getId());
            if (!ObjectUtils.isEmpty(accountCouponList)) {
                AccountCoupon accountCoupon = accountCouponList.get(0);
                //当天已领取优惠券
                if (communalActivityService.getDayEnd(accountCoupon.getCreatedAt()).getTime() > System.currentTimeMillis()) {
                    //未使用则返回优惠券
                    if (AccountCouponService.NORMAL.equals(accountCoupon.getState())) {
                        log.info("当天已领取该安全文明奖励金 {}", templateId);
                        accountCoupon.setState("again");
                        coupons.add(accountCoupon);
                    } else {
                        log.info("当天领取该安全文明奖励金已使用{}", templateId);
                    }
                }
            } else {
                //未领取则发放优惠券
                SendCouponDomain domain = new SendCouponDomain(templateId, account.getId(), AccountCoupon.TYPE_YJX, "1", null, null);
                log.info("发放优惠券 。。 templateId is{}", templateId);
                List<AccountCoupon> accountCoupons = accountCouponService.sendCoupon(domain, null);
                if (ObjectUtils.isEmpty(accountCoupons)) {
                    log.info("发券失败！");
                    return Result.fail("send_coupon_fail", "发券失败！");
                } else {
                    coupons.add(accountCoupons.get(0));
                }
            }
        }
        return Result.success(coupons);
    }

    @RequestMapping(value = "education/getAmount", method = RequestMethod.GET)
    public Result getAmount() throws NotRuleException {
        String amount = (String) redisTools.get("education_coupon_amount");
        if (!ObjectUtils.isEmpty(amount)) {
            return Result.success(amount);
        }
        throw new NotRuleException("系统异常");
    }


}
