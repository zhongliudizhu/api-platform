package com.winstar.carLifeMall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 类别
 **/
@Entity
@Data
@Table(name = "CBC_CAR_LIFE_CATEGORY")
public class Category {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 名称
     */
    @Column(length = 50)
    private String name;
    /**
     * 地址
     */
    @Column(length = 50)
    private String address;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 状态 0正常 1锁定
     */
    private Integer status;
}
