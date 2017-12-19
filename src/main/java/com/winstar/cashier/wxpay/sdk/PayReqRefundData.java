package com.winstar.cashier.wxpay.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 统一下单请求数据封装
 * @author zhanglin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayReqRefundData {
	
	//公众账号ID	微信分配的公众账号ID（企业号corpid即为此appId）
	private String appid = "";
	//商户号		微信支付分配的商户号
    private String mch_id = "";
    //随机字符串	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str = "";
    //签名		签名，详见签名生成算法
    private String sign = "";
    //商品描述		商品或支付单简要描述
    private String body = "";
    //商户订单号	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private String out_trade_no = "";
    //总金额		订单总金额，单位为分
    private String total_fee = "";
    //微信订单号
    private String transaction_id = "";
    
    //商户退款单号 	商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    private String out_refund_no = "";
    //退款金额 		退款总金额，订单总金额，单位为分，只能为整数
    private String refund_fee = "";
    //操作员 		操作员帐号, 默认为商户号
    private String op_user_id = "";
    
}
