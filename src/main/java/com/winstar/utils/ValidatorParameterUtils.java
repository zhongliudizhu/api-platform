package com.winstar.utils;

import java.util.regex.Pattern;

/**
 * Created by zl on 2019/8/20
 */
public class ValidatorParameterUtils {

    public static final String REGEX_MOBILE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";

    /**
     * 校验手机号
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

}
