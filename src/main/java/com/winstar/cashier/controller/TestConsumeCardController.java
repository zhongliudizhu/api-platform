package com.winstar.cashier.controller;

import com.winstar.cashier.wx.service.WxMartetTemplate;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2019/7/19
 */
@RestController
@RequestMapping("/api/v1/cbc/test/consumeCard")
public class TestConsumeCardController {

    @Autowired
    AccountCouponService accountCouponService;

    @Autowired
    WxMartetTemplate wxMartetTemplate;

    @Autowired
    AccountService accountService;

    @RequestMapping("")
    public void consumeCard(String accountId, String couponIds){
        accountCouponService.consumeWxCard(accountId, couponIds, wxMartetTemplate, accountService);
    }

}
