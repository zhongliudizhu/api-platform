package com.winstar.order.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shoo on 2017/7/6 16:13.
 *  -- 订单 --
 */
@Entity
@Table(name = "oil_order")
public class OilOrder {
    /*
    *主键
    * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /*
    * 账户id
    * */
    @Column(length = 50)
    private String accountId;
    /*
     * 订单来源 1 微信 2 安卓  3 IOS 0 littleCode
     * */
    private Integer orderFrom;
    /*
    * 订单序列号
    * */
    @Column(length = 50)
    private String serialNo;
    /*
    * 付款总金额
    * */
    private Double payPrice;
    /*
    * 商品售价
    * */
    private Double salePrice;
    /*
    * 商品总值
    * */
    private Double itemTotalValue;
    /*
    * 优惠金额
    * */
    private Double discountAmount;
    /*
    * 优惠券id
    * */
    @Column(length = 50)
    private String couponId;
    /*
    * 商品id
    * */
    @Column(length = 50)
    private String itemId;

    /*
    * 活动id
    * */
    @Column(length = 50)
    private String activityId;
    /*
    * 油券明细
    * */
    @Column(length = 100)
    private String oilDetail;
    /*
    *手机号
    * */
    @Column(length = 50)
    private String phoneNo;
    /*
    *银行缴费流水号
    * */
    @Column(length = 50)
    private String bankSerialNo;
    /*
    *状态 1 订单生成(未支付) 2 待发货(支付成功) 3 已发货 4 已确认收货
    * */
    private Integer status;
    /*
    *支付状态 0 未支付 1支付成功
    * */
    private Integer payStatus;
    /*
    *发货状态  1待发货 2发货中 3发货成功 4发货失败
    * */
    private Integer sendStatus;
    /*
    *备注
    * */
    @Column(length = 100)
    private String mark;
    /*
    *下单时间
    * */
    private Date createTime;
    /*
    *付款时间
    * */
    private Date payTime;
    /*
    *订单完成时间
    * */
    private Date finishTime;
    /*
    * 最后修改时间
    * */
    private Date updateTime;
    /*
    * 退款  0 初始化  1  退款成功  2 退款失败 3 退款中 4 部分退款
    * */
    private Integer refund;
    /*
    * 修改订单地址
    * */
    private String updateOrderUrl;
    /*
    *  0 正常  1  促销  2 秒杀 3 活动
    * */
    @Column(length = 2)
    private String isPromotion;
    /*
    * 是否可用  0 正常  1 关闭
    * */
    @Column(length = 50)
    private String isAvailable;

    /*
    * 支付方式 0：建行 1：银联 2：微信 3：支付宝
    * */
    private Integer payType;

    public OilOrder() {
    }

    /* 订单来源 账户id 订单序列号 手机号 状态 支付状态 下单时间 退款 商品id 活动类型 秒杀id 活动id*/
    public OilOrder(Integer orderFrom, String accountId, String serialNo, String phoneNo, Integer status, Integer payStatus, Date createTime, Integer refund, String itemId, String isPromotion, String activityId) {
        this.accountId = accountId;
        this.serialNo = serialNo;
        this.phoneNo = phoneNo;
        this.status = status;
        this.payStatus = payStatus;
        this.createTime = createTime;
        this.refund = refund;
        this.itemId = itemId;
        this.isPromotion = isPromotion;
        this.orderFrom = orderFrom;
        this.activityId = activityId;
        this.discountAmount = 0.0;
        this.isAvailable = "0";
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getUpdateOrderUrl() {
        return updateOrderUrl;
    }

    public void setUpdateOrderUrl(String updateOrderUrl) {
        this.updateOrderUrl = updateOrderUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getItemTotalValue() {
        return itemTotalValue;
    }

    public void setItemTotalValue(Double itemTotalValue) {
        this.itemTotalValue = itemTotalValue;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(String isPromotion) {
        this.isPromotion = isPromotion;
    }

    public String getOilDetail() {
        return oilDetail;
    }

    public void setOilDetail(String oilDetail) {
        this.oilDetail = oilDetail;
    }

    public Integer getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(Integer orderFrom) {
        this.orderFrom = orderFrom;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "OilOrder{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", orderFrom=" + orderFrom +
                ", serialNo='" + serialNo + '\'' +
                ", payPrice=" + payPrice +
                ", salePrice=" + salePrice +
                ", itemTotalValue=" + itemTotalValue +
                ", discountAmount=" + discountAmount +
                ", couponId='" + couponId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", oilDetail='" + oilDetail + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", bankSerialNo='" + bankSerialNo + '\'' +
                ", status=" + status +
                ", payStatus=" + payStatus +
                ", sendStatus=" + sendStatus +
                ", mark='" + mark + '\'' +
                ", createTime=" + createTime +
                ", payTime=" + payTime +
                ", finishTime=" + finishTime +
                ", updateTime=" + updateTime +
                ", refund=" + refund +
                ", updateOrderUrl='" + updateOrderUrl + '\'' +
                ", isPromotion='" + isPromotion + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", payType=" + payType +
                '}';
    }
}
