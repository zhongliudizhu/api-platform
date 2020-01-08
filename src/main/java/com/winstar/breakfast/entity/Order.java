package com.winstar.breakfast.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2020/1/6
 */
@Data
@Entity
@Table(name = "breakfast_order")
@NoArgsConstructor
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
    @Column(columnDefinition = "double(16,2) comment '付款金额'")
    private Double payPrice;

    /**
     * 商品价格
     */
    @Column(columnDefinition = "double(16,2) comment '商品价格'")
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
     * 支付状态  0 未支付 1支付成功
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 付款时间
     */
    @Column(columnDefinition = "datetime comment '付款时间'")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 是否关闭  0 正常  1 关闭
     */
    @Column(columnDefinition = "varchar(1) comment '是否关闭'")
    private String isAvailable;

    /**
     * 支付方式  0：建行 1：微信 2：银联 3：支付宝
     */
    @Column(columnDefinition = "int comment '支付方式'")
    private Integer payType;

    /**
     * 优惠金额
     */
    @Column(columnDefinition = "double(16,2) comment '优惠金额'")
    private Double discountAmount;

    public Order(String accountId, String serialNumber, Double price, String itemId, String phoneNo, Integer payStatus, Date createTime, String isAvailable) {
        this.accountId = accountId;
        this.serialNumber = serialNumber;
        this.price = price;
        this.itemId = itemId;
        this.phoneNo = phoneNo;
        this.payStatus = payStatus;
        this.createTime = createTime;
        this.isAvailable = isAvailable;
    }

    @Transient
    private String shopName;
    @Transient
    private String shopImg;

}
