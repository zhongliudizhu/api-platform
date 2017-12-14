package com.winstar.coupon.service;

import com.winstar.coupon.entity.MyCoupon;

import java.util.List;

;

/**
 * 名称： CouponService
 * 作者： sky
 * 日期： 2017-12-12 10:44
 * 描述： 优惠券service
 **/

public interface CouponService {

    /**
     * 发送优惠券
     *
     * @param accountId  用户ID
     * @param activityId 活动ID
     * @param goodsId    商品Id
     * @return MyCoupon
     */
    MyCoupon sendCoupon(String accountId, String activityId, String goodsId);

    /**
     * 检查过期
     * @param accountId 用户Id
     */
    void checkExpired(String accountId);

    /**
     * 查询我的优惠券
     *
     * @param accountId 用户ID
     * @return  MyCoupon
     */
    List<MyCoupon> findMyCoupon(String accountId);

    /**
     * 使用优惠券
     *
     * @param id
     * @return MyCoupon
     */
    MyCoupon useCoupon(String id);

    /**
     * 撤销已用的优惠券
     *
     * @param id 优惠券Id
     * @return
     */
    MyCoupon cancelMyCoupon(String id);

    /**
     * 查询当前金额可用的优惠券
     *
     * @param accountId 用户ID
     * @param money 金额
     * @return MyCoupon
     */
    List<MyCoupon> findMyUsableCoupon(String accountId, Double money);

    /**
     * 查询优惠券是否可用
     * @param goodsId 商品ID
     * @param couponId 优惠券ID
     * @return true 可用，false不可用
     */
    boolean findMyCouponById(String goodsId,String couponId);



}
