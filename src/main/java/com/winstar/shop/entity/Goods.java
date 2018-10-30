package com.winstar.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 名称： Goods
 * 作者： sky
 * 日期： 2017-12-12 9:22
 * 描述： 商品实体
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_goods")
public class Goods {

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
    private String name;
    /**
     * 商品图片
     */
    private String image;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品售价
     */
    private Double saledPrice;
    /**
     * 商品描述
     */
    private String descriptions;
    /**
     * 商品状态 0 已下架 1 已上架
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 商品详情[面值：数量]
     */
    private String couponDetail;
    /**
     * 赠送优惠券
     */
    private String couponTempletId;
    /**
     * 折扣
     */
    private Double disCount;

    /**
     * 类型 所属活动 1 折中折 2 赠券 3 0.01元活动
     */
    private Integer type;

    /**
     * 售罄状态  0 未售  1 已售罄  （置灰）
     */
    private Integer isSale;

    private Integer limitAmount;

}
