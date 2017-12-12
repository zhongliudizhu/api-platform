package com.winstar.order.vo;

import java.util.List;

/**
 * @author shoo on 2017/7/14 17:37.
 *         --  --
 */
public class GoodsVo {
    private String id;
    private Integer periods;
    private Double salePrice;
    private Double shopPrice;
    private Integer stock;//库存
    private List<GiftsVo> gifts;
    private List<TagsVo> tags;
    private String expdate;
    private String sendRate;
    private String sendType;
    private String type;
    private String shopState;//状态：上架还是下架
    private String isPromotion;//0 正常  1  促销 2 秒杀

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Double shopPrice) {
        this.shopPrice = shopPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<GiftsVo> getGifts() {
        return gifts;
    }

    public void setGifts(List<GiftsVo> gifts) {
        this.gifts = gifts;
    }

    public List<TagsVo> getTags() {
        return tags;
    }

    public void setTags(List<TagsVo> tags) {
        this.tags = tags;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getSendRate() {
        return sendRate;
    }

    public void setSendRate(String sendRate) {
        this.sendRate = sendRate;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopState() {
        return shopState;
    }

    public void setShopState(String shopState) {
        this.shopState = shopState;
    }

    public String getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(String isPromotion) {
        this.isPromotion = isPromotion;
    }

    @Override
    public String toString() {
        return "GoodsVo{" +
                "id='" + id + '\'' +
                ", periods=" + periods +
                ", salePrice=" + salePrice +
                ", shopPrice=" + shopPrice +
                ", stock=" + stock +
                ", gifts=" + gifts +
                ", tags=" + tags +
                ", expdate='" + expdate + '\'' +
                ", sendRate='" + sendRate + '\'' +
                ", sendType='" + sendType + '\'' +
                ", type='" + type + '\'' +
                ", shopState='" + shopState + '\'' +
                ", isPromotion='" + isPromotion + '\'' +
                '}';
    }
}
