package com.winstar.activityCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
/**
 * Created by qyc on 2019/08/14.
 * 建行活动绑定白名单
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cbc_common_white_list")
@Data
@Entity
public class CbcWhiteList {

    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;

    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(20) COMMENT '手机号'")
    private String phone;

    /**
     * 身份证号
     */
    @Column(columnDefinition = "varchar(20) COMMENT '身份证号'")
    private String cardNumber;

    /**
     * 有效时间
     */
    @Column(columnDefinition = "datetime COMMENT '有效时间'")
    private Date validTime;

    /**
     * 优惠券模板id
     */
    @Column(columnDefinition = "varchar(100) COMMENT '优惠券模板id'")
    private String couponId;

    /**
     * 活动code
     */
    @Column(columnDefinition = "varchar(50) COMMENT '活动code'")
    private String activityCode;

    /**
     * 活动名称
     */
    @Column(columnDefinition = "varchar(50) COMMENT '活动名称'")
    private String activityName;

    /**
     * 认证状态 0 未认证 1 已认证
     */
    @Column(columnDefinition = "varchar(2) COMMENT '认证状态'")
    private String state;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;

    /**
     * 领取的优惠券id
     */
    @Column(columnDefinition = "varchar(50) COMMENT '领取的优惠券id'")
    private String receiveCouponId;

}
