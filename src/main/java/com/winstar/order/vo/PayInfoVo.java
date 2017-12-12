package com.winstar.order.vo;

import java.util.Date;

/**
 * @author shoo on 2017/7/12.
 * 付款信息
 */
public class PayInfoVo {
    private String id;
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
    private Date bankTime;


    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getBankSerialNumber() {
        return bankSerialNumber;
    }

    public void setBankSerialNumber(String bankSerialNumber) {
        this.bankSerialNumber = bankSerialNumber;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Date getBankTime() {
        return bankTime;
    }

    public void setBankTime(Date bankTime) {
        this.bankTime = bankTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
