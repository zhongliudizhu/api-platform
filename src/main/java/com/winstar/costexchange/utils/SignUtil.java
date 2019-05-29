package com.winstar.costexchange.utils;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by zl on 2019/5/16
 * 签名验签类
 */
public class SignUtil {

    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    public static String merchant = "61045834521";
    private static String secret = "6abab93252ea1a93c6c36a4585075da1b7116c6cdc0dccc8c8fe14e633289305";

    public static String sign(Map<String, String> parameters) {
        StringBuilder param = new StringBuilder();
        TreeMap<String, String> map = new TreeMap<>(parameters);
        Set es = map.entrySet();
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (!StringUtils.isEmpty(value)) {
                param.append(key).append("=").append(value).append("&");
            }
        }
        param.append(secret);
        logger.info("待签名字符串：" + param.toString());
        return encodeMd5(param.toString().getBytes());
    }

    public static String getParameters(Map<String, String> parameters){
        StringBuilder param = new StringBuilder();
        TreeMap<String, String> map = new TreeMap<>(parameters);
        Set es = map.entrySet();
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            Object value = entry.getValue();
            if (!StringUtils.isEmpty(value)) {
                param.append((String) entry.getKey()).append("=").append(value).append("&");
            }
        }
        param.append("sign=").append(sign(parameters));
        return param.toString();
    }

    public static boolean checkSign(Map<String, String> parameters) {
        String sign = MapUtils.getString(parameters, "sign");
        parameters.remove("sign");
        return sign.equals(sign(parameters));
    }

    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static String encodeHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) aByte & 0xff, 16));
        }
        return buffer.toString();
    }

}
