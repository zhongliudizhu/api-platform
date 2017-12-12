package com.winstar.coupon.service;

import com.winstar.coupon.entity.MyCoupon;

/**
 * 名称： CouponService
 * 作者： sky
 * 日期： 2017-12-12 10:44
 * 描述： 优惠券service
 **/
public interface CouponService {

    /**
     *  发送优惠券
     * @param accountId 用户ID
     * @param couponTemplateId 优惠券模板ID
     * @return
     */
    MyCoupon sendCoupon(String accountId ,String couponTemplateId);


}
