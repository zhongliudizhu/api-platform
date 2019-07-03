package com.winstar.oil.repository;

import com.winstar.oil.entity.MyOilCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 名称： MyOilCouponRepository
 * 作者： sky
 * 日期： 2017-12-12 9:37
 * 描述：
 **/
public interface MyOilCouponRepository extends JpaSpecificationExecutor<MyOilCoupon>,JpaRepository<MyOilCoupon,String> {

    @Query(value =
            "SELECT count(*) as '总数'," +
                    "sum(CASE WHEN use_state = '0' THEN 1 ELSE 0 END) as '剩余劵数'," +
                    "sum(CASE WHEN use_state = '0' THEN t.pan_amt ELSE 0 END) as '剩余金额'," +
                    "t.shop_price '总价',t.order_id as '订单号'," +
                    "t.send_state as '赠送状态'," +
                    "t.create_time " +
                    "from cbc_my_oil_coupon t " +
                    "where t.account_id=?1 " +
                    "GROUP BY t.order_id " +
                    "order by t.use_state asc limit ?2,?3"
            ,nativeQuery = true)
    List<Object[]> findOilSetMeal(String accountId,Integer begin,Integer end);

    List<MyOilCoupon> findByAccountIdAndOrderIdOrderByUseStateAscPanDesc(String accountId,String orderId);

    MyOilCoupon findByPan(String pan);

    List<MyOilCoupon> findByOrderIdOrderByUseStateAsc(String orderId);

    Page<MyOilCoupon> findByAccountIdAndUseStateAndIdNotIn(String accountId, String useState, List<String> ids, Pageable pageable);

    //统计用户未使用的油券张数
    @Query(value = "SELECT COUNT(*) FROM `cbc_my_oil_coupon` WHERE account_id=?1 AND use_state=0;",nativeQuery = true)
    long findByUseState(String accountId);
    //统计用户未使用的油卷金额
    @Query(value = "SELECT SUM(pan_amt) FROM `cbc_my_oil_coupon` WHERE account_id=?1 AND use_state=0;",nativeQuery = true)
    Double findByPanamt(String accountId);
}
