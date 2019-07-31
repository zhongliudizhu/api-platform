package com.winstar.user.controller;

import com.winstar.exception.*;
import com.winstar.order.entity.OilOrder;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import com.winstar.user.utils.UUIDUtils;
import com.winstar.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author laohu
 **/
@RestController
@RequestMapping("/api/v1/cbc/account")
public class AccountController {

    @GetMapping(value = "userHasBoughtOil")
    public Result userHasBoughtOil(@RequestParam String openId) {
        Account account = ServiceManager.accountRepository.findByOpenid(openId);
        if (ObjectUtils.isEmpty(account)) {
            return Result.fail("user_not_here", "查找不到该用户");
        }
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndStatus(account.getId(), 3);
        if (!ObjectUtils.isEmpty(oilOrders)) {
            return Result.success(true);
        }
        return Result.fail("user_never_bought", "该用户未买过");
    }


    /**
     * 获取token
     *
     * @param accountParam accountParam
     * @return AccessToken AccessToken
     * @throws NotRuleException NotRuleException
     */
    @PostMapping(value = "/getToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccessToken getToken(@RequestBody AccountParam accountParam) throws NotRuleException {
        checkGetTokenRule(accountParam);
        Account account = ServiceManager.accountRepository.findByOpenid(accountParam.getOpenid());
        if (null == account) {
            Account accountSaved = ServiceManager.accountService.createAccount(accountParam);
            return ServiceManager.accountService.createAccessToken(accountSaved);
        }
        AccessToken accessToken = ServiceManager.accessTokenService.findByAccountId(account.getId());
        if (null == accessToken) {
            return ServiceManager.accountService.createAccessToken(account);
        }

        return accessToken;
    }

    /**
     * 验证token
     *
     * @param tokenId tokenId
     * @return AccessToken AccessToken
     * @throws NotRuleException NotRuleException
     */
    @PostMapping(value = "/authToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity authToken(String tokenId) throws NotRuleException {
        if (StringUtils.isEmpty(tokenId))
            throw new NotRuleException("tokenId");

        AccessToken accessToken = ServiceManager.accessTokenService.findByTokenId(tokenId);
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
