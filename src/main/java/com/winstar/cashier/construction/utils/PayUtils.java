/**
 * 
 */
package com.winstar.cashier.construction.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 工具类
 * @author 清算平台项目组
 * @version v1.0.2
 * @date 2013-04-17
 */
public class PayUtils {
	
	/**
	 * 签名串计算
	 * @param paramVal 参与签名的参数值
	 * @param paramKeys 参与签名的参数
	 * @return 签名结果
	 */
	public static String signDictionary(Map<String, String> paramVal,String[] paramKeys,String signKey,String charset) {
		String charsetplus = StringUtils.isBlank(charset) ? PayConfPC.CHARSET : charset;
		StringBuffer envsrc = new StringBuffer(StringUtils.EMPTY);
		for (int i = 0; i < paramKeys.length; i++) {
			envsrc.append("&").append(paramKeys[i]).append("=").append(paramVal.get(paramKeys[i]));
		}
		envsrc.append("&").append(Crypto.GetMessageDigest(signKey, "MD5",charsetplus));
		System.out.print(envsrc);
		return Crypto.GetMessageDigest(envsrc.toString().substring(1), "MD5",charsetplus);// 返回加密后的字符串
	}
	
	/**
     * 元转换为分
     * 
     * @param amount
     * @return int
     */
    public static int convertAmountY2F(Double amount) {
        double orderAmountSrc = Arith.mul(amount, new Double("100"));
        int orderAmount = (int) orderAmountSrc;
        return orderAmount;
    }

	public static double convertAmountF2Y(int amount){
		return Arith.div(amount,100,2);
	}

}
