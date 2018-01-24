package com.winstar.user.utils;

import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.user.repository.AccessTokenRepository;
import com.winstar.user.repository.AccountRepository;
import com.winstar.user.repository.OneMoneyCouponRecordRepository;
import com.winstar.user.repository.PageViewLogRepository;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.OneMoneyCouponRecordService;
import com.winstar.user.service.PageViewLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ServiceManager {

    public static AccessTokenRepository accessTokenRepository;
    public static AccountRepository accountRepository;
    public static PageViewLogRepository pageViewLogRepository;
    public static PageViewLogService pageViewLogService;
    public static AccountService accountService;

    public static OneMoneyCouponRecordService oneMoneyCouponRecordService;

    public static OneMoneyCouponRecordRepository oneMoneyCouponRecordRepository;
    public static OilOrderRepository oilOrderRepository;
    public static FlowOrderRepository flowOrderRepository;

    @Autowired
    public void setAccessTokenRepository(AccessTokenRepository accessTokenRepository) {
        ServiceManager.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        ServiceManager.accountRepository = accountRepository;
    }

    @Autowired
    public void setPageViewLogRepository(PageViewLogRepository pageViewLogRepository) {
        ServiceManager.pageViewLogRepository = pageViewLogRepository;
    }

    @Autowired
    public void setPageViewLogService(PageViewLogService pageViewLogService) {
        ServiceManager.pageViewLogService = pageViewLogService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        ServiceManager.accountService = accountService;
    }

    @Autowired
    public void setOneMoneyCouponRecordService(OneMoneyCouponRecordService oneMoneyCouponRecordService) {
        ServiceManager.oneMoneyCouponRecordService = oneMoneyCouponRecordService;
    }

    @Autowired
    public void setOneMoneyCouponRecordRepository(OneMoneyCouponRecordRepository oneMoneyCouponRecordRepository) {
        ServiceManager.oneMoneyCouponRecordRepository = oneMoneyCouponRecordRepository;
    }
    @Autowired
    public void setOilOrderRepository(OilOrderRepository oilOrderRepository){
        ServiceManager.oilOrderRepository = oilOrderRepository;
    }
    @Autowired
    public void setFlowOrderRepository(FlowOrderRepository flowOrderRepository){
        ServiceManager.flowOrderRepository = flowOrderRepository;
    }
}
