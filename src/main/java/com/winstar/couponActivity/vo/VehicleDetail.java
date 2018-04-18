package com.winstar.couponActivity.vo;

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

    private String isChina;//是否国产

    private String displacement;//排量

    private String power;//功率

    private Integer vehicleStatus;//机动车状态

    private Integer mortgageStatus;//抵押状态

    private String checkTime;//检验有效期

    private String strongInsuranceTime;//交强险有效期

    private String productionTime;//出厂日期

    private String registerTime;//初登日期

    private Integer isTransfer;//是否过户

    private String environmental;//环保情况

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

    public Integer getMortgageStatus() {
        return mortgageStatus;
    }

    public void setMortgageStatus(Integer mortgageStatus) {
        this.mortgageStatus = mortgageStatus;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getStrongInsuranceTime() {
        return strongInsuranceTime;
    }

    public void setStrongInsuranceTime(String strongInsuranceTime) {
        this.strongInsuranceTime = strongInsuranceTime;
    }

    public String getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(String productionTime) {
        this.productionTime = productionTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(Integer isTransfer) {
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
