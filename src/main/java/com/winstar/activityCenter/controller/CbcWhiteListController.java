package com.winstar.activityCenter.controller;

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
        String accountId = accountService.getAccountId(request);
        String code = (String) redisTools.get(cbcWhiteListVo.getMobile() + "randCode");
        if (ObjectUtils.isEmpty(code) || !code.equals(cbcWhiteListVo.getRandCode())) {
            return Result.fail("code_error", "短信验证码错误");
        }
        List<CbcWhiteList> list = cbcWhiteListRepository.findByPhoneAndActivityCode(cbcWhiteListVo.getMobile(), cbcWhiteListVo.getActivityCode());
        if (ObjectUtils.isEmpty(list)) {
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
                cbcWhiteListRepository.save(whiteList);
            }
        }
        return !ObjectUtils.isEmpty(recList) ? Result.success(recList) : Result.fail("send_error", "发券失败");
    }

}
