package com.winstar.weekendBrand.param;

import com.winstar.user.param.UpdateAccountParam;
import lombok.Data;

@Data
public class ReceiveRedPackageParam {
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
    /**
     * 订单号
     */
    private String orderId;

    public UpdateAccountParam getUpdateAccountParam() {
        return new UpdateAccountParam(this.mobile, this.msgVerifyCode, this.msgVerifyId);
    }
}
