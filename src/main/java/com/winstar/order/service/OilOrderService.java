package com.winstar.order.service;

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
}
