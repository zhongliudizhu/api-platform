package com.winstar.cashier.creditpay.config;

/**
 * Created by zl on 2017/12/20
 */
public class CreditConfig {

    /**
     * 商户号
     */
    public static final String merchantid = "105791000000574";
    /**
     * 分行代码
     */
    public static final String branchid = "610000000";
    /**
     * 商户柜台代码
     */
    public static final String posid = "006062988";
    /**
     * 币种 01：人民币
     */
    public static final String curcode = "01";
    /**
     * 交易码
     */
    public static final String txcode = "520100";
    /**
     * 公钥后30位
     */
    public static final String pubkey30 = "658ca67f6a993719540a3b6b020111";
    /**
     * 公钥
     */
    public static final String pubkey = "30819c300d06092a864886f70d010101050003818a0030818602818067513395779fd05204e783c8a81cf6623aafb3873573f6a5b244c342fe0870f1e961625e03e6888ec3037d4aa328186bce6d9c5d123a73b2def258bd9a68bf8b7798f39ae9d4effbc2d077942eaa5cd55222b63f279274e30eab5e7c0051094350c11953a6a47187cdf26bb2d3f3811ab20f9701658ca67f6a993719540a3b6b020111";
    /**
     * 交易地址
     */
    public static final String sendUrl = "https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain";
    /**
     * 客户在商户系统中的IP 测试
     */
    public static final String clientIp_test = "219.145.62.234";
    /**
     * 客户在商户系统中的IP 生产
     */
    public static final String clientIp_prod = "113.142.33.142";

}
