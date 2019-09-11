package com.winstar.user.utils;

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
    public static AccessTokenService accessTokenService;
    public static RedisTools redisTools;

    @Autowired
    public void setRedisTools(RedisTools redisTools) {
        ServiceManager.redisTools = redisTools;
    }

    @Autowired
    public void setAccessTokenService(AccessTokenService accessTokenService) {
        ServiceManager.accessTokenService = accessTokenService;
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
