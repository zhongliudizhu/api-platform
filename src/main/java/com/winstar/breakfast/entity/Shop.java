package com.winstar.breakfast.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zl on 2020/1/6
 */
@Data
@Entity
@Table(name = "breakfast_shop")
public class Shop {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 商品名称
     */
    @Column(columnDefinition = "varchar(50) comment '商品名称'")
    private String shopName;

    /**
     * 商品图片
     */
    @Column(columnDefinition = "varchar(255) comment '商品图片'")
    private String shopImg;

    /**
     * 商品价格
     */
    @Column(columnDefinition = "double comment '商品价格'")
    private Double price;

    /**
     * 商品详情json  eg：[{unitPrice:'6', number:'5'},{unitPrice:'3', number:'6'}]
     */
    @Column(columnDefinition = "varchar(255) comment '商品详情json'")
    private String shopDetailJson;

}
