package com.winstar.user.service;

import com.alibaba.fastjson.JSON;
import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import com.winstar.user.vo.WinstarAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${info.getTokenInfoUrl}")
    String getTokenInfoUrl;
    @Value("${info.checkBindLicense}")
    String checkBindLicenseUrl;
    @Value("${info.authDriverLicense}")
    String authDriverLicenseUrl;


    @Cacheable(value = "findAccountByAccountIdCbc", keyGenerator = "tkKeyGenerator")
    public Account findOne(String accountId) {
        return ServiceManager.accountRepository.findOne(accountId);
    }

    /**
     * 根据openid获取用户accountId 如果没有用户，注册用户
     *
     * @param openid openid
     * @return accountId
     */
    @Cacheable(value = "findAccountByAccountIdAndOpenidCbc", keyGenerator = "tkKeyGenerator")
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

    /**
     * 从head中获取openId
     */
    public String getOpenId(HttpServletRequest request) throws NotRuleException {
        Object obj = request.getAttribute("openId");
        if (null == obj) throw new NotRuleException("openId");
        return obj.toString();
    }

    public SimpleResult checkBindMobile(HttpServletRequest request) throws NotRuleException {
        String accountId = ServiceManager.accountService.getAccountId(request);

        Account account = ServiceManager.accountService.findOne(accountId);
        if (!StringUtils.isEmpty(account.getMobile())) return new SimpleResult("YES");
        return new SimpleResult("NO");
    }

    public Boolean checkBindMobileUnique(String phone) {

        List<Account> account = ServiceManager.accountRepository.findByMobile(phone);
        return account.size() == 0;
    }


    /**
     * 获取优驾行用户请求token
     */
    public String getAccessTokenWinstar(String openid, String type) {
        Map<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("userId", openid);
        urlVariables.put("type", type);
        ResponseEntity<String> responseEntity = null;

        ResponseEntity<String> result = restTemplate.exchange(getTokenInfoUrl, HttpMethod.GET, responseEntity, String.class, urlVariables);
        if (result.getStatusCode().is2xxSuccessful()) {
            WinstarAccessToken winstarAccessToken = JSON.parseObject(result.getBody(), WinstarAccessToken.class);
            if (null != winstarAccessToken)
                return winstarAccessToken.getToken();
        }
        return "";
    }

    /**
     * 校验优驾行用户是否绑定驾驶证
     *
     * @param token
     * @return
     */
    public ResponseEntity checkBindDriverLicenseWinstar(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token_id", token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(checkBindLicenseUrl, HttpMethod.GET, entity, String.class);
        return result;
    }

    public ResponseEntity authDriverLicense(String token, String serialNum, String infoCard, String verify, String phone) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("token_id", token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = new StringBuilder("serialNum=").append(serialNum)
                .append("&infoCard=").append(infoCard)
                .append("&verify=").append(verify)
                .append("&phone=").append(phone).toString();
        HttpEntity<Map<String, Object>> entity = new HttpEntity(requestBody, headers);

        ResponseEntity<String> result = restTemplate.exchange(authDriverLicenseUrl, HttpMethod.POST, entity, String.class);
        return result;
    }
}
