package com.winstar.cashier.creditpay.config;

import com.winstar.utils.WsdUtils;
import netpay.merchant.crypto.ABAProvider;
import netpay.merchant.crypto.RSAPubKey;
import org.apache.commons.lang3.StringUtils;

import java.security.Security;
import java.security.Signature;
import java.util.Map;

public class RSASig{

	static String[] paramKeys = new String[]{
			"POSID", "BRANCHID", "ORDERID", "PAYMENT", "CURCODE", "REMARK1", "REMARK2",
			"ACC_TYPE", "SUCCESS", "TYPE", "REFERER", "CLIENTIP", "ACCDATE", "USRMSG",
			"INSTALLNUM", "ERRMSG", "USRINFO"};

	public static boolean verifySigature(Map<String, String> respMap, String key){
		try{
			String sign = respMap.get("SIGN");
			String src = getSrcStr(respMap);
			System.out.println("src--->" + src);
			Security.addProvider(new ABAProvider());
			Signature sigEng = Signature.getInstance("MD5withRSA","ABA");
			byte[] pubbyte = hexStrToBytes(key);
			sigEng.initVerify(new RSAPubKey(pubbyte));
			sigEng.update(src.getBytes());
			byte[] sign1 = hexStrToBytes(sign);
			return sigEng.verify(sign1);
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Transform the specified Hex String into a byte array.
	 */
	private static byte[] hexStrToBytes(String	s) {
		byte[]	bytes = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte)Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	private static String getSrcStr(Map<String, String> respMap){
		String retStr = StringUtils.EMPTY;
		StringBuilder sbd = new StringBuilder(StringUtils.EMPTY);
		for (String key : paramKeys) {
			if((key.equals("ACCDATE") || key.equals("USRMSG") || key.equals("INSTALLNUM") || key.equals("ERRMSG") || key.equals("USRINFO") || key.equals("DISCOUNT"))
					&& WsdUtils.isEmpty(respMap.get(key))){
				continue;
			}
			sbd.append(key).append("=").append(StringUtils.trimToEmpty(respMap.get(key))).append("&");
		}
		if (sbd.length() >= 1) {
			retStr = sbd.subSequence(0, sbd.length() - 1).toString();
		}
		return retStr;
	}

}