package com.winstar.carLifeMall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 名称： OrdersItems
 * 作者： dpw
 * 日期： 2018-05-03 16:37
 * 描述： 订单商品
 **/
@Entity
@Data
@Table(name = "CBC_CAR_LIFE_ORDERS_ITEMS", indexes = {
        @Index(name = "idx_order_serial", columnList = "orderSerial")
})
public class OrdersItems {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 订单编号
     */
    @Column(length = 50)
    private String orderSerial;
    /**
     * 商家id
     */
    @Column(length = 50)
    private String sellerId;
    /**
     * 商家名称
     */
    @Column(length = 50)
    private String sellerName;
    /**
     * 商品名称
     */
    @Column(length = 50)
    private String itemName;
    /**
     * 预约事项详情
     */
    @Column(length = 300)
    private String itemDetails;
    /**
     * 预约时间
     */
    private Date reserveTime;
    /**
     * 预约手机号码
     */
    @Column(length = 20)
    private String reserveMobile;
}
