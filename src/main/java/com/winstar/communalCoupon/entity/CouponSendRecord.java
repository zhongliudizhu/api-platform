package com.winstar.communalCoupon.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2017/8/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "communal_coupon_send_record")
@ToString
public class CouponSendRecord {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    private String id;

    /**
     * 赠送人
     */
    @Column(columnDefinition = "varchar(50) comment '赠送人'")
    private String sendAccountId;

    /**
     * 赠送人openid
     */
    @Column(columnDefinition = "varchar(50) comment '赠送人openid'")
    private String sendAccountOpenid;

    /**
     * 赠送时间
     */
    @Column(columnDefinition = "datetime comment '赠送时间'")
    private Date sendTime;

    /**
     * 领取人
     */
    @Column(columnDefinition = "varchar(50) comment '领取人'")
    private String receiveAccountId;

    /**
     * 领取人openId
     */
    @Column(columnDefinition = "varchar(50) comment '领取人openid'")
    private String receiverAccountOpenid;

    /**
     * 领取时间
     */
    @Column(columnDefinition = "datetime comment '领取时间'")
    private Date receiveTime;

    /**
     * 优惠券id
     */
    @Column(columnDefinition = "varchar(50) comment '优惠券id'")
    private String couponId;

    /**
     * 模板id
     */
    @Column(columnDefinition = "varchar(50) comment '模板id'")
    private String templateId;

    @Transient
    private AccountCoupon accountCoupon;

    /**
     * 赠送人姓名
     */

    @Transient
    private String sendName;
    /**
     * 领取人姓名
     */
    @Transient
    private String receiveName;

}
