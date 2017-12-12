package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.param.AccountParam;
import com.winstar.user.repository.AccessTokenRepository;
import com.winstar.user.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author laohu
 **/
@RestController
@RequestMapping("/api/v1/cbc/accountInfo")
public class AccountInfoController {
    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    AccountRepository accountInfoRepository;

    /**
     * 获取token
     *
     * @param accountParam accountParam
     * @return AccessToken
     * @throws NotRuleException NotRuleException
     */
    @PostMapping("/getToken")
    public AccessToken getToken(@RequestBody AccountParam accountParam) throws NotRuleException {
        checkGetTokenRule(accountParam);
        return null;
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
