package com.winstar.user.service;

import com.winstar.user.entity.OneMoneyCouponRecord;
import com.winstar.user.repository.OneMoneyCouponRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 名称： OneMoneyCouponRecordService
 * 作者： sky
 * 日期： 2017-12-14 17:17
 * 描述：
 **/
@Service
public class OneMoneyCouponRecordService {

    @Autowired
    OneMoneyCouponRecordRepository oneMoneyCouponRecordRepository;

    /**
     * 查询用户是否有购买一元券的权限
     * @param accountId 用户id
     * @return true 有 false 无
     */
    public boolean checkBuyAuth(String accountId){
        List<OneMoneyCouponRecord> list=oneMoneyCouponRecordRepository.findByAccountId(accountId);
        if(list.size()>0) return  false;
        return true;
    }

    /**
     * 用户购买1元油券
     * @param accountId
     * @return
     */
    public OneMoneyCouponRecord changeStatus(String accountId){

        return null;
    }

}
