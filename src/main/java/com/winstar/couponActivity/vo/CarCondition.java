package com.winstar.couponActivity.vo;

/**
 * @author shoo on 2018/4/4 13:43.
 *         --
 */
//获取车况说明
public class CarCondition {

    private  String cId;//获取车况说明ID
    private String surface; //外观状况
    private String interior; //车辆内饰
    private String work_state;//工况状况

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getWork_state() {
        return work_state;
    }

    public void setWork_state(String work_state) {
        this.work_state = work_state;
    }
}
