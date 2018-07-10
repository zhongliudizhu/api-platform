package com.winstar.order.repository;

import com.winstar.order.entity.OilOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/7/7 13:52.
 * --  --
 */
public interface OilOrderRepository extends JpaRepository<OilOrder, String> {
    /**
     * 根据订单序列号查询订单
     *
     * @param serialNo 订单序列号
     * @return result
     */
    OilOrder findBySerialNumber(String serialNo);

    /*
    * 查询用户油券订单
    * */
    List<OilOrder> findByAccountId(String accountId);

    @Query("select o from OilOrder o where o.isAvailable=?1 and o.status=?2 and o.createTime between ?3 and ?4")
    List<OilOrder> findByIsAvailableAndStatusAndCreateTimeBetween(String isAvailable, Integer status, Date begin, Date end);

    /*
    * 每天某商品购买的总数量
    * */
    @Query("select o from OilOrder o where o.isAvailable=?1 and o.itemId=?2  and o.createTime between ?3 and ?4")
    List<OilOrder> findByIsAvailableAndItemIdAndCreateTime(String isAvailable, String itemId, Date begin, Date end);

    /*
    * 用户一段时间的订单
    * */
    @Query("select o from OilOrder o where o.accountId=?1 and o.itemId=?2  and o.isAvailable = '0' and o.createTime between ?3 and ?4")
    List<OilOrder> findByAccountIdAndAndItemId(String accountId, String itemId, Date begin, Date end);

    /*
    * 用户一段时间某活动的订单
    * */
    @Query("select o from OilOrder o where o.isAvailable='0' and o.accountId=?1 and o.activityId=?2 and o.createTime between ?3 and ?4")
    List<OilOrder> findByAccountIdAndActivityId(String accountId, String activityId, Date begin, Date end);

    /*
   * 一段时间某活动的订单
   * */
    @Query("select o from OilOrder o where o.isAvailable='0' and o.activityId=?1 and o.createTime between ?2 and ?3")
    List<OilOrder> findByActivityId(String activityId, Date begin, Date end);

    @Query("select o from OilOrder o where o.isAvailable='0'and o.itemTotalValue=?1 and o.activityId =?2")
    List<OilOrder> findByItemTotalValue(Double price, String activityId);

    @Query("select o from OilOrder o where o.isAvailable='0'  and o.itemId=?1 and o.createTime between ?2 and ?3")
    List<OilOrder> findByItemId(String itemId, Date begin, Date end);

    @Query("select o from OilOrder o where o.isAvailable='0'  and o.couponId=?1")
    List<OilOrder> findByCouponId(String findByCouponId);

    @Query(value = "select count(0) from cbc_oil_order o where o.is_available='0' and o.activity_id=?1 and o.create_time  like concat('%',  SUBSTR(SYSDATE()  FROM 1 FOR 10),'%')   and o.account_id=?2", nativeQuery = true)
    long countValidOrderByActivityIdAndCreateTimeAndAccountId(String activityId, String accountId);

    long countByStatusAndAccountIdAndIsAvailable(Integer status, String accountId, String isAvailable);
}
