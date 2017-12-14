package com.winstar.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shoo on 2017/7/6 16:13.
 *  -- 订单 --
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "cbc_oil_order")
public class OilOrder {
    /*
    *主键
    * */
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
    /*
     * 订单来源 1 微信 2 安卓  3 IOS 0 littleCode
     * */
    private Integer orderFrom;
    /*
    * 订单序列号
    * */
    @Column(length = 50)
    private String serialNo;
    /*
    * 付款总金额
    * */
    private Double payPrice;
    /*
    * 商品售价
    * */
    private Double salePrice;
    /*
    * 商品总值
    * */
    private Double itemTotalValue;
    /*
    * 优惠金额
    * */
    private Double discountAmount;
    /*
    * 优惠券id
    * */
    @Column(length = 50)
    private String couponId;
    /*
    * 商品id
    * */
    @Column(length = 50)
    private String itemId;

    /*
    * 活动id
    * */
    @Column(length = 50)
    private String activityId;
    /*
    * 油券明细
    * */
    @Column(length = 100)
    private String oilDetail;
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
    private Integer sendStatus;
    /*
    *备注
    * */
    @Column(length = 100)
    private String mark;
    /*
    *下单时间
    * */
    private Date createTime;
    /*
    *付款时间
    * */
    private Date payTime;
    /*
    *订单完成时间
    * */
    private Date finishTime;
    /*
    * 最后修改时间
    * */
    private Date updateTime;
    /*
    * 退款  0 初始化  1  退款成功  2 退款失败 3 退款中 4 部分退款
    * */
    private Integer refund;
    /*
    * 修改订单地址
    * */
    private String updateOrderUrl;
    /*
    *  0 正常  1  促销  2 秒杀 3 活动
    * */
    @Column(length = 2)
    private String isPromotion;
    /*
    * 是否可用  0 正常  1 关闭
    * */
    @Column(length = 50)
    private String isAvailable;
    /**
     * 赠送优惠券
     */
    private String couponTempletId;

    /*
    * 支付方式 0：建行 1：银联 2：微信 3：支付宝
    * */
    private Integer payType;

    /* 订单来源 账户id 订单序列号 手机号 状态 支付状态 下单时间 退款 商品id 活动类型 秒杀id 活动id*/
    public OilOrder(Integer orderFrom, String accountId, String serialNo, String phoneNo, Integer status, Integer payStatus, Date createTime, Integer refund, String itemId, String isPromotion, String activityId) {
        this.accountId = accountId;
        this.serialNo = serialNo;
        this.phoneNo = phoneNo;
        this.status = status;
        this.payStatus = payStatus;
        this.createTime = createTime;
        this.refund = refund;
        this.itemId = itemId;
        this.isPromotion = isPromotion;
        this.orderFrom = orderFrom;
        this.activityId = activityId;
        this.discountAmount = 0.0;
        this.isAvailable = "0";
    }

}
