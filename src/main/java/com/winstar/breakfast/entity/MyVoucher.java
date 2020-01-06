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
@Table(name = "breakfast_my_voucher")
public class MyVoucher {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 早餐券编号
     */
    @Column(columnDefinition = "varchar(100) comment '券码编号'")
    private String code;

    /**
     * 金额
     */
    @Column(columnDefinition = "double comment '金额'")
    private Double amt;

    /**
     * 生效时间
     */
    @Column(columnDefinition = "datetime comment '生效时间'")
    private Date openDate;

    /**
     * 失效时间
     */
    @Column(columnDefinition = "datetime comment '失效时间'")
    private Date endDate;

    /**
     * 用户Id
     */
    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String accountId;

    /**
     * 订单编号
     */
    @Column(columnDefinition = "varchar(50) comment '订单编号'")
    private String orderId;

    /**
     * 商品Id
     */
    @Column(columnDefinition = "varchar(50) comment '商品编号'")
    private String shopId;

    /**
     * 创建日期
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private String createTime;

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
     * 使用的便利店Id
     */
    @Column(columnDefinition = "varchar(20) comment '便利店id'")
    private String storeId;

}
