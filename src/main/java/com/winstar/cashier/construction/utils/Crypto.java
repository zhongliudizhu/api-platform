package com.winstar.cashier.construction.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能：基本加解密相关操作
 * <p>
 * Title: 基本加解密相关操作
 * </p>
 * <p>
 * Description: 基本加解密相关操作
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: 西安融联网络科技有限公司
 * </p>
 * 
 * @author 清算平台项目组
 * @version 1.0.2 date：Mar 25, 2010
 */
public class Crypto {
	
	private static final Logger log = Logger.getLogger(Crypto.class);
	
	/**
	 * 
	 * GetMessageDigest: 带字符集摘要计算 . <br/>
	 * 
	 * @author tolly
	 * @param strSrc
	 * @param encName
	 * @param charset
	 * @return String
	 * @since JDK 1.7
	 */
	public static String GetMessageDigest(String strSrc, String encName, String charset) {
		String charset_inner = StringUtils.isBlank(charset) ? PayConfPC.CHARSET : StringUtils.trimToEmpty(charset);
		MessageDigest md = null;
		String strDes = null;
		final String ALGO_DEFAULT = "SHA-1";
		try {
			if (StringUtils.isBlank(encName)) {
				encName = ALGO_DEFAULT;
			}
			md = MessageDigest.getInstance(encName);
			md.update(strSrc.getBytes(charset_inner));
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			log.error("不支持的摘要算法:" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.error("字符集错误:" + e.getMessage());
		}
		return strDes;
	}
	
	/**
	 * 
	 * bytes2Hex: 将字节数组转为HEX字符串(16进制串) . <br/>
	 * 
	 * @author tolly
	 * @param bts 要转换的字节数组
	 * @return 转换后的HEX串
	 * @since JDK 1.7
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = StringUtils.EMPTY;
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
