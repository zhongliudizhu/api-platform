package com.winstar.user.utils;


import java.util.UUID;

public class UUIDUtils {
    /**
     * 生成32位编码
     *
     * @return string
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }


}
