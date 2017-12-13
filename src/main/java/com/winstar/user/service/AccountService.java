package com.winstar.user.service;

import com.winstar.exception.NotFoundException;
import com.winstar.user.entity.Account;
import com.winstar.user.utils.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class AccountService {


    /**
     * 根据id 获取账号信息
     *
     * @param accountId accountId
     * @return Account
     * @throws NotFoundException NotFoundException
     */
    Account findById(String accountId) throws NotFoundException {
        Account account = ServiceManager.accountRepository.findOne(accountId);
        if (null == account)
            throw new NotFoundException("account");
        return account;
    }
}
