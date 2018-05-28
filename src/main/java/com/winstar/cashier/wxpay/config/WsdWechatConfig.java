package com.winstar.cashier.wxpay.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2017/7/18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WsdWechatConfig {

    /**
     * 微信签名key
     */
    public String signKey = "B793c5IPBC7B9wFQ5p55JFpx37S3iNRy";

    /**
     * 微信APP支付的商户号
     */
    private String mchId = "1434171402";

    /**
     * 微信公众号支付的商户号
     */
    private String mchId_wechat = "1249655701";

    /**
     * 微信公众号支付的商户号
     */
    private String mchId_wechat_test = "1504450061";

    /**
     * 公众号Id
     */
    private String appId = "wxcf71d6832b8e3ebe";

    /**
     * 公众号密钥
     */
    private String appSecret = "9401a98f94e021d3f006c3831f8d0e05";

    /**
     * 公众号Id
     */
    private String appId_test = "wx47a21bee64eb7a6c";

    /**
     * 公众号密钥
     */
    private String appSecret_test = "43dd74d136a23e8467e5866f4757a3ec";

    /**
     * 小程序Id
     */
    private String appId_little;

    /**
     * 小程序密钥
     */
    private String appSecret_little;

    /**
     * appId
     */
    private String appId_app = "wx59c20afc62e2c39c";

    /**
     * app密钥
     */
    private String appSecret_app = "43d43c1bbe551e38ecdff718f1f66fab";

    /**
     * 子商户号
     */
    private String subMchId;

    /**
     * HTTPS证书的本地路径
     */
    private String certLocalPath;

    /**
     * 微信公众号证书路径
     */
    private String certLocalPath_wechat;

    /**
     * HTTPS证书密码，默认密码等于商户号MCHID
     */
    private String certPassword = "1434171402";

    /**
     * 微信公众号证书密码
     */
    private String certPassword_wechat = "1249655701";

    /**
     * 微信回调地址(测试)
     */
    private String backUrl_test = "http://develop.sxeccellentdriving.com/api/v1/cbc/wxPay/notify";

    /**
     * 微信回调地址(正式)
     */
    private String backUrl_prod = "https://mobile.sxwinstar.net/ccb-api/api/v1/cbc/wxPay/notify";

}
