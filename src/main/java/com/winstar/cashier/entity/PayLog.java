package com.winstar.cashier.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 支付日志记录
 * @author wanghaibo
 */
@Entity
@Table(name="CASHIER_PAY_LOG")
public class PayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "ORDER_NUMBER", length = 50)
    private String orderNumber;

    /**
     * 订单金额  单位：分
     */
    @Column(name = "ORDER_AMOUNT", length = 12)
    private String orderAmount;

    /**
     * 访问时间
     */
    @Column(name = "CREATED_AT")
    private Date createdAt;

    /**
     * 客户端ip
     */
    @Column(name = "CUSTOMER_IP")
    private String customerIp;

    /**
     * 访问接口
     */
    @Column(name = "apply_url", length = 255)
    private String applyUrl;

    /**
     * 请求报文
     */
    @Column(name = "req_info", length = 4000)
    private String reqInfo;

    /**
     * 响应报文
     */
    @Column(name = "resp_info", length = 4000)
    private String respInfo;

    /**
     * 信息内容
     */
    @Column(name = "MESSAGE",length = 255)
    private String message;

    /**
     * 信息编码
     */
    @Column(name = "CODE",length = 32)
    private String code;

    /**
     * 交易流水号
     */
    @Column(name = "qid",length = 50)
    private String qid;

    /**
     * 支付金额
     */
    @Column(name = "payAmount",length = 12)
    private String payAmount;

    public PayLog(){}

    public PayLog(String orderNumber,String orderAmount,String customerIp,String applyUrl,String reqInfo,String code,String message){
        this.orderNumber = orderNumber;
        this.orderAmount = orderAmount;
        this.createdAt = new Date();
        this.customerIp = customerIp;
        this.applyUrl = applyUrl;
        this.reqInfo = reqInfo;
        this.code = code;
        this.message = message;
    }

    public PayLog(String orderNumber,String orderAmount,String customerIp,String applyUrl,String reqInfo,String code,String message,String payAmount,String qid,String respInfo){
        this.orderNumber = orderNumber;
        this.orderAmount = orderAmount;
        this.createdAt = new Date();
        this.customerIp = customerIp;
        this.applyUrl = applyUrl;
        this.reqInfo = reqInfo;
        this.code = code;
        this.message = message;
        this.payAmount = payAmount;
        this.qid = qid;
        this.respInfo = respInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public String getReqInfo() {
        return reqInfo;
    }

    public void setReqInfo(String reqInfo) {
        this.reqInfo = reqInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getRespInfo() {
        return respInfo;
    }

    public void setRespInfo(String respInfo) {
        this.respInfo = respInfo;
    }
}
