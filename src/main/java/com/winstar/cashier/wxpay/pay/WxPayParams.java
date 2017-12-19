package com.winstar.cashier.wxpay.pay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2017/3/31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxPayParams {

    /**
     * appId
     */
    private String appid;

    /**
     * partnerId
     */
    private String partnerid;

    /**
     * 交易会话Id
     */
    private String prepayid;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * H5支付链接
     */
    private String web_url;

    private String errorMsg;

}
