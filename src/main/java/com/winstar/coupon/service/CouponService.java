package com.winstar.coupon.service;

import com.winstar.coupon.entity.MyCoupon;
import org.springframework.stereotype.Service;
;

import java.util.Date;
import java.util.List;

/**
 * 名称： CouponService
 * 作者： sky
 * 日期： 2017-12-12 10:44
 * 描述： 优惠券service
 **/
@Service
public interface CouponService {

    /**
     *  发送优惠券
     * @param accountId 用户ID
     * @param activityId 活动ID
     * @param goodsId 商品Id
     * @return
     */
    MyCoupon sendCoupon(String accountId ,String activityId,String goodsId);

    /**
     * 检查过期
     */
    void checkExpired(String accountId);

    /**
     * 查询我的优惠券
     * @param accountId
     * @return
     */
    List<MyCoupon> findMyCoupon(String accountId);

    /**
     * 使用优惠券
     * @param id
     * @return
     */
    MyCoupon useCoupon(String id);

    /**
     * 查询我可用的优惠券
     * @param accountId
     * @return
     */
    List<MyCoupon> findMyUsableCoupon(String accountId);

    /**
     * 查询优惠券详情
     * @param id
     * @return
     */
    MyCoupon findMyCouponById(String id);
}
