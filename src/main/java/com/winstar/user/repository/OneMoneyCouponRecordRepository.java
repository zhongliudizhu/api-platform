package com.winstar.user.repository;

import com.winstar.user.entity.OneMoneyCouponRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 名称： OneMoneyCouponRecordRepository
 * 作者： sky
 * 日期： 2017-12-14 17:15
 * 描述：
 **/
public interface OneMoneyCouponRecordRepository extends JpaRepository<OneMoneyCouponRecord, String> {
    @Modifying
    @Query(value = "update OneMoneyCouponRecord  set status=1, updateTime = sysdate() where accountId=?1")
    void updateStatus(String accountId);
    Integer countByAccountId(String accountId);

    @Query(value="select count(1) ct from cbc_user_one_money_coupon_record t where t.account_id = ?1 and t.status = ?2 and t.create_time like ?3")
    Integer countByAccountIdAndStatusAndCreateTimeLike(String accountId, Integer status, String createTime);
}
