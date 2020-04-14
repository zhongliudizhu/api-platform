package com.winstar.mobile.config;

/**
 * Created by zl on 2020/3/6
 */
public class Config {

    /**
     * 用户id
     */
    public static String userId = "1234673582324604928";

    /**
     * 应用id
     */
    public static String clientId = "1235808878978666496";

    /**
     * 应用密钥
     */
    public static String clientSecret = "46aba93baf70ac0ebe8c764018ae40c4";

    /**
     * 测试域名
     */
    public static String testMain = "http://rctest.izaoban.cn/ecmp";

    /**
     * 正式域名
     */
    public static String prodMain = "http://sn.ac.10086.cn/ecmp";

    /**
     * 获取token
     */
    public static String getTokenUrl = "/openplatform/consumer/openApi/token/getAccessToken";

    /**
     * 营销活动校验
     */
    public static String activityVerifyUrl = "/openplatform/consumer/handle/checkYxhdService";

    /**
     * 营销活动 V2.1.0
     */
    public static String activityCreateUrl = "/openplatform/consumer/handle/yxhdServiceUpgrade";

    /**
     * 短信验证码发送与校验 V2.0.0
     */
    public static String messageUrl = "/openplatform/consumer/handle/sendSMSAndCheckUpgrade";

}
