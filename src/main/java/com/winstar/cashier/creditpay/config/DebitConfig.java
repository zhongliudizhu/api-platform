package com.winstar.cashier.creditpay.config;

/**
 * Created by zl on 2017/12/20
 */
public class DebitConfig {

    /**
     * 商户号
     */
    public static final String merchantid = "105791000001084";
    /**
     * 分行代码
     */
    public static final String branchid = "610000000";
    /**
     * 商户柜台代码
     */
    public static final String posid = "012760994";
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
    public static final String pubkey30 = "0c74ed5aab19a4568b47357f020111";
    /**
     * 公钥
     */
    public static final String pubkey = "30819c300d06092a864886f70d010101050003818a003081860281806e9ffec199273dee0ef277209a2c543b9d3d878114982d7a946f90d1f4094e98a32a774eabaa1f297154d552aa0075351f5856e4edf9f95bf071ede5e4f4c97ea16806f950e81d85eeb1c0602c9807b90c713702cc79a106e98b593473b4ea93fc19738aea709e3b7b7a6b4f8aeeaf237084629b0c74ed5aab19a4568b47357f020111";
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
