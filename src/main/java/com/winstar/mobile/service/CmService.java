package com.winstar.mobile.service;

import com.alibaba.fastjson.JSON;
import com.winstar.mobile.config.Config;
import com.winstar.mobile.domain.*;
import com.winstar.redis.RedisTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zl on 2020/3/6
 */
@Service
@Slf4j
public class CmService {

    static RedisTools redisTools;

    static String cmTokenKey = "cm_token_key";

    @Autowired
    private void setRedisTools(RedisTools redisTools){
        CmService.redisTools = redisTools;
    }

    /**
     * 获取token
     */
    public String getToken(){
        Object token = redisTools.get(cmTokenKey);
        if(!ObjectUtils.isEmpty(token)){
            log.info("token未失效，直接返回！token：" + token.toString());
            return token.toString();
        }
        log.info("token已失效，重新获取......");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(new GetTokenRequestDomain()),headers);
        log.info("获取token请求参数：" + JSON.toJSONString(new GetTokenRequestDomain()));
        CmResult cmResult = new RestTemplate().exchange(Config.prodMain +  Config.getTokenUrl, HttpMethod.POST, entity, CmResult.class).getBody();
        log.info("请求移动获取token返回结果：" + JSON.toJSONString(cmResult));
        if(cmResult.isSuccess()){
            GetTokenResponseDomain responseDomain = JSON.parseObject(JSON.toJSONString(cmResult.getDataInfo()), GetTokenResponseDomain.class);
            redisTools.set(cmTokenKey, responseDomain.getAccessToken(), Long.valueOf(responseDomain.getExpiresTime()));
            return responseDomain.getAccessToken();
        }
        log.info("token获取失败，失败原因：" + cmResult.getMessage());
        return null;
    }

    /**
     * 发送短信验证码
     */
    public CmResult sendMessage(SendMessageRequestDomain sendMessageRequestDomain){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("version", "V2.0.0");
        headers.set("accessToken", getToken());
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(sendMessageRequestDomain),headers);
        CmResult cmResult = new RestTemplate().exchange(Config.prodMain +  Config.messageUrl, HttpMethod.POST, entity, CmResult.class).getBody();
        log.info("请求移动发送验证码返回结果：" + JSON.toJSONString(cmResult));
        return cmResult;
    }

    /**
     * 营销活动校验
     */
    public CmResult verify(VerifyRequestDomain verifyRequestDomain){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("version", "V1.0.0");
        headers.set("accessToken", getToken());
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(verifyRequestDomain),headers);
        CmResult cmResult = new RestTemplate().exchange(Config.prodMain +  Config.activityVerifyUrl, HttpMethod.POST, entity, CmResult.class).getBody();
        log.info("请求移动营销活动校验返回结果：" + JSON.toJSONString(cmResult));
        return cmResult;
    }

    /**
     * 营销活动登记
     */
    public CmResult create(VerifyRequestDomain verifyRequestDomain){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("version", "V2.1.0");
        headers.set("accessToken", getToken());
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(verifyRequestDomain),headers);
        CmResult cmResult = new RestTemplate().exchange(Config.prodMain +  Config.activityCreateUrl, HttpMethod.POST, entity, CmResult.class).getBody();
        log.info("请求移动营销活动登记返回结果：" + JSON.toJSONString(cmResult));
        return cmResult;
    }

}
