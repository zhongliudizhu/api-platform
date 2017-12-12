package com.winstar.coupon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Coupon
 * 创建者: zl
 * 创建时间: 2017-09-08 下午1:58
 * 功能描述: 优惠券
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_coupon")
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


}
