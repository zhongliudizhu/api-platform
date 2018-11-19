package com.winstar.coupon.repository;

import com.winstar.coupon.entity.MyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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

//    List<MyCoupon> findByAccountIdAndStatusAndUseRuleLessThanEqual(String accountId,Integer status,Double rules);

    List<MyCoupon> findByAccountIdAndActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Object accountId, String activityId, Date startMonth, Date endMonth);

    List<MyCoupon> findByAccountIdAndActivityIdAndStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Object accountId, String activityId,Integer status, Date startMonth, Date endMonth);

    List<MyCoupon> findByAccountIdAndActivityId(Object accountId, String activityId);

//    List<MyCoupon> findByActivityId(String activityId);

//    List<MyCoupon> findByActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(String activityId, Date startMonth, Date endMonth);

    @Query("select f from MyCoupon f where f.accountId=?1 " +
            "and f.status = ?2 " +
            "and f.useRule <= ?3 " +
            "and f.validEndAt >= ?4 " +
            "and f.activityId not in ?5")
    List<MyCoupon> findMyUsableCoupon(String accountId,Integer status,Double rules,Date nowTime,List<String > activity);

    @Query("from MyCoupon t where t.accountId=?1 and t.activityId=?2 and  t.status<>2")
    List<MyCoupon> findByAccountIdAndActivityId(String accountId,String activity);
    @Query("from MyCoupon t where t.accountId=?1 and t.activityId=?2 and t.validEndAt >=?3 and  t.status<>2")
    List<MyCoupon> findByAccountIdAndActivityId2(String accountId,String activity,Date currentTime);

    @Query("select f from MyCoupon f where f.accountId=?1 " +
            "and f.status = ?2 " +
            "and f.useRule <= ?3 " +
            "and f.validEndAt >= ?4 " +
            "and f.activityId=?5")
    List<MyCoupon> findFassionMyUsableCoupon(String accountId, int i, Double money, Date date, String type);
}
