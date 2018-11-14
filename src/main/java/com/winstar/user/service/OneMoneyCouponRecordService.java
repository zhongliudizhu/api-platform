package com.winstar.user.service;

import com.winstar.user.entity.OneMoneyCouponRecord;
import com.winstar.user.repository.OneMoneyCouponRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 名称： OneMoneyCouponRecordService
 * 作者： sky
 * 日期： 2017-12-14 17:17
 * 描述：
 **/
@Service
public class OneMoneyCouponRecordService {

    static final Integer STATUS_UNUSED = 0;

    @Autowired
    OneMoneyCouponRecordRepository oneMoneyCouponRecordRepository;

    /**
     * 查询用户是否有购买一元券的权限
     *
     * @param accountId 用户id
     * @return true 有 false 无
     */
    public boolean checkBuyAuth(String accountId) {
        String date = new SimpleDateFormat("yyyy-MM").format(new Date());
        int count = oneMoneyCouponRecordRepository.countByAccountIdAndStatusAndCreateTimeLike(accountId, STATUS_UNUSED, date + "%");
        return count > 0;
    }

    /**
     * 用户购买1元油券
     *
     * @param accountId
     * @return
     */
    @Transactional
    public void changeStatus(String accountId) {
        oneMoneyCouponRecordRepository.updateStatus(accountId);
    }

    /**
     * 处理违法后生成可购买1元油券的权限
     *
     * @param accountId 用户Id
     * @param orderId   违法id
     * @return
     */
    public OneMoneyCouponRecord insertRecord(String accountId, String orderId) {
        OneMoneyCouponRecord record = new OneMoneyCouponRecord();
        record.setStatus(0);
        record.setAccountId(accountId);
        record.setOrderId(orderId);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        OneMoneyCouponRecord oneMoneyCouponRecord = oneMoneyCouponRecordRepository.save(record);
        return oneMoneyCouponRecord;
    }

}
