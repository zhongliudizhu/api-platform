package com.winstar.cashier.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.Security;

/**
 * 
 * @ClassName: EnDe3DES
 * @Description: 3DES加密算法
 * @author tolly
 * @date 2013-7-5 下午3:12:42
 * 
 */
@SuppressWarnings("restriction")
public class EnDe3DES {
	
	private static String DesCharset = "UTF-8";//加密算法字符集
	
	private static String Algorithm = "DESede";
	
	static {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}
	
	public static byte[] encode(byte[] input, byte[] key)
		throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		return cipherByte;
	}
	
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	/**
	 * 
	 * EnycrptDes: 加密，默认字符集 . <br/>
	 *
	 * @author tolly
	 * @param src
	 * @param key 24位定长
	 * @return String
	 * @throws Exception
	 */
	public static String EnycrptDes(String src, String key)
		throws Exception {
		key = getKeyLen(key, 24);//生成24位密钥
		String ret = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(src)) {
			ret = byte2hex(encode(src.getBytes(DesCharset), key.getBytes(DesCharset)));
		}
		return ret;
	}
	
	/**
	 * 
	 * EnycrptDes: 加密，自定义字符集 . <br/>
	 *
	 * @author tolly
	 * @param src
	 * @param key 24位定长
	 * @param charSet
	 * @return String
	 * @throws Exception
	 */
	public static String EnycrptDes(String src, String key, String charSet)
		throws Exception {
		key = getKeyLen(key, 24);//生成24位密钥
		String ret = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(src)) {
			ret = byte2hex(encode(src.getBytes(charSet), key.getBytes(charSet)));
		}
		return ret;
	}

	/**
	 * 
	 * getKeyLen: 字符串后固定长度位，不够左补0 . <br/>
	 *
	 * @author tolly
	 * @param key
	 * @param len
	 * @return String
	 */
	public static String getKeyLen(String key, int len) {
		String keyLen = StringUtils.EMPTY;
		if(key.length()>=len){
			keyLen = StringUtils.substring(key, key.length()-len);
		}else {
			keyLen = StringUtils.leftPad(key, len, "0");
		}
		return keyLen;
	}
}
