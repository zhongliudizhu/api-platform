package com.winstar.communalCoupon.repository;

import com.winstar.communalCoupon.entity.CouponSendRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zl on 2019/7/8
 */
public interface CouponSendRecordRepository extends JpaRepository<CouponSendRecord, String> {

    CouponSendRecord findByCouponId(String couponId);

    CouponSendRecord findCouponSendRecordById(String id);

}
