package com.winstar.coupon.repository;

import com.winstar.coupon.entity.MyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * 名称： MyCouponRepository
 * 作者： sky
 * 日期： 2017-12-12 10:40
 * 描述：
 **/
public interface MyCouponRepository extends JpaRepository<MyCoupon,String> ,JpaSpecificationExecutor<MyCoupon>{

    List<MyCoupon> findByAccountId(String accountId);

    List<MyCoupon> findByAccountIdAndStatusAndUseRuleLessThanEqual(String accountId,Integer status,Double rules);

    List<MyCoupon> findByAccountIdAndActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Object accountId, String activityId, Date startMonth, Date endMonth);

    List<MyCoupon> findByAccountIdAndActivityId(Object accountId, String activityId);

    List<MyCoupon> findByActivityId(String activityId);


}
