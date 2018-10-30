package com.winstar.couponActivity.vo;

import java.util.Date;

/**
 * VehicleDetail
 *
 * @author: Big BB
 * @create 2018-04-18 10:47
 * @DESCRIPTION:
 **/
public class VehicleDetail {

    private String vehicleType;//车辆类型

    private String brand;//中文品牌

    private String isChina;//是否国产   A：国产

    private String displacement;//排量

    private String power;//功率

    private Integer vehicleStatus;//机动车状态

    private String mortgageStatus;//抵押状态

    private Date checkTime;//检验有效期   2019/8/31 星期六

    private Date strongInsuranceTime;//交强险有效期

    private String productionTime;//出厂日期

    private Date registerTime;//初登日期

    private String isTransfer;//是否过户  0：未过户  1：过户

    private String environmental;//环保情况   GB18352.5-2013(国五阶段)/GB18352.5-2013国Ⅴ

    private String location;//车辆所在地

    private Integer illegalNumber;//累计违法数量

    private Integer integral;//累计罚款积分

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIsChina() {
        return isChina;
    }

    public void setIsChina(String isChina) {
        this.isChina = isChina;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Integer getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(Integer vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getMortgageStatus() {
        return mortgageStatus;
    }

    public void setMortgageStatus(String mortgageStatus) {
        this.mortgageStatus = mortgageStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getStrongInsuranceTime() {
        return strongInsuranceTime;
    }

    public void setStrongInsuranceTime(Date strongInsuranceTime) {
        this.strongInsuranceTime = strongInsuranceTime;
    }

    public String getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(String productionTime) {
        this.productionTime = productionTime;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getEnvironmental() {
        return environmental;
    }

    public void setEnvironmental(String environmental) {
        this.environmental = environmental;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getIllegalNumber() {
        return illegalNumber;
    }

    public void setIllegalNumber(Integer illegalNumber) {
        this.illegalNumber = illegalNumber;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
