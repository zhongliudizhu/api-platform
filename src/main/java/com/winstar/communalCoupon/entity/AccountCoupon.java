package com.winstar.communalCoupon.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2019/5/21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "communal_account_coupon")
@ToString
public class AccountCoupon {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    @JSONField(deserialize = false)
    private String id;

    /**
     * 用户id
     */
    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String accountId;

    /**
     * 优惠券id
     */
    @Column(columnDefinition = "varchar(50) comment '优惠券id'")
    @JSONField(name = "id")
    private String couponId;

    /**
     * 面值
     */
    @Column(columnDefinition = "double(10,2) comment '面值'")
    @JSONField(name = "amount")
    private Double amount;

    /**
     * 满多钱使用
     */
    @Column(columnDefinition = "double(10,2) comment '满多钱'")
    @JSONField(name = "doorSkill")
    private Double fullMoney;

    /**
     * 标题
     */
    @Column(columnDefinition = "varchar(100) comment '标题'")
    @JSONField(name = "name")
    private String title;

    /**
     * 副标题
     */
    @Column(columnDefinition = "varchar(100) comment '副标题'")
    @JSONField(name = "subTitle")
    private String subTitle;

    /**
     * 有效开始时间
     */
    @Column(columnDefinition = "datetime comment '有效开始时间'")
    @JSONField(name = "startTime")
    private Date beginTime;

    /**
     * 有效结束时间
     */
    @Column(columnDefinition = "datetime comment '有效结束时间'")
    @JSONField(name = "endTime")
    private Date endTime;

    /**
     * 订单id
     */
    @Column(columnDefinition = "varchar(100) comment '订单id'")
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 适用类型   移动话费/建行/优驾行
     */
    @Column(columnDefinition = "varchar(20) comment '适用类型'")
    private String type;

    /**
     * 优惠券标记
     */
    @Column(columnDefinition = "varchar(100) comment '优惠券标记'")
    @JSONField(name = "suitItems")
    private String tags;

    /**
     * 状态
     */
    @Column(columnDefinition = "varchar(10) comment '状态：used/normal/expired/locked'")
    @JSONField(name = "status")
    private String state;

    /**
     * 显示状态（yes/no）
     */
    @Column(columnDefinition = "varchar(5) comment '显示状态'")
    @JSONField(name = "showStatus")
    private String showStatus;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    @JsonIgnore
    private Date createdAt;

}
