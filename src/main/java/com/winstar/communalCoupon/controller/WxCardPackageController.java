package com.winstar.communalCoupon.controller;

import com.winstar.cashier.wx.entity.base.BaseSignature;
import com.winstar.cashier.wx.service.WxMartetTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2019/7/19
 */
@RestController
@RequestMapping("/api/v1/cbc/coupon")
@AllArgsConstructor
public class WxCardPackageController {

    @Autowired
    WxMartetTemplate wxMartetTemplate;

    /**
     * 微信卡包签名
     */
    @GetMapping("queryCardSign")
    public BaseSignature queryCardSign(String cardId) throws Exception {
        return wxMartetTemplate.getCardSign(cardId);
    }

}
