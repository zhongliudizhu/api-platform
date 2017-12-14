package com.winstar.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shoo on 2017/7/12.
 * 付款信息
 */
@Setter
@Getter
public class PayInfoVo {
    /*
    * 订单序列号
    * */
    private String orderSerialNumber;
    /*
    * 付款金额
    * */
    private Double payPrice;
    /*
    * 付款类型
    * */
    private Integer payType;
    /*
   * 银行付款流水号
   * */
    private String bankSerialNumber;
    /*
   * 付款状态
   * */
    private Integer payState;
    /*
   * 付款时间
   * */
    private Date payTime;


}
