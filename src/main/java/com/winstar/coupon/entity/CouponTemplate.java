package com.winstar.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * CouponTemplate
 * <p>
 * 创建者: orange
 * 创建时间: 2017-09-04 下午3:31
 * 功能描述: 优惠券模板
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon_template")
public class CouponTemplate {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 名称
     */
    private String name;
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
     * 有效期起多少天
     */
    private Integer days;
    /**
     * 描述
     */
    private String description;
    /**
     * 备注
     */
    private String remark;

    /**
     *
     */
    private String rules;
}
