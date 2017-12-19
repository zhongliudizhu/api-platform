package com.winstar.order.service;

import com.winstar.order.entity.OilOrder;
import com.winstar.order.vo.PayInfoVo;

/**
 * @author shoo on 2017/12/14 9:45.
 * 订单
 */
public interface OilOrderService {
     /*
     * 付款成功更新订单（张林调用）
     * */
     public String updateOrderCashier(PayInfoVo payInfo);
     /*
     * 根据订单序列号查询订单
     * */
     public OilOrder getOneOrder(String serialNumber);
}
