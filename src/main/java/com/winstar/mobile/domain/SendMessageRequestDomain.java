package com.winstar.mobile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2020/3/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequestDomain {

    /**
     * 手机号
     */
    private String serialNumber;

    /**
     * 验证码
     */
    private String SmsCode;

    /**
     * 业务编码
     */
    private String businessCode;

    public SendMessageRequestDomain(String mobile, String businessCode){
        this.serialNumber = mobile;
        this.businessCode = businessCode;
    }

}
