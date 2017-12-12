package com.winstar.invoice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shoo on 2017/10/23 14:01.
 * @Describe： 发票
 */
@Entity
@Table(name = "winstar_invoice")
public class Invoice {
    /*
    *主键
    * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /*
    * 类型 0 个人  1 公司
    * */
    private Integer type;
    /*
    *姓名
    * */
    @Column(length = 50)
    private String personName;
    /*
    * 身份证号
    * */
    @Column(length = 50)
    private String identNumber;
    /*
    * 手机号
    * */
    @Column(length = 50)
    private String phoneNo;
    /*
    * 电子邮箱
    * */
    @Column(length = 50)
    private String email;
    /*
    * 公司名称
    * */
    @Column(length = 50)
    private String companyName;
    /*
    * 纳税人识别号
    * */
    @Column(length = 50)
    private String registrationNo;
    /*
    * 公司地址
    * */
    @Column(length = 200)
    private String companyAddress;
    /*
    * 电话
    * */
    @Column(length = 50)
    private String telephone;
    /*
    * 开户银行
    * */
    @Column(length = 50)
    private String depositBank;
    /*
    * 银行账号
    * */
    @Column(length = 50)
    private String bankAccount;
    /*
    *  0 待开票  1 开票完成  2 信息变更 3 申请重开 4 已拒绝 5 已重开
    * */
    private Integer status;
    /*
    * 创建时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /*
    * 开票时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date invoiceTime;

    /*
    * 修改时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /*
    * 订单序列号
    * */
    @Column(length = 50)
    private String orderSerialNo;
    /*
    * 订单金额
    * */
    private Double payPrice;

    /*
    * 油券总值
    * */
    private Double oilTotalValue;
    /*
    * 是否删除  0 否  1 已删除
    * */
    @Column(length = 10)
    private String isDel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIdentNumber() {
        return identNumber;
    }

    public void setIdentNumber(String identNumber) {
        this.identNumber = identNumber;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(Date invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderSerialNo() {
        return orderSerialNo;
    }

    public void setOrderSerialNo(String orderSerialNo) {
        this.orderSerialNo = orderSerialNo;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getOilTotalValue() {
        return oilTotalValue;
    }

    public void setOilTotalValue(Double oilTotalValue) {
        this.oilTotalValue = oilTotalValue;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
}
