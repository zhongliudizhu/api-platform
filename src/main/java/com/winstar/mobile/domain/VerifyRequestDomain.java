package com.winstar.mobile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2020/3/6
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRequestDomain {

    /**
     * 营销产品编码
     */
    private String productId;

    /**
     * 营销包编码
     */
    private String packageId;

    /**
     * 手机号
     */
    private String serialNumber;

    /**
     * 短信验证码
     */
    private String smsCode;

}
