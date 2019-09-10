package com.winstar.communalCoupon.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
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
public class AccountCoupon implements Serializable {

    private static final long serialVersionUID = 1162873596142996403L;
    public static String TYPE_YJX = "yjx";
    public static String TYPE_CCB = "ccb";
    public static String TYPE_MOVE_COST = "moveCost";
    public static String TYPE_SHELL = "shell";

    /**
     * 唯一标识
     */
    @Id
//    @GenericGenerator(name = "idGenerator", strategy = "uuid")
//    @GeneratedValue(generator = "idGenerator")
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
    @Column(columnDefinition = "varchar(10) comment '状态：used/normal/expired/locked/sending'")
    @JSONField(name = "status")
    private String state;

    /**
     * 优惠券模板ID
     */
    @Column(columnDefinition = "varchar(50) comment '优惠券模板ID'")
    private String templateId;

    /**
     * 活动ID
     */
    @Column(columnDefinition = "varchar(50) comment '活动ID'")
    private String activityId;

    /**
     * 使用时间
     */
    @Column(columnDefinition = "datetime comment '使用时间'")
    private Date useDate;


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

    /**
     * 赠送时间
     */
    @Column(columnDefinition = "datetime comment '赠送时间'")
    private Date sendTime;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /**
     * 微信卡包id
     */
    @Column(columnDefinition = "varchar(50) comment '微信卡包id'")
    private String cardPackageId;

    /**
     * 微信卡包code
     */
    @Column(columnDefinition = "varchar(50) comment '微信卡包code'")
    private String cardPackageCode;
    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(20) COMMENT '手机号'")
    private String phone;


}
