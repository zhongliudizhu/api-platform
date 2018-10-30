package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.CouponActivity;
import com.winstar.couponActivity.entity.CouponActivityType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * CouponActivityRepository
 *
 * @author: Big BB
 * @create 2018-09-16 10:02
 * @DESCRIPTION:
 **/
public interface CouponActivityTypeRepository extends PagingAndSortingRepository<CouponActivityType,String> {
    /**
     * 根据活动id和状态查询优惠券
     * @param activityId
     * @param state
     * @return
     */
    List<CouponActivityType> findByActivityIdAndShowStatus(String activityId,Integer state);
}
