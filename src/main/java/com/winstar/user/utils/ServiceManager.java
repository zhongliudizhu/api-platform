package com.winstar.user.utils;

import com.winstar.carLifeMall.entity.ItemSellerRelation;
import com.winstar.carLifeMall.entity.Orders;
import com.winstar.carLifeMall.repository.*;
import com.winstar.carLifeMall.service.CategoryService;
import com.winstar.coupon.service.CouponService;
import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.user.repository.AccessTokenRepository;
import com.winstar.user.repository.AccountRepository;
import com.winstar.user.repository.OneMoneyCouponRecordRepository;
import com.winstar.user.repository.PageViewLogRepository;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.OneMoneyCouponRecordService;
import com.winstar.user.service.PageViewLogService;
import com.winstar.user.service.SmsService;
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

    public static SmsService smsService;

    public static ItemRepository itemRepository;
    public static SellerRepository sellerRepository;
    public static ItemSellerRelationRepository itemSellerRelationRepository;

    public static CategoryRepository categoryRepository;
    public static CategoryService categoryService;
    public static OrdersRepository ordersRepository;
    public static OrdersItemsRepository ordersItemsRepository;
    public static CouponService couponService;

    @Autowired
    public void setCouponService(CouponService couponService) {
        ServiceManager.couponService = couponService;
    }

    @Autowired
    public void setOrdersItemsRepository(OrdersItemsRepository ordersItemsRepository) {
        ServiceManager.ordersItemsRepository = ordersItemsRepository;
    }

    @Autowired
    public void setOrdersRepository(OrdersRepository ordersRepository) {
        ServiceManager.ordersRepository = ordersRepository;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        ServiceManager.categoryService = categoryService;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        ServiceManager.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setItemSellerRelationRepository(ItemSellerRelationRepository itemSellerRelationRepository) {
        ServiceManager.itemSellerRelationRepository = itemSellerRelationRepository;
    }

    @Autowired
    public void setSellerRepository(SellerRepository sellerRepository) {
        ServiceManager.sellerRepository = sellerRepository;
    }

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        ServiceManager.itemRepository = itemRepository;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        ServiceManager.smsService = smsService;
    }

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
    public void setOilOrderRepository(OilOrderRepository oilOrderRepository) {
        ServiceManager.oilOrderRepository = oilOrderRepository;
    }

    @Autowired
    public void setFlowOrderRepository(FlowOrderRepository flowOrderRepository) {
        ServiceManager.flowOrderRepository = flowOrderRepository;
    }
}
