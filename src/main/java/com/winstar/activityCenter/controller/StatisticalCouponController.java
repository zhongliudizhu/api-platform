package com.winstar.activityCenter.controller;

import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/cbc/couponStatistics")
public class StatisticalCouponController {
    @Autowired
    AccountService accountService;
    @Autowired
    MyOilCouponRepository myOilCouponRepository;
    @Autowired
    AccountCouponService accountCouponService;

    @RequestMapping(value = "/statistical", method = RequestMethod.POST)
    public Map statisticalCoupon(HttpServletRequest request) throws NotRuleException {
        Map<Object, Object> map = new HashMap<>();
        String accountId = accountService.getAccountId(request);

        //统计该用户未使用的油券
        long oilNum = myOilCouponRepository.findByUseState(accountId);
        //统计该用户未使用的优惠券
        long couponNum = accountCouponService.getAccountCouponFromRedisHash(accountId).stream().filter(e -> AccountCouponService.NORMAL.equals(e.getState())).count();
        map.put("oilNum", oilNum);
        map.put("couponNum", couponNum);
        return map;
    }
}
