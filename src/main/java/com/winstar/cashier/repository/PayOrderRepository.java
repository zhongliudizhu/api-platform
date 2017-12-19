package com.winstar.cashier.repository;

import com.winstar.cashier.entity.PayOrder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * 支付订单数据仓库
 * @author wanghaibo
 */
public interface PayOrderRepository extends PagingAndSortingRepository<PayOrder,String> {
    /**
     * 根据订单号查询退款单
     * @param orderNumber 订单号
     * @return
     */
    List<PayOrder> findByOrderNumberAndState(String orderNumber, String state);

    List<PayOrder> findByOrderNumber(String orderNumber);

    /**
     * 根据支付订单号查询退款单
     * @param payOrderNumber 订单号
     * @return
     */
    PayOrder findByPayOrderNumber(String payOrderNumber);

    /**
     * 银行流水号查询订单
     * @param qid
     * @return
     */
    PayOrder findByQid(String qid);

    List<PayOrder> findByStateAndCreatedAtBetween(String state, Date date1, Date date2);

    List<PayOrder> findByStateAndCreatedAtBefore(String state, Date date);

}
