package com.winstar.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author shoo on 2017/7/6 16:50.
 *         -- 订单项 --
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cbc_oil_orderitems")
public class OilOrderItems {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /*
    * 商品名称
    * */
    @Column(length = 50)
    private String name;
    /*
    * 商品属性（油劵id）
    * */
    @Column(length = 50)
    private String property;
    /*
    *商品状态 1.未付款 2.已付款 3.已发货 4.已确认收货 5.已取消
    * */
    private Integer status;
    /*
    *单价
    * */
    private Double unitPrice;
    /*
    *数量
    * */
    private Integer amount;
    /*
    *优惠
    * */
    private Double favourable;
    /*
    *卖家id
    * */
    @Column(length = 50)
    private String sellerId;
    /*
    *订单序列号
    * */
    @Column(length = 50)
    private String orderSerialNo;
    /*
    * 商品类型 0 商品  1 赠品
    * */
    private Integer itemType;
    /*
    *商品快照
    * */
    @Column(length = 5000)
    private String goodsMsg;


}
