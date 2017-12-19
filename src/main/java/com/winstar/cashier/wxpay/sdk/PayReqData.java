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
public class PayReqData {
	
	//公众账号ID	微信分配的公众账号ID（企业号corpid即为此appId）
	private String appid = "";
	//商户号		微信支付分配的商户号
    private String mch_id = "";
    //设备号		终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    private String device_info = "";
    //随机字符串	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str = "";
    //签名		签名，详见签名生成算法
    private String sign = "";
	//签名类型
	private String sign_type = "MD5";
    //商品描述		商品或支付单简要描述
    private String body = "";
	//商品详情
	private String detail = "";
    //附加数据		附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    private String attach = "";
	//商户订单号	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private String out_trade_no = "";
	//货币类型	默认人民币
	private String fee_type = "CNY";
	//总金额		订单总金额，单位为分
	private int total_fee = 0;
	//终端IP		APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    private String spbill_create_ip = "";
	//交易起始时间	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
    private String time_start = "";
    //交易结束时间	订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
    private String time_expire = "";
    //商品标记		商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    private String goods_tag = "";
    //通知地址		接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    private String notify_url = "";
    //交易类型		取值如下：JSAPI，NATIVE，APP
    private String trade_type = "";
    //商品ID		trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    private String product_id = "";
    //指定支付方式	no_credit--指定不能使用信用卡支付
    private String limit_pay = "no_credit";
    //用户标识
    private String openid = "";
    //微信订单号
    private String transaction_id = "";
	//场景信息
	private String scene_info = "";
    
}
