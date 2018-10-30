package com.winstar.event;

import com.alibaba.fastjson.JSON;
import com.winstar.carLifeMall.service.CarLifeOrdersService;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.vo.PayInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zl on 2018/4/3
 */
@Async
@Component
public class ModifyOrderListener implements ApplicationListener<ModifyOrderEvent>{

    private final static Logger logger = LoggerFactory.getLogger(ModifyOrderListener.class);

    @Autowired
    private OilOrderService orderService;

    @Autowired
    private CarLifeOrdersService carLifeOrdersService;

    @Override
    public void onApplicationEvent(ModifyOrderEvent modifyOrderEvent) {
        logger.info("------------收到修改订单事件-------------" + modifyOrderEvent);
        PayOrder payOrder = modifyOrderEvent.getPayOrder();
        modifyOrder(payOrder);
    }

    private void modifyOrder(PayOrder payOrder){
        long beginTime = System.currentTimeMillis();
        PayInfoVo payInfoVo = new PayInfoVo();
        payInfoVo.setOrderSerialNumber(payOrder.getOrderNumber());
        payInfoVo.setBankSerialNumber(payOrder.getQid());
        payInfoVo.setPayPrice(Double.valueOf(payOrder.getPayAmount()));
        payInfoVo.setPayState(Integer.valueOf(payOrder.getState()));
        payInfoVo.setPayType(payOrder.getPayWay());
        payInfoVo.setPayTime(new Date());
        if(payOrder.getOrderNumber().contains("wxcar")){
            carLifeOrdersService.updateCarLifeOrderCashier(payInfoVo);
        }else{
            orderService.updateOrderCashier(payInfoVo);
        }
        long endTime = System.currentTimeMillis();
        logger.info("修改订单消耗时间：" + (endTime - beginTime) + "ms，订单号：" + payOrder.getOrderNumber());
    }

}
