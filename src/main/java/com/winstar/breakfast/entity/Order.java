package com.winstar.breakfast.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2020/1/6
 */
@Data
@Entity
@Table(name = "breakfast_order")
public class Order {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;

    /**
     * 用户id
     */
    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String accountId;

    /**
     * 订单编号
     */
    @Column(columnDefinition = "varchar(50) comment '订单编号'")
    private String serialNumber;

    /**
     * 付款金额
     */
    @Column(columnDefinition = "double comment '付款金额'")
    private Double payPrice;

    /**
     * 商品价格
     */
    @Column(columnDefinition = "double comment '商品价格'")
    private Double price;

    /**
     * 优惠券id
     */
    @Column(columnDefinition = "varchar(50) comment '优惠券id'")
    private String couponId;

    /**
     * 商品编号
     */
    @Column(columnDefinition = "varchar(50) comment '商品编号'")
    private String itemId;

    /**
     * 手机号码
     */
    @Column(columnDefinition = "varchar(11) comment '手机号码'")
    private String phoneNo;

    /**
     * 银行流水号
     */
    @Column(columnDefinition = "varchar(50) comment '银行流水号'")
    private String bankSerialNo;

    /**
     * 支付状态
     */
    @Column(columnDefinition = "int comment '支付状态'")
    private Integer payStatus;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(100) comment '备注'")
    private String mark;

    /**
     * 下单时间
     */
    @Column(columnDefinition = "datetime comment '下单时间'")
    private Date createTime;

    /**
     * 付款时间
     */
    @Column(columnDefinition = "datetime comment '付款时间'")
    private Date payTime;

    /**
     * 是否关闭
     */
    @Column(columnDefinition = "varchar(1) comment '是否关闭'")
    private String isAvailable;

    /**
     * 支付方式
     */
    @Column(columnDefinition = "int comment '支付方式'")
    private Integer payType;

}
