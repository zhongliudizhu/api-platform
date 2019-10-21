package com.winstar.oilOutPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2019/10/9
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "out_platform_oil_coupon")
public class OutOilCoupon {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 加油券编号
     */
    @Column(columnDefinition = "varchar(100) comment '加油券编号'")
    private String pan;

    /**
     * 电子券金额
     */
    @Column(columnDefinition = "double comment '电子券金额'")
    private Double panAmt;

    /**
     * 加油券名称
     */
    @Column(columnDefinition = "varchar(30) comment '加油券名称'")
    private String panName;

    /**
     * 批次号
     */
    @Column(columnDefinition = "varchar(30) comment '批次号'")
    private String batchNumber;

    /**
     * 加油券状态 0：未售、1：已售
     */
    @Column(columnDefinition = "varchar(1) comment '加油券状态'")
    private String oilState;

    /**
     * 创建日期
     */
    @Column(columnDefinition = "datetime comment '创建日期'")
    private Date createTime;

    /**
     * 成本价
     */
    @Column(columnDefinition = "double comment '成本价'")
    private Double costPrice;

    /**
     * 销售时间
     */
    @Column(columnDefinition = "datetime comment '销售时间'")
    private Date saleTime;

    /**
     * 使用状态 0：未使用、1：已使用
     */
    @Column(columnDefinition = "varchar(1) comment '使用状态'")
    private String useState;

    /**
     * 使用时间
     */
    @Column(columnDefinition = "varchar(30) comment '使用时间'")
    private String useDate;

    /**
     * 使用的加油站Id
     */
    @Column(columnDefinition = "varchar(20) comment '油站id'")
    private String tId;

    /**
     * 订单号
     */
    @Column(columnDefinition = "varchar(50) comment '订单号'")
    private String orderId;

    /**
     * 输出平台油券Id
     */
    @Column(columnDefinition = "varchar(100) comment '输出平台油券Id'")
    private String outId;

    /**
     * 商户号
     */
    @Column(columnDefinition = "varchar(50) comment '商户号'")
    private String merchant;

}
