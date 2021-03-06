package com.winstar.activityCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author UU
 * @Classname Activity
 * @Description TODO
 * @Date 2019/7/2 11:29
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cbc_communal_activity")
public class CommunalActivity {

    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;

    /**
     * 名称
     */
    @Column(columnDefinition = "varchar(100) COMMENT '名称'")
    private String name;

    /**
     * 发放类型 1：长期;  2:限时限量
     */
    @Column(columnDefinition = "varchar(100) COMMENT '发放类型 1：长期;  2:限时限量'")
    private String type;

    /**
     * 发放总量
     */
    @Column(columnDefinition = "int(100) COMMENT '发放总量'")
    private Integer totalNum;

    /**
     * 每人限领张数
     */
    @Column(columnDefinition = "int(10) COMMENT '每人限领张数'")
    private Integer perLimitNum;

    /**
     * 优惠券模板ID
     */
    @Column(columnDefinition = "varchar(100) COMMENT '优惠券模板ID'")
    private String couponTemplateId;

    /**
     * 展示日期
     */
    @Column(columnDefinition = "datetime COMMENT '展示日期'")
    private Date showDate;

    /**
     * 领取开始日期
     */
    @Column(columnDefinition = "datetime COMMENT '领取开始日期'")
    private Date startDate;

    /**
     * 领取结束日期
     */
    @Column(columnDefinition = "datetime COMMENT '领取结束日期'")
    private Date endDate;

    /**
     * 活动状态(yes上架，no下架)
     */
    @Column(columnDefinition = "varchar(10) COMMENT '活动状态(yes上架，no下架)'")
    private String status;

    /**
     * 是否删除(yes已删除，no未删除)
     */
    @Column(columnDefinition = "varchar(10) COMMENT '是否删除(yes已删除，no未删除)'")
    private String del;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    private Date createdAt;

    /**
     * 是否首页展示 (yes/no)
     */
    @Column(columnDefinition = "varchar(10) COMMENT '是否首页展示'")
    private String showHome;

    /**
     * 是否只有新用户才能领取 (yes/no)
     */
    @Column(columnDefinition = "varchar(10) COMMENT '是否只有新用户才能领取'")
    private String onlyNew;

    /**
     * 展示处(center/ccb/?/?)
     */
    @Column(columnDefinition = "varchar(10) COMMENT ' 展示处'")
    private String target;

}
