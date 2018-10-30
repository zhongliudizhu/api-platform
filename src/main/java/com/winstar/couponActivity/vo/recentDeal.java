package com.winstar.couponActivity.vo;

/**
 * @author shoo on 2018/4/2 9:09.
 *         --
 */
//近期成交记录
public class recentDeal {
    private  int rId;//汽车交易ID
    private  String models;//成交车型
    private  String new_car_guidance_price;//新车指导价
    private  String transaction_price;//成交价
    private  String on_the_card_time;//上牌时间
    private  String kilometre;//公里
    private  String transaction_time;//交易日期
    private  String city;//城市
    private  String category;//类别

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }


    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public String getNew_car_guidance_price() {
        return new_car_guidance_price;
    }

    public void setNew_car_guidance_price(String new_car_guidance_price) {
        this.new_car_guidance_price = new_car_guidance_price;
    }

    public String getTransaction_price() {
        return transaction_price;
    }

    public void setTransaction_price(String transaction_price) {
        this.transaction_price = transaction_price;
    }

    public String getOn_the_card_time() {
        return on_the_card_time;
    }

    public void setOn_the_card_time(String on_the_card_time) {
        this.on_the_card_time = on_the_card_time;
    }

    public String getKilometre() {
        return kilometre;
    }

    public void setKilometre(String kilometre) {
        this.kilometre = kilometre;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
