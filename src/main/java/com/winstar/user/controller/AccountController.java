package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import com.winstar.user.utils.UUIDUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * @author laohu
 **/
@RestController
@RequestMapping("/api/v1/cbc/account")
public class AccountController {

    /**
     * 获取token
     *
     * @param accountParam accountParam
     * @return AccessToken AccessToken
     * @throws NotRuleException NotRuleException
     */
    @PostMapping(value = "/getToken", produces = "application/json")
    public AccessToken getToken(@RequestBody AccountParam accountParam) throws NotRuleException {
        checkGetTokenRule(accountParam);
        Account account = ServiceManager.accountRepository.findByOpenid(accountParam.getOpenid());
        if (null == account) {
            Account accountSaved = ServiceManager.accountService.createAccount(accountParam);
            return createAccessToken(accountSaved);
        }
        AccessToken accessToken = ServiceManager.accessTokenRepository.findByAccountId(account.getId());
        if (null != accessToken) {
            return updateAccessToken(accessToken);
        } else {
            return createAccessToken(account);
        }
    }

    /**
     * 验证token
     *
     * @param tokenId tokenId
     * @return AccessToken AccessToken
     * @throws NotRuleException NotRuleException
     */
    @PostMapping(value = "/authToken", produces = "application/json")
    public ResponseEntity authToken(String tokenId) throws NotRuleException {
        if (StringUtils.isEmpty(tokenId))
            throw new NotRuleException("tokenId");

        AccessToken accessToken = ServiceManager.accessTokenRepository.findByTokenId(tokenId);
        if (null == accessToken) {
            return new ResponseEntity<>(new SimpleResult("未授权"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }




    /**
     * 更新token信息
     *
     * @param accessToken accessToken
     * @return AccessToken AccessToken
     */
    private AccessToken updateAccessToken(AccessToken accessToken) {
        accessToken.setTokenId(UUIDUtils.getUUID());
        accessToken.setUpdateTime(new Date());
        return ServiceManager.accessTokenRepository.save(accessToken);
    }

    /**
     * 获取token信息
     *
     * @param account account
     * @return AccessToken AccessToken
     */
    private AccessToken createAccessToken(Account account) {
        AccessToken accessToken;
        accessToken = new AccessToken();
        accessToken.setCreateTime(new Date());
        accessToken.setTokenId(UUIDUtils.getUUID());
        accessToken.setAccountId(account.getId());
        accessToken.setUpdateTime(new Date());
        return ServiceManager.accessTokenRepository.save(accessToken);
    }

    private void checkGetTokenRule(@RequestBody AccountParam accountParam) throws NotRuleException {
        if (null == accountParam)
            throw new NotRuleException("accountParam");
        else if (StringUtils.isEmpty(accountParam.getOpenid())) {
            throw new NotRuleException("openid");
        }
        if (StringUtils.isEmpty(accountParam.getNickName())) {
            throw new NotRuleException("nickName");
        }
        if (StringUtils.isEmpty(accountParam.getHeadImgUrl())) {
            throw new NotRuleException("headImgUrl");
        }
    }
}
