package com.winstar.communalCoupon.repository;

import com.winstar.communalCoupon.entity.AccountCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zl on 2019/5/24
 */
public interface AccountCouponRepository extends JpaRepository<AccountCoupon, String> {

    Page<AccountCoupon> findByAccountIdAndShowStatusAndState(String accountId, String showStatus, String state, Pageable pageable);

    List<AccountCoupon> findByAccountIdAndShowStatusAndState(String accountId, String showStatus, String state);

    List<AccountCoupon> findByAccountIdAndCouponIdIn(String accountId, String[] couponIds);

    List<AccountCoupon> findByAccountId(String accountId);
    List<AccountCoupon> findByAccountIdAndActivityId(String accountId, String activityId);
    //统计用户未使用的优惠券张数
    @Query(value = "SELECT COUNT(*) FROM `communal_account_coupon` WHERE account_id=?1 AND state='normal'",nativeQuery = true)
    long findByState(String accountId);

    AccountCoupon findByCouponId(String couponId);

}
