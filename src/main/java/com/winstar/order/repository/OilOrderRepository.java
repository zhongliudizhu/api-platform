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
    OilOrder findBySerialNo(String serialNo);

}
