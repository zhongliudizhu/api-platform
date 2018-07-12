package com.winstar.obu.utils;

import com.winstar.ClientErrorHandler;
import com.winstar.user.param.UpdateAccountParam;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * SmsUtil
 *
 * @author: Big BB
 * @create 2018-07-11 18:38
 * @DESCRIPTION:
 **/
public class SmsUtil {
    private static Logger logger = Logger.getLogger(SmsUtil.class);

    public static ResponseEntity sendSms(String phoneNumber, String sendSmsUrl) {

        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("phoneNumber", phoneNumber);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ClientErrorHandler());

        ResponseEntity resp = restTemplate.getForEntity(sendSmsUrl, String.class, urlVariables);
        logger.debug("发送验证码"+resp.getBody().toString());
        return resp;
    }

    public static boolean verifySms(UpdateAccountParam updateAccountParam,String verifySmsUrl) {


        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("phoneNumber", updateAccountParam.getMobile());
        urlVariables.put("id", updateAccountParam.getMsgVerifyId());
        urlVariables.put("verifyCode", updateAccountParam.getMsgVerifyCode());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ClientErrorHandler());

        ResponseEntity resp = restTemplate.getForEntity(verifySmsUrl, String.class, urlVariables);
        logger.debug("校验验证码"+resp.getBody().toString());
        if (resp.getStatusCode().is2xxSuccessful()) {
            return true;
        }
        return false;
    }
}
