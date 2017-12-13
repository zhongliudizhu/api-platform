package com.winstar.utils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by zl on 2016/10/13
 */
public class WsdUtils {

    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj
     *            待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if (pObj == "")
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) return true;
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) return true;
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) return true;
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isNotEmpty(Object pObj) {
        if (pObj == null)
            return false;
        if (pObj == "")
            return false;
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) return false;
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) return false;
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) return false;
        }
        return true;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String formatDate(String nowDate,String oldFormat,String newFormat) throws Exception{
        if(isEmpty(nowDate)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        Date date = sdf.parse(nowDate);
        return new SimpleDateFormat(newFormat).format(date);
    }

}
