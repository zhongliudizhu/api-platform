package com.winstar.order.vo;

import org.springframework.format.annotation.NumberFormat;

/**
 * @author shoo on 2017/7/14 17:04.
 *         --  --
 */
public class GiftsVo {
    private String id;

    private String shopName;

    @NumberFormat
    private Integer shopNumber;

    private Integer shopPrice;

    private String masterShopId;

    private String isGifts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(Integer shopNumber) {
        this.shopNumber = shopNumber;
    }

    public Integer getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Integer shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getMasterShopId() {
        return masterShopId;
    }

    public void setMasterShopId(String masterShopId) {
        this.masterShopId = masterShopId;
    }

    public String getIsGifts() {
        return isGifts;
    }

    public void setIsGifts(String isGifts) {
        this.isGifts = isGifts;
    }

    @Override
    public String toString() {
        return "GiftsVo{" +
                "id='" + id + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopNumber='" + shopNumber + '\'' +
                ", shopPrice=" + shopPrice +
                ", masterShopId='" + masterShopId + '\'' +
                ", isGifts='" + isGifts + '\'' +
                '}';
    }
}
