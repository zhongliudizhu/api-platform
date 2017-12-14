package com.winstar.user.repository;

import com.winstar.user.entity.OneMoneyCouponRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 名称： OneMoneyCouponRecordRepository
 * 作者： sky
 * 日期： 2017-12-14 17:15
 * 描述：
 **/
public interface OneMoneyCouponRecordRepository  extends JpaRepository<OneMoneyCouponRecord, String>  {

    List<OneMoneyCouponRecord> findByAccountId(String accountId);

}
