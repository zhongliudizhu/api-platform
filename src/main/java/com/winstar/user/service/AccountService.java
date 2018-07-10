package com.winstar.user.service;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class AccountService {
    /**
     * 根据openid获取用户accountId 如果没有用户，注册用户
     *
     * @param openid openid
     * @return accountId
     */
    public String findAccountIdByOpenid(String openid) {
        Account account = ServiceManager.accountRepository.findByOpenid(openid);
        if (null == account) {
            account = ServiceManager.accountService.createAccount(new AccountParam(openid, "", ""));
            return account.getId();
        }
        return account.getId();
    }

    /**
     * 创建用户信息
     *
     * @param accountParam accountParam
     * @return Account Account
     */
    public Account createAccount(@RequestBody AccountParam accountParam) {
        Account account;
        account = new Account();
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());
        account.setHeadImgUrl(accountParam.getHeadImgUrl());
        account.setOpenid(accountParam.getOpenid());
        account.setNickName(accountParam.getNickName());
        return ServiceManager.accountRepository.save(account);
    }


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


    public SimpleResult checkBindMobile(HttpServletRequest request) throws NotRuleException {
        String accountId = ServiceManager.accountService.getAccountId(request);

        Account account = ServiceManager.accountRepository.findOne(accountId);
        if (StringUtils.isEmpty(account.getMobile())) return new SimpleResult("YES");
        return new SimpleResult("NO");
    }


    public void checkBindDriverLicense(){

    }
}
