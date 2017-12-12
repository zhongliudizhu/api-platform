package com.winstar.coupon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Coupon
 * 创建者: zl
 * 创建时间: 2017-09-08 下午1:58
 * 功能描述: 优惠券
 */
@Entity
@Table(name = "marketing_coupon")
@JsonIgnoreProperties(value={"showStatus","couponTemplateId","useRule"})
public class Coupon {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 折扣率
     */
    private Double discountRate;
    /**
     * 最高折扣金额
     */
    private Double limitDiscountAmount;
    /**
     * 有效期起始时间
     */
    private Date validBeginAt;
    /**
     * 有效期结束时间
     */
    private Date validEndAt;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 显示状态 0 显示 1不显示
     */
    private Integer showStatus;
    /**
     * 状态 0 未使用 1 已使用 2 已失效
     */
    private Integer status;
    /**
     * 账户id
     */
    private String accountId;
    /**
     * 优惠券模板id
     */
    private String couponTemplateId;
    /**
     * 使用规则表达式
     */
    private String useRule;
    /**
     * 使用时间
     */
    private Date useDate;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getLimitDiscountAmount() {
        return limitDiscountAmount;
    }

    public void setLimitDiscountAmount(Double limitDiscountAmount) {
        this.limitDiscountAmount = limitDiscountAmount;
    }

    public Date getValidBeginAt() {
        return validBeginAt;
    }

    public void setValidBeginAt(Date validBeginAt) {
        this.validBeginAt = validBeginAt;
    }

    public Date getValidEndAt() {
        return validEndAt;
    }

    public void setValidEndAt(Date validEndAt) {
        this.validEndAt = validEndAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCouponTemplateId() {
        return couponTemplateId;
    }

    public void setCouponTemplateId(String couponTemplateId) {
        this.couponTemplateId = couponTemplateId;
    }

    public String getUseRule() {
        return useRule;
    }

    public void setUseRule(String useRule) {
        this.useRule = useRule;
    }

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
