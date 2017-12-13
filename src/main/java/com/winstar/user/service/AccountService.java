package com.winstar.user.service;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.Account;
import com.winstar.user.utils.ServiceManager;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccountService {


    /**
     * 根据id 获取账号信息
     *
     * @param accountId accountId
     * @return Account
     * @throws NotFoundException NotFoundException
     */
    public Account findById(String accountId) throws NotFoundException {
        Account account = ServiceManager.accountRepository.findOne(accountId);
        if (null == account)
            throw new NotFoundException("account");
        return account;
    }

    /**
     * 从head中获取accountId
     *
     * @param request request
     * @return String eg: "accountId"
     * @throws NotRuleException NotRuleException
     */
    public String getAccountId(HttpServletRequest request) throws NotRuleException {
        Object obj = request.getAttribute("accountId");
        if (null == obj) throw new NotRuleException("accountId");
        return obj.toString();
    }
}
