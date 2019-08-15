package com.winstar.activityCenter.service;

import com.winstar.activityCenter.vo.SmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zl on 2019/1/3
 */
@Service
public class SmsService {

    private final
    SmsConfig clSmsConfig;

    @Autowired
    public SmsService(SmsConfig clSmsConfig) {
        this.clSmsConfig = clSmsConfig;
    }

    public boolean sendSms(String phone, String message) {
        String str = new RestTemplate().getForObject(clSmsConfig.sendMsg(phone, message), String.class);
        return str.contains("true");
    }

}
