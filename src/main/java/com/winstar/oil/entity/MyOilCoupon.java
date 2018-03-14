package com.winstar.oil.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 名称： MyOilCoupon
 * 作者： sky
 * 日期： 2017private12private12 9:25
 * 描述： 我的油券
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_my_oil_coupon")
public class MyOilCoupon {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 加油券编号
     */
    private String pan;

    /**
     * 电子券金额
     */
    private Double panAmt;

    /**
     * 生效日期
     */
    private String openDate;

    /**
     * 失效日期
     */
    private String endDate;

    /**
     * 用户Id
     */
    private String accountId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 商品Id
     */
    private String shopId;

    /**
     * 分期套餐原价
     */
    private Double shopPrice;

    /**
     * 创建日期
     */
    private String createTime;

    /**
     * 使用状态 0：未使用、1：已使用
     */
    private String useState;

    /**
     * 使用时间
     */
    private String useDate;

    /**
     * 使用的加油站Id
     */
    @Column(length = 30)
    private String tId;

    /**
     * 赠送状态 0：正常 1：已赠送待领取
     */
    @Column(length = 1)
    private String sendState;

    /**
     * 实际售价 （开发票用）
     */
    @Transient
    private Double payPrice;

}
