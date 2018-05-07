package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 订单
 **/
public interface OrdersRepository extends JpaRepository<Orders, String> {
    Orders findByOrderSerial(String orderSerial);

    List<Orders> findByAccountIdOrderByCreateTimeDesc(String accountId);
}
