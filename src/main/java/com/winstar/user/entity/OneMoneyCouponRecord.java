package com.winstar.user.entity;

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
 * 名称： OneMoneyCouponRecord
 * 作者： sky
 * 日期： 2017-12-14 17:13
 * 描述： 1元优惠券购买实体
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_one_money_coupon_record")
public class OneMoneyCouponRecord {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 用户id
     */
    private String accountId;
    /**
     * 0 未购买过 1 已购买过
     */
    private Integer status;
    /**
     * 违法订单号
     */
    private String orderId;
}
