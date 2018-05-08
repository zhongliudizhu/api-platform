package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.CarLifeOrders;
import com.winstar.order.entity.OilOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 订单
 **/
public interface CarLifeOrdersRepository extends JpaRepository<CarLifeOrders, String> {
    CarLifeOrders findByOrderSerial(String orderSerial);

    List<CarLifeOrders> findByAccountIdOrderByCreateTimeDesc(String accountId);

    @Query("select o from OilOrder o where o.isAvailable=?1 and o.status=?2 and o.createTime between ?3 and ?4")
    List<CarLifeOrders> findByIsAvailableAndStatusAndCreateTimeBetween(String isAvailable, Integer status, Date begin, Date end);
}
