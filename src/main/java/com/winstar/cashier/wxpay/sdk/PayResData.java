package com.winstar.cashier.wxpay.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 统一下单返回数据封装
 * @author zhanglin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayResData {
	
	//协议层
    private String return_code = "";
    private String return_msg = "";
    //协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    private String appid = "";
    private String mch_id = "";
    private String nonce_str = "";
    private String sign = "";
    private String result_code = "";
    private String err_code = "";
    private String err_code_des = "";
    private String device_info = "";
	//交易类型 		调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定	示例值：JSAPI	
	private String trade_type = "";
	//预支付交易会话标识	微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时	示例值：wx201410272009395522657a690389285100
	private String prepay_id = "";
	//二维码链接	trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	private String code_url = "";
	
}
