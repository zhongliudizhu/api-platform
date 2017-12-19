package com.winstar.cashier.wxpay.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayReqRefundQueryData {
	
	//公众账号ID	微信分配的公众账号ID（企业号corpid即为此appId）
	private String appid = "";
	//商户号		微信支付分配的商户号
    private String mch_id = "";
    //随机字符串	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str = "";
    //签名		签名，详见签名生成算法
    private String sign = "";
    //设备号
    private String device_info = "";
    //微信订单号
    private String transaction_id = "";
    //商户订单号
    private String out_trade_no = "";
    //商户退款单号
    private String out_refund_no = "";
    //微信退款单号
    private String refund_id = "";

}
