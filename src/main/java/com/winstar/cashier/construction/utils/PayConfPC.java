package com.winstar.cashier.construction.utils;

/**
 * Created by zl on 2016/12/14
 */
public class PayConfPC {

    /**
     * 公共参数
     */
    public final static String CHARSET = "UTF-8";
    public final static String SIGNMETHOD = "MD5";
    public final static String VERSION_CODE = "2.0";
    public final static String APP_SCHEMA = "winstar123546";
    public final static String BILL_DOWNLOAD_TRANSCODE1 = "F001";
    public final static String BILL_DOWNLOAD_TRANSCODE2 = "F002";
    public final static String ORDER_CURRENCY = "156";
    public final static String PAY_TRANSTYPE = "01";
    public final static String PAY_TYPE = "B2C";

    /**
     * 测试环境
     */
    public final static String TEST_BACK_END_URL = "http://develop.sxeccellentdriving.com/api/v1/cbc/payCallback";
    public final static String TEST_BILL_DOWNLOAD_URL = "http://test.ezf123.com/jspt/download/fileDownloadReq.action";
    public final static String TEST_EZFMER_PAY_URL = "http://test.ezf123.com/jspt/payment/back-mobilepay.action";
    public final static String TEST_JSPT_PAY_URL = "http://test.ezf123.com/jspt/payment/frontTransReq.action";
    public final static String TEST_JSPT_QUERY_URL = "http://test.ezf123.com/jspt_query/payment/front-order-query.action";
    public final static String TEST_MER_ID = "10384";
    public final static String TEST_REF_TRANSTYPE = "02";
    public final static String TEST_REF_BACK_END_URL = "";
    public final static String TEST_REF_PAY_URL = "http://test.ezf123.com/jspt/payment/order-refund.action";
    public final static String TEST_REF_QUERY_URL = "http://test.ezf123.com/jspt_query/payment/front-refund-query.action";
    public final static String TEST_SIGN_KEY = "W76T1AZU669T628A1J4KYGQDA2446MZ2H17M8FQDUZMIQUYIENMSPWYTS8QQ";


    /**
     * 正式环境
     */
    public final static String PROD_BACK_END_URL = "https://mobile.sxwinstar.net/api/v1/cbc/payCallback";
    public final static String PROD_BILL_DOWNLOAD_URL = "https://www.ezf123.com/jspt/download/fileDownloadReq.action";
    public final static String PROD_EZFMER_PAY_URL = "https://www.ezf123.com/jspt/payment/back-mobilepay.action";
    public final static String PROD_JSPT_PAY_URL = "https://www.ezf123.com/jspt/payment/frontTransReq.action";
    public final static String PROD_JSPT_QUERY_URL = "https://www.ezf123.com/jspt_query/payment/front-order-query.action";
    public final static String PROD_MER_ID = "57629";
    public final static String PROD_REF_TRANSTYPE = "02";
    public final static String PROD_REF_BACK_END_URL = "";
    public final static String PROD_REF_PAY_URL = "https://www.ezf123.com/jspt/payment/order-refund.action";
    public final static String PROD_REF_QUERY_URL = "https://www.ezf123.com/jspt_query/payment/front-refund-query.action";
    public final static String PROD_SIGN_KEY = "U3JGX6T22YFEAFPSXPHM864F8PF5TXG6PPNF6YAK4D5BUYX3ULSZ6Q82EI5M";

}
