package com.winstar.activityCenter.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.CbcWhiteList;
import com.winstar.activityCenter.repository.CbcWhiteListRepository;
import com.winstar.activityCenter.vo.CbcWhiteListVo;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by qyc on 2019/08/14.
 * 建行活动白名单领取优惠券
 */
@RestController
@RequestMapping("/api/v1/cbc/white")
@Slf4j
@AllArgsConstructor
public class CbcWhiteListController {

    CbcWhiteListRepository cbcWhiteListRepository;

    RedisTools redisTools;

    AccountCouponService accountCouponService;

    AccountCouponRepository accountCouponRepository;

    AccountService accountService;

    /**
     * 根据电话号码验证 发券
     */
    @RequestMapping(value = "/receiveCoupons", method = RequestMethod.POST)
    public Result giveCoupons(HttpServletRequest request, @RequestBody @Valid CbcWhiteListVo cbcWhiteListVo) throws NotRuleException {
        log.info("建行白名单用户领取优惠券：" + JSON.toJSONString(cbcWhiteListVo));
        String accountId = accountService.getAccountId(request);
        if(StringUtils.isEmpty(cbcWhiteListVo.getMobile()) && StringUtils.isEmpty(cbcWhiteListVo.getCardNumber())){
            log.info("手机号和身份证号不能同时为空！");
            return Result.fail("missing_param_mobile_or_cardNumber", "手机号和身份证号不能同时为空！");
        }
        if(cbcWhiteListVo.getActivityCode().contains("-1-") && StringUtils.isEmpty(cbcWhiteListVo.getMobile())){
            log.info("手机号不能为空！");
            return Result.fail("missing_param_mobile", "手机号不能为空！");
        }
        if(cbcWhiteListVo.getActivityCode().contains("-1-")){
            String code = (String) redisTools.get(cbcWhiteListVo.getMobile() + "randCode");
            if (ObjectUtils.isEmpty(code) || !code.equals(cbcWhiteListVo.getRandCode())) {
                return Result.fail("code_error", "短信验证码错误");
            }
        }
        List<CbcWhiteList> list = null;
        if(!StringUtils.isEmpty(cbcWhiteListVo.getCardNumber()) && !StringUtils.isEmpty(cbcWhiteListVo.getMobile())){
            list = cbcWhiteListRepository.findByActivityCodeAndPhoneAndCardNumberIgnoreCase(cbcWhiteListVo.getActivityCode(), cbcWhiteListVo.getMobile(), cbcWhiteListVo.getCardNumber());
        }else if(!StringUtils.isEmpty(cbcWhiteListVo.getCardNumber())){
            list = cbcWhiteListRepository.findByActivityCodeAndCardNumberIgnoreCase(cbcWhiteListVo.getActivityCode(), cbcWhiteListVo.getCardNumber());
        }else if(!StringUtils.isEmpty(cbcWhiteListVo.getMobile())){
            list = cbcWhiteListRepository.findByPhoneAndActivityCode(cbcWhiteListVo.getMobile(), cbcWhiteListVo.getActivityCode());
        }
        if (ObjectUtils.isEmpty(list)) {
            log.info("非白名单用户，不能领取");
            return Result.fail("user_not_here", "用户无法领取");
        }
        List<CbcWhiteList> unReceivedList = list.stream().filter(e -> "0".equals(e.getState())).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(unReceivedList)) {
            return Result.fail("user_received", "用户已领取");
        }
        List<CbcWhiteList> unExpiredList = unReceivedList.stream().filter(e -> e.getValidTime().after(new Date())).collect(Collectors.toList());
        if(ObjectUtils.isEmpty(unExpiredList)){
            return Result.fail("seniority_expired", "领取时间已过");
        }
        List<AccountCoupon> recList = new ArrayList<>();
        for (CbcWhiteList whiteList : unExpiredList) {
            SendCouponDomain domain = new SendCouponDomain(whiteList.getCouponId(), accountId, AccountCoupon.TYPE_CCB, "1", cbcWhiteListVo.getActivityCode(), cbcWhiteListVo.getMobile());
            List<AccountCoupon> accountCoupons = accountCouponService.sendCoupon(domain, null);
            if (!ObjectUtils.isEmpty(accountCoupons)) {
                recList.addAll(accountCoupons);
                whiteList.setState("1");
                whiteList.setReceiveCouponId(accountCoupons.get(0).getCouponId());
                cbcWhiteListRepository.save(whiteList);
            }
        }
        return !ObjectUtils.isEmpty(recList) ? Result.success(recList) : Result.fail("send_error", "发券失败");
    }

}
