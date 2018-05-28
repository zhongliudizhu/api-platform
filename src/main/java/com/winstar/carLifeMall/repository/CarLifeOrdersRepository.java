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

    @Query("select o from CarLifeOrders o where o.isAvailable=?1 and o.status=?2 and o.createTime between ?3 and ?4")
    List<CarLifeOrders> findByIsAvailableAndStatusAndCreateTimeBetween(Integer isAvailable, Integer status, Date begin, Date end);

    @Query(value = "SELECT count(1) FROM cbc_car_life_orders t inner join cbc_car_life_orders_items t2 on t.order_serial = t2.order_serial  where t.is_available=?1 and t2.item_id = ?2", nativeQuery = true)
    long countByIsAvailableAndItemId(Integer isAvailable, String itemId);

    @Query(value = "SELECT\n" +
            "\tcount(1)\n" +
            "FROM\n" +
            "\tcbc_car_life_orders_items  t\n" +
            "INNER JOIN cbc_car_life_orders  t2 ON t.order_serial = t2.order_serial \n" +
            "inner join cbc_car_life_item t3 on t3.id = t.item_id\n" +
            "WHERE\n" +
            "\tt2.is_available =?1 \n" +
            "AND t3.active_type = ?2\n" +
            "AND t2.account_id =?3  and t2.create_time like concat('%',  SUBSTR(t2.create_time FROM 1 FOR 10),'%')  ", nativeQuery = true)
    long countByIsAvailableAndActivityTypeAndAccountId(Integer isAvailable, Integer activity_type, String accountId);
}
