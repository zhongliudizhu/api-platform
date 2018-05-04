package com.winstar.carLifeMall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 商家
 **/
@Entity
@Data
@Table(name = "CBC_CAR_LIFE_ORDERS", indexes = {
        @Index(name = "idx_account_id", columnList = "accountId"),
        @Index(name = "idx_is_available", columnList = "isAvailable"),
        @Index(name = "idx_pay_status", columnList = "payStatus"),
        @Index(name = "idx_order_serial", columnList = "orderSerial")
})
public class Orders {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /*
      * 账户id
      * */
    @Column(length = 50)
    private String accountId;
    @Column(length = 50)
    private String orderSerial;
    /*
     * 订单来源 1 微信 2 安卓  3 IOS 0 littleCode
     * */
    @Column(length = 2)
    private Integer orderFrom;
    /*
    * 商品售价
    * */
    private Double salePrice;
    /*
    * 付款总金额
    * */
    private Double payPrice;
    /*
    * 优惠金额
    * */
    private Double discountAmount;

    /*
    *手机号
    * */
    @Column(length = 50)
    private String phoneNo;
    /*
    *银行缴费流水号
    * */
    @Column(length = 50)
    private String bankSerialNo;
    /*
    *备注
    * */
    @Column(length = 100)
    private String mark;
    /*
    *下单时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /*
    *付款时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    /*
    *订单完成时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;
    /*
    * 最后修改时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /*
    * 退款  0 初始化  1  退款成功  2 退款失败 3 退款中 4 部分退款
    * */
    @Column(length = 2, columnDefinition = "int default 0")
    private Integer refund;
    /*
    *  0 正常  1 秒杀
    * */
    @Column(length = 2, columnDefinition = "int default 0")
    private Integer isPromotion;
    /*
    * 是否可用  0 正常  1 关闭
    * */
    @Column(length = 2, columnDefinition = "int default 0")
    private Integer isAvailable;
    /*
    * 支付方式 0：建行 1：银联 2：微信 3：支付宝
    * */
    private Integer payType;

    /*
    *状态 1 订单生成(未支付) 2 待发货(支付成功) 3 已发货 4 已确认收货
    * */
    private Integer status;
    /*
    *支付状态 0 未支付 1支付成功
    * */
    private Integer payStatus;
    /*
    *发货状态  1待发货 2发货中 3发货成功 4发货失败
    * */
    @Column(length = 1, columnDefinition = "int default 1")
    private Integer sendStatus;
}
