package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.ItemSellerRelation;
import com.winstar.carLifeMall.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 类别卖家关系表
 **/
public interface ItemSellerRelationRepository extends JpaRepository<ItemSellerRelation, String> {
    List<ItemSellerRelation> findByItemId(String itemId);

    long countByItemIdAndSellerId(String itemId, String sellerId);
}
