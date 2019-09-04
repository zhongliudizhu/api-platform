package com.winstar.order.service;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.exception.NotFoundException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.vo.PayInfoVo;

import java.util.List;

/**
 * @author shoo on 2017/12/14 9:45.
 * 订单
 */
public interface OilOrderService {
     /*
     * 付款成功更新订单（张林调用）
     * */
     String updateOrderCashier(PayInfoVo payInfo);
     /*
     * 根据订单序列号查询订单
     * */
     OilOrder getOneOrder(String serialNumber) throws NotFoundException;
     /*
     * 根据订单序列号查询订单（不过滤关闭订单）
     * */
     OilOrder getOrder(String serialNumber);

     OilOrder saveOrderAndCoupon(List<AccountCoupon> accountCoupons, OilOrder order) throws Exception;

}
