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

/**
 * 名称： OilCoupon
 * 作者： sky
 * 日期： 2017private12private11 17:35
 * 描述： 油券
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oil_coupon")
public class OilCoupon {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 卡券所属公司
     */
    private String orgCode;
    /**
     * 加油券编号
     */
    private String pan;
    /**
     * 电子券金额
     */
    private Double panAmt;

    /**
     * 加油券名称
     */
    private String panName;
    /**
     * 加油券状态 0：未售、1：已售
     */
    private String oilState;

    /**
     * 创建日期
     */
    private String createTime;
    /**
     * 加油券描述
     */
    private String panDescription;

    /**
     * 分配时间
     */
    private String distributionTime;
    /**
     * 成本价
     */
    private Double costPrice;

}
