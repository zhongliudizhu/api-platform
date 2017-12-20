package com.winstar.cashier.construction.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 格式化当前系统时间（精确到秒）
	 * @return String
	 */
	public static String currentTime() {
		String strYMDHMSS = StringUtils.EMPTY;
		Date currentDateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		strYMDHMSS = formatter.format(currentDateTime);
		return strYMDHMSS;
	}
	
	/**
	 * 格式化当前系统时间（精确到毫秒）
	 * @return String
	 */
	public static String currentTimeToSS() {
		String strYMDHMSSS = StringUtils.EMPTY;
		Date currentDateTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		strYMDHMSSS = formatter.format(currentDateTime);
		return strYMDHMSSS;
	}

	public static Date parseTime(String timeStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try{
			return  formatter.parse(timeStr);
		}catch(ParseException e){
			return new Date();
		}
	}
	public static String currentTime(Date date ) {
		String strYMDHMSS = StringUtils.EMPTY;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		strYMDHMSS = formatter.format(date);
		return strYMDHMSS;
	}
}
