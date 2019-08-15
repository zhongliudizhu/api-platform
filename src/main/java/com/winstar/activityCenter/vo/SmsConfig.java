package com.winstar.activityCenter.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class SmsConfig {
    private static String account;

    @Value("${info.account}")
    public void setAccount(String account){
        SmsConfig.account = account;
    }

    private static String password;

    @Value("${info.password}")
    public void setPassword(String password){
        SmsConfig.password = password;
    }

    private static String sendUrl;

    @Value("${info.sms.sendUrl}")
    public void setSendUrl(String sendUrl){
        SmsConfig.sendUrl = sendUrl;
    }

    public static String sendMsg(String phone,String message){
        return sendUrl + "?un=" + account + "&pw=" + password + "&da=" + phone + "&sm=" + message + "&dc=15&rd=1&rf=2&tf=3";
    }

}
