package com.winstar.order.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author shoo on 2017/7/6 16:50.
 *         -- 订单项 --
 */
@Entity
@Table(name = "oil_orderitems")
public class OilOrderItems {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /*
    * 商品名称
    * */
    @Column(length = 50)
    private String name;
    /*
    * 商品属性（油劵id）
    * */
    @Column(length = 50)
    private String property;
    /*
    *商品状态 1.未付款 2.已付款 3.已发货 4.已确认收货 5.已取消
    * */
    private Integer status;
    /*
    *单价
    * */
    private Double unitPrice;
    /*
    *数量
    * */
    private Integer amount;
    /*
    *优惠
    * */
    private Double favourable;
    /*
    *卖家id
    * */
    @Column(length = 50)
    private String sellerId;
    /*
    *订单序列号
    * */
    @Column(length = 50)
    private String orderSerialNo;
    /*
    * 商品类型 0 商品  1 赠品
    * */
    private Integer itemType;
    /*
    *商品快照
    * */
    @Column(length = 5000)
    private String goodsMsg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getFavourable() {
        return favourable;
    }

    public void setFavourable(Double favourable) {
        this.favourable = favourable;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getOrderSerialNo() {
        return orderSerialNo;
    }

    public void setOrderSerialNo(String orderSerialNo) {
        this.orderSerialNo = orderSerialNo;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getGoodsMsg() {
        return goodsMsg;
    }

    public void setGoodsMsg(String goodsMsg) {
        this.goodsMsg = goodsMsg;
    }


    @Override
    public String toString() {
        return "OilOrderItems{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", property='" + property + '\'' +
                ", status=" + status +
                ", unitPrice=" + unitPrice +
                ", amount=" + amount +
                ", favourable=" + favourable +
                ", sellerId='" + sellerId + '\'' +
                ", orderSerialNo='" + orderSerialNo + '\'' +
                ", itemType=" + itemType +
                ", goodsMsg='" + goodsMsg + '\'' +
                '}';
    }
}
