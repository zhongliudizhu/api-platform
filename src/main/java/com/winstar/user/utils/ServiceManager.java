package com.winstar.user.utils;

import com.winstar.carLifeMall.repository.*;
import com.winstar.carLifeMall.service.CarLifeOrdersService;
import com.winstar.carLifeMall.service.CategoryService;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.redis.RedisTools;
import com.winstar.user.repository.AccessTokenRepository;
import com.winstar.user.repository.AccountRepository;
import com.winstar.user.service.AccessTokenService;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceManager {

    public static String REDIS_KEY_FIND_ACCOUNT_BY_ID = "com.winstar.user.service.AccountServicefindOne";
    public static String REDIS_KEY_FIND_ACCOUNT_BY_OPENID = "com.winstar.user.service.findAccountIdByOpenid";

    public static AccessTokenRepository accessTokenRepository;
    public static AccountRepository accountRepository;
    public static AccountService accountService;
    public static OilOrderRepository oilOrderRepository;
    public static SmsService smsService;
    public static ItemRepository itemRepository;
    public static SellerRepository sellerRepository;
    public static ItemSellerRelationRepository itemSellerRelationRepository;
    public static CategoryRepository categoryRepository;
    public static CategoryService categoryService;
    public static CarLifeOrdersRepository carLifeOrdersRepository;
    public static OrdersItemsRepository ordersItemsRepository;
    public static CarLifeOrdersService carLifeOrdersService;
    public static AccessTokenService accessTokenService;
    public static RedisTools redisTools;

    @Autowired
    public void setRedisTools(RedisTools redisTools) {
        ServiceManager.redisTools = redisTools;
    }

    @Autowired
    public void setCarLifeOrdersRepository(CarLifeOrdersRepository carLifeOrdersRepository) {
        ServiceManager.carLifeOrdersRepository = carLifeOrdersRepository;
    }

    @Autowired
    public void setAccessTokenService(AccessTokenService accessTokenService) {
        ServiceManager.accessTokenService = accessTokenService;
    }

    @Autowired
    public void setCarLifeOrdersService(CarLifeOrdersService carLifeOrdersService) {
        ServiceManager.carLifeOrdersService = carLifeOrdersService;
    }

    @Autowired
    public void setOrdersItemsRepository(OrdersItemsRepository ordersItemsRepository) {
        ServiceManager.ordersItemsRepository = ordersItemsRepository;
    }

    @Autowired
    public void setOrdersRepository(CarLifeOrdersRepository carLifeOrdersRepository) {
        ServiceManager.carLifeOrdersRepository = carLifeOrdersRepository;
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
    public void setAccountService(AccountService accountService) {
        ServiceManager.accountService = accountService;
    }

    @Autowired
    public void setOilOrderRepository(OilOrderRepository oilOrderRepository) {
        ServiceManager.oilOrderRepository = oilOrderRepository;
    }

}
