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
@Table(name = "my_oil_coupon")
public class MyOilCoupon {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 用户Id
     */
    private String accountId;
    /**
     * 商品Id
     */
    private String goodId;

}
