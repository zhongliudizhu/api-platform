package com.winstar.order.repository;

import com.winstar.order.entity.OilOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/7/7 13:52.
 *         --  --
 */
public interface OilOrderRepository extends JpaRepository<OilOrder,String> {
    /**
     * 根据订单序列号查询订单
     * @param serialNo 订单序列号
     * @return result
     */
    OilOrder findBySerialNumber(String serialNo);

    /*
    * 查询用户油券订单
    * */
    List<OilOrder> findByAccountId(String accountId);

    @Query("select o from OilOrder o where o.isAvailable=?1 and o.status=?2 and o.createTime between ?3 and ?4")
    List<OilOrder> findByIsAvailableAndStatusAndCreateTimeBetween(String isAvailable, Integer status,Date begin, Date end);

}
