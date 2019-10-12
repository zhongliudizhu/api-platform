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
@Table(name = "out_platform_oil_coupon_log")
public class OutOilCouponLog {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 油券id
     */
    @Column(columnDefinition = "varchar(50) comment '加油券id'")
    private String oilId;

    /**
     * 订单编号
     */
    @Column(columnDefinition = "varchar(50) comment '订单编码'")
    private String orderId;

    /**
     * 数量
     */
    @Column(columnDefinition = "varchar(5) comment '数量'")
    private String number;

    /**
     * 日志类型
     * active/sale
     */
    @Column(columnDefinition = "varchar(10) comment '日志类型：active/sale/verification'")
    private String type;

    /**
     * 状态码：success/fail
     */
    @Column(columnDefinition = "varchar(10) comment '状态码：success/fail'")
    private String code;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建日期'")
    private Date createTime;

}
