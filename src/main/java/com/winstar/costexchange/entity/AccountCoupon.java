package com.winstar.costexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class AccountCoupon {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
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
    private String couponId;

    /**
     * 面值
     */
    @Column(columnDefinition = "double(10,2) comment '面值'")
    private Double amount;

    /**
     * 满多钱使用
     */
    @Column(columnDefinition = "double(10,2) comment '满多钱'")
    private Double fullMoney;

    /**
     * 标题
     */
    @Column(columnDefinition = "varchar(100) comment '标题'")
    private String title;

    /**
     * 副标题
     */
    @Column(columnDefinition = "varchar(100) comment '副标题'")
    private String subTitle;

    /**
     * 有效开始时间
     */
    @Column(columnDefinition = "datetime comment '有效开始时间'")
    private Date beginTime;

    /**
     * 有效结束时间
     */
    @Column(columnDefinition = "datetime comment '有效结束时间'")
    private Date endTime;

    /**
     * 适用类型   移动话费/建行/优驾行
     */
    @Column(columnDefinition = "varchar(20) comment '适用类型'")
    private String type;

    /**
     * 优惠券标记
     */
    @Column(columnDefinition = "varchar(100) comment '优惠券标记'")
    private String tags;

    /**
     * 状态
     */
    @Column(columnDefinition = "varchar(10) comment '状态：used/normal/expired'")
    private String state;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private Date createdAt;

}
