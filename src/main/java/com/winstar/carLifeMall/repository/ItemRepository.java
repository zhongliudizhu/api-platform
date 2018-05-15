package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.Item;
import com.winstar.user.utils.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 商品
 **/
public interface ItemRepository extends JpaRepository<Item, String> {
    List<Item> findByCategoryIdAndStatus(String category, Integer status);
    List<Item> findByStatus(Integer status);
}
