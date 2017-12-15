package com.winstar.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;

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
@Table(name = "CBC_USER_ONE_MONEY_COUPON_RECORD")
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;

}
