package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.OrdersItems;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 订单商品
 **/
public interface OrdersItemsRepository extends JpaRepository<OrdersItems, String> {
    OrdersItems findByOrderSerial(String orderSerial);
}
