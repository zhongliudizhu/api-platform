package com.winstar.vo;

/**
 * ${DESCRIPTION}
 *
 * @author Big BB
 * @create 2017-08-25 14:43
 **/
public class OilSetMealVo {
    private String orderId;//订单
    private String sendState;//赠送状态
    private String shopName;//套餐名字
    private int surplusNumber;//剩余张数
    private Double surplusPrice;//余额
    private int totalNumber;//总张数
    private Double totalPrice;//总价
    private String createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSendState() {
        return sendState;
    }

    public void setSendState(String sendState) {
        this.sendState = sendState;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getSurplusNumber() {
        return surplusNumber;
    }

    public void setSurplusNumber(int surplusNumber) {
        this.surplusNumber = surplusNumber;
    }

    public Double getSurplusPrice() {
        return surplusPrice;
    }

    public void setSurplusPrice(Double surplusPrice) {
        this.surplusPrice = surplusPrice;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
