package com.winstar.user.utils;

import com.winstar.user.repository.AccessTokenRepository;
import com.winstar.user.repository.AccountRepository;
import com.winstar.user.repository.PageViewLogRepository;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.PageViewLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceManager {

    public static AccessTokenRepository accessTokenRepository;
    public static AccountRepository accountRepository;
    public static PageViewLogRepository pageViewLogRepository;
    public static PageViewLogService pageViewLogService;
    public static AccountService accountService;

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
}
