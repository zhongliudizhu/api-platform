package com.winstar.carLifeMall.entity;

import com.winstar.user.utils.ServiceManager;
import groovy.lang.Category;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.loader.custom.Return;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 商品
 **/
@Entity
@Data
@Table(name = "CBC_CAR_LIFE_ITEM")
public class Item {
    @Transient
    public static final Integer STATUS_NORMAL = 0;

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;

    @Column(length = 50)
    private String categoryId;

    /**
     * 名称
     */
    @Column(length = 50)
    private String name;
    /**
     * 封面图
     */
    @Column(length = 100)
    private String coverImg;
    /**
     * 图文富文本
     */
    @Column(length = 1000)
    private String description;
    /**
     * 原价
     */
    private Double originalPrice;
    /*
    * 商品售价
    * */
    private Double salePrice;
    /**
     * 售卖状态 0正常 1 售罄
     */
    private Integer saleStatus;
    /**
     * 库存量
     */
    @Column(columnDefinition = "int default 0")
    private Integer storage_count;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 活动类别 0 常规 1 秒杀
     */
    @Column(length = 2, columnDefinition = "int default 0")
    private Integer activeType;

    /**
     * 商品状态 0 正常 1 下架
     */
    @Column(length = 2, columnDefinition = "int default 0")
    private Integer status;

    /**
     * 获取卖家信息
     *
     * @return
     */
    public List<Seller> getSeller() {
        List<ItemSellerRelation> list = ServiceManager.itemSellerRelationRepository.findByItemId(id);
        List<Seller> result = list.stream().map(t -> {
            return t.getSeller();
        }).collect(toList());
        return result;
    }
}
