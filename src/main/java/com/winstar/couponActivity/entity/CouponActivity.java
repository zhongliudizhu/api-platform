//package com.winstar.couponActivity.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.Date;
//
///**
// * CouponActivity
// *
// * @author Big BB
// * @create 2017-12-19 11:23
// * 优惠券卷活动实体
// **/
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "cbc_coupon_activity")
//public class CouponActivity {
//    /**
//     * 唯一标识
//     */
//    @Id
//    @GenericGenerator(name = "idGenerator", strategy = "uuid")
//    @GeneratedValue(generator = "idGenerator")
//    private String id;
//    /**
//     * 活动名称
//     */
//    private String name;
//    /**
//     * 金额
//     */
//    private Double amount;
//
//    /**
//     * 发卷规则
//     */
//    private String sendRule;
//    /**
//     * 有效期起始时间
//     */
//    private Date validBeginAt;
//    /**
//     * 有效期结束时间
//     */
//    private Date validEndAt;
//    /**
//     * 创建时间
//     */
//    private Date createdAt;
//    /**
//     * 显示状态 0 显示 1不显示
//     */
//    private Integer showStatus;
//
//    /**
//     * 描述
//     */
//    private String description;
//    /**
//     * 备注
//     */
//    private String remark;
//
//    /**
//     * 满多少全用 100
//     */
//    private Double useRule;
//}
