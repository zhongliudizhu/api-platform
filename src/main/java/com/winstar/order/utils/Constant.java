package com.winstar.order.utils;

/**
 * @author  syc  2017/12/14 11:20
 * 常量
 */
public class Constant {
    /*
    * 订单生成
    * */
    public static final int ORDER_STATUS_CREATE = 1;
    /*
    * 待发货（已支付）
    * */
    public static final int ORDER_STATUS_WAIT_SEND = 2;
    /*
    * 已发货
    * */
    public static final int ORDER_STATUS_SENDED = 3;
    /*
    * 已确认收货
    * */
    public static final int ORDER_STATUS_RECEIVED = 4;
    /*
    * 部分收货
    * */
    public static final int ORDER_STATUS_PART_RECEIVED = 5;

    /*
    * 未支付
    * */
    public static final int PAY_STATUS_NOT_PAID = 0;
    /*
    * 支付中
    * */
    public static final int PAY_STATUS_PAID = 1;
    /*
    * 订单项状态：待发货
    * */
    public static final int ORDERSITEMS_STATUS_WAIT = 1;
    /*
    * 订单项状态：发货中
    * */
    public static final int ORDERSITEMS_STATUS_SENDING = 2;
    /*
    * 订单项状态：已收货
    * */
    public static final int ORDERSITEMS_STATUS_RECEIVED = 3;

    /*
    * 订单项状态：发货失败
    * */
    public static final int ORDERSITEMS_STATUS_FAIL = 4;

    /*
    * 待发货
    * */
    public static final int SEND_STATUS_WAIT = 1;
    /*
    * 发货失败
    * */
    public static final int SEND_STATUS_FAILED = 2;
    /*
    * 发货成功
    * */
    public static final int SEND_STATUS_SUCCESS = 3;
    /*
    * 部分发货成功
    * */
    public static final int SEND_STATUS_PART_SUCCESS = 4;

    /*
    * 订单是否正常：正常
    * */
    public static final String IS_NORMAL_NORMAL = "0";
    /*
    * 订单是否正常：已取消
    * */
    public static final String IS_NORMAL_CANCELED = "1";
    /*
    * 账户来源：微信
    * */
    public static final int ORDER_FROM_WECHAT = 1;

    /*
    * 退款状态初始化值
    * */
    public static final int REFUND_STATUS_ORIGINAL = 0;
    /*
    * 退款状态：成功
    * */
    public static final int REFUND_STATUS_SUCCESS = 1;

    public static final String ONE_BUY_ITEMID = "8";

    public static final int ONE_DAY_MAX = 500;


}
