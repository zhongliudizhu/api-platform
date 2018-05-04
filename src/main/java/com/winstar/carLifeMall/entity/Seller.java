package com.winstar.carLifeMall.entity;

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
@Table(name = "CBC_CAR_LIFE_SELLER")
public class Seller {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 商家名称
     */
    @Column(length = 50)
    private String name;
    /**
     * 服务地址
     */
    @Column(length = 50)
    private String address;

    private Date createTime;

    private Date updateTime;
    /**
     * 0正常 1已禁用
     */
    @Column(length = 2, columnDefinition = "int default 0")
    private Integer status;
}
