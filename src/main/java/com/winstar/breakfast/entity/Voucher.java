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
@Table(name = "breakfast_voucher")
public class Voucher {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 早餐券编号
     */
    @Column(columnDefinition = "varchar(100) comment '加油券编号'")
    private String code;

    /**
     * 早餐券金额
     */
    @Column(columnDefinition = "double comment '电子券金额'")
    private Double amt;

    /**
     * 券名称
     */
    @Column(columnDefinition = "varchar(30) comment '名称'")
    private String codeName;

    /**
     * 批次号
     */
    @Column(columnDefinition = "varchar(30) comment '批次号'")
    private String batchNumber;

    /**
     * 加油券状态 0：未售、1：已售
     */
    @Column(columnDefinition = "varchar(1) comment '分配状态'")
    private String saleState;

    /**
     * 创建日期
     */
    @Column(columnDefinition = "datetime comment '创建日期'")
    private Date createTime;

    /**
     * 成本价
     */
    @Column(columnDefinition = "double comment '成本价'")
    private Double costPrice;

    /**
     * 销售时间
     */
    @Column(columnDefinition = "datetime comment '分配时间'")
    private Date saleTime;

}
