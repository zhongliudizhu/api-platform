package com.winstar.order.repository;

import com.winstar.order.entity.FlowOrder;
import com.winstar.order.entity.OilOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/7/7 13:52.
 *         --  --
 */
public interface FlowOrderRepository extends JpaRepository<FlowOrder,String> {

    @Query("select f from FlowOrder f where f.accountId=?1 and f.itemId=?2 and f.isAvailable = '0' and f.createTime between ?3 and ?4 ")
    List<FlowOrder> findByAccountIdAndItemId(String accountId, String itemId, Date begin, Date end);

    FlowOrder findBySerialNumber(String serialNumber);

    List<FlowOrder> findByAccountId(String accountId);

    @Query("select f from FlowOrder f where f.isAvailable=?1 and f.status=?2 and f.createTime between ?3 and ?4")
    List<FlowOrder> findByIsAvailableAndStatusAndCreateTimeBetween(String isAvailable, Integer status,Date begin, Date end);

}
