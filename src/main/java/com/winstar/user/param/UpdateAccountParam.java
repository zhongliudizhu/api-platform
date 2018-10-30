package com.winstar.user.param;

import lombok.*;

@Data
public class UpdateAccountParam {
    /**
     * mobile
     */
    private String mobile;
    /**
     * 短信验证码
     */
    private String msgVerifyCode;

    /**
     * 短信验证ID
     */
    private String msgVerifyId;
}
