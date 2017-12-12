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
 * 名称： MyOilCouponDetail
 * 作者： sky
 * 日期： 2017private12private12 9:27
 * 描述： 我的油券详情
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_oil_coupon_detail")
public class MyOilCouponDetail {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String MyOilCouponId;
    /**
     * 加油券编号
     */
    private String pan;
    /**
     * 电子券金额
     */
    private Double panAmt;
    /**
     * 创建日期/分配时间
     */
    private Date createTime;
    /**
     * 使用状态 0：未使用、1：已使用
     */
    private String useState;
    /**
     * 使用时间
     */
    private Date useDate;
    /**
     * 生效日期
     */
    private Date openDate;
    /**
     * 失效日期
     */
    private Date endDate;
    /**
     * 使用的加油站Id
     */
    private String tId;

}
