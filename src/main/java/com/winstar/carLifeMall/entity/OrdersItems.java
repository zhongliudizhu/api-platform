package com.winstar.carLifeMall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 服务地址
     */
    @Column(length = 50)
    private String address;
    /**
     * 商品ID
     */
    @Column(length = 50)
    private String itemId;
    /**
     * 商品名称
     */
    @Column(length = 300)
    private String itemName;
    /**
     * 预约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reserveTime;
    /**
     * 预约手机号码
     */
    @Column(length = 20)
    private String reserveMobile;
    /**
     * 商户电话
     */
    @Column(length = 20)
    private String telephone;
}
