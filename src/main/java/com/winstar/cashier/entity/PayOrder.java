package com.winstar.cashier.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 支付订单
 * @author zl
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CBC_PAY_ORDER")
public class PayOrder{

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;
    /**
     * 订单号
     */
    @Column(name = "ORDER_NUMBER", length = 50)
    private String orderNumber;

    /**
     * 支付订单编号
     */
    @Column(name = "PAY_ORDER_NUMBER", length = 50, unique = true)
    private String payOrderNumber;
    /**
     * 订单金额金额  单位：分
     */
    @Column(name = "ORDER_AMOUNT", length = 12)
    private String orderAmount;
    /**
     * 支付金额金额  单位：分
     */
    @Column(name = "PAY_AMOUNT", length = 12)
    private String payAmount;
    /**
     * 状态
     * 0—订单未支付
     * 1—订单支付成功
     * 2—订单支付失败
     * 注意：未支付订单不返回
     */
    @Column(length = 1)
    private String state;
    /**
     * 创建时间
     */
    @Column(name = "CREATED_AT")
    private Date createdAt;
    /**
     * 修改时间
     */
    @Column(name = "UPDATED_AT")
    private Date updaedAt;
    /**
     * 支付时间
     */
    @Column(name = "ORDER_TIME")
    private Date orderTime;

    /**
     * 币种
     */
    @Column(name = "ORDER_CURRENCY", length = 3)
    private String orderCurrency;

    /**
     * 支付接口类型 个人支付：B2C、企业支付：B2B
     */
    @Column(name = "PAY_TYPE")
    private String payType;

    /**
     * 客户端ip
     */
    @Column(name = "CUSTOMER_IP")
    private String customerIp;

    /**
     * 银行编码
     */
    @Column(name = "DEFAULT_BANK_NUMBER")
    private String defaultBankNumber;
    /**
     * 交易流水号
     */
    @Column(length = 32)
    private String qid;
    /**
     * 交易类型
     */
    @Column(name = "CONSUMER_TYPE", length = 255)
    private String consumerType;
    /**
     * 来源 1 微信  2 andriond  3 ios
     */
    @Column(name = "source", length = 1)
    private String source;

    /**
     * 回调地址参数
     */
    @Column(name = "CALL_URL", length = 255)
    private String callUrl;

    /**
     * 用户Id, 移动端使用、适配1.0
     */
    private String userId;

    /**
     * 订单状态 0：未裁未缴 1：已裁未缴
     */
    private Integer orderState;

    /**
     * 支付方式 0：建行 1：银联 2：微信 3：支付宝
     */
    private Integer payWay;

    /**
     * 对账状态
     */
    @Column(columnDefinition = "varchar(1) default '0'")
    private String checkState;

    /**
     * 订单拥有者，0：对公（违法）、1：对私（油卡）、2：对私（保险）
     */
    @Column(columnDefinition = "varchar(1) default '0'")
    private String orderOwner;

    /**
     * 支付子方式
     */
    @Column(length = 3)
    private String subPayWay;

    /**
     * 微信代金券id
     */
    @Column(length = 50)
    private String couponId;

    /**
     * 微信代金券优惠金额
     */
    @Column(length = 10)
    private String couponFee;

    /**
     * 微信支付方式：零钱/银行卡
     */
    @Column(length = 20)
    private String bankType;

}
