package com.winstar.event;

import com.winstar.cashier.entity.PayOrder;
import com.winstar.oil.service.SendOilCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zl on 2018/4/3
 */
@Async
@Component
public class SendOilCouponListener implements ApplicationListener<SendOilCouponEvent>{

    @Autowired
    private SendOilCouponService sendOilCouponService;

    private final static Logger logger = LoggerFactory.getLogger(SendOilCouponListener.class);

    @Override
    public void onApplicationEvent(SendOilCouponEvent sendOilCouponEvent) {
        logger.info("------------开始通知油卡的发送-------------" + sendOilCouponEvent);
        try {
            PayOrder payOrder = sendOilCouponEvent.getPayOrder();
            if(payOrder.getOrderOwner().equals("1") && payOrder.getState().equals("1")){
                sendOilCouponService.checkCard(payOrder.getOrderNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
