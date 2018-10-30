package com.winstar.carLifeMall.entity;

import com.winstar.user.utils.ServiceManager;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 商品
 **/
@Entity
@Data
@Table(name = "CBC_CAR_LIFE_ITEM_SELLER_RELATION", indexes = {
        @Index(name = "idx_item_id", columnList = "itemId"),
        @Index(name = "idx_seller_id", columnList = "sellerId")
})
public class ItemSellerRelation {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 商品ID
     */
    @Column(length = 50)
    private String itemId;
    /**
     * 商家ID
     */
    @Column(length = 50)
    private String sellerId;
}
