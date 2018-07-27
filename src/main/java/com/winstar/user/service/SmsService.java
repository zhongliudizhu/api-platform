package com.winstar.user.service;

import com.winstar.ClientErrorHandler;
import com.winstar.user.param.UpdateAccountParam;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author laohu
 * @date 2017/12/11 17:52
 * @desc
 **/
@Service("SmsService")
public class SmsService {
    private static Logger logger = Logger.getLogger(SmsService.class);

    @Value("${bank.sms.sendVerifyCodeUrl}")
    String sendVerifyCodeUrl;
    @Value("${bank.sms.checkVerifyCodeUrl}")
    String checkVerifyCodeUrl;
    @Value("${bank.sms.user}")
    String smsUser;
    @Value("${bank.sms.pwd}")
    String smsPwd;

    @Value("${verify_sms_url}")
    String verifySmsUrl;

    /**
     * 发送建行信息卡验证码
     *
     * @param msgContent
     * @return
     */
    public SendVerifyCodeMsgResult sendSms(String msgContent) {
        SendVerifyCodeMsgResult sendVerifyCodeMsgResult = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String plainCreds = smsUser + ":" + smsPwd;
            byte[] plainCredsBytes = plainCreds.getBytes();
            byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
            String base64Creds = new String(base64CredsBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Creds);
            logger.info("base64Creds:"+base64Creds);
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());

            HttpEntity<String> formEntity = new HttpEntity<String>(msgContent, headers);
            sendVerifyCodeMsgResult = restTemplate.postForObject(sendVerifyCodeUrl, formEntity, SendVerifyCodeMsgResult.class);
        } catch (RestClientException e) {
            logger.error(new StringBuilder("验证码发送失败-->").append(msgContent), e);
        }
        return sendVerifyCodeMsgResult;
    }

    /**
     * 信息卡验证码校验
     *
     * @param msgContent
     * @return
     */
    public AuthVerifyCodeMsgResult authSms(String msgContent) {

        AuthVerifyCodeMsgResult authVerifyCodeMsgResult = null;

        RestTemplate restTemplate = new RestTemplate();
        String plainCreds = smsUser + ":" + smsPwd;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        logger.info("base64Creds:"+base64Creds);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(msgContent, headers);
        authVerifyCodeMsgResult = restTemplate.postForObject(checkVerifyCodeUrl, formEntity, AuthVerifyCodeMsgResult.class);

        return authVerifyCodeMsgResult;
    }


    /**
     * 验证登录短信是否正确
     *
     * @return
     */
    public boolean verifySms(UpdateAccountParam updateAccountParam) {


        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("phoneNumber", updateAccountParam.getMobile());
        urlVariables.put("id", updateAccountParam.getMsgVerifyId());
        urlVariables.put("verifyCode", updateAccountParam.getMsgVerifyCode());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ClientErrorHandler());

        ResponseEntity resp = restTemplate.getForEntity(verifySmsUrl, String.class, urlVariables);
        logger.debug("发送验证码"+resp.getBody().toString());
        if (resp.getStatusCode().is2xxSuccessful()) {
            return true;
        }
        return false;
    }
}
