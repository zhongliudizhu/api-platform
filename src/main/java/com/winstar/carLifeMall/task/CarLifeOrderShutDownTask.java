package com.winstar.carLifeMall.task;

import com.winstar.carLifeMall.entity.CarLifeOrders;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 名称： CarLifeOrderSutDownTask
 * 作者： dpw
 * 日期： 2018-05-08 10:09
 * 描述： shutDown
 **/
@Component
@EnableScheduling
public class CarLifeOrderShutDownTask {

    public static final Logger logger = LoggerFactory.getLogger(CarLifeOrderShutDownTask.class);
    /**
     * 关闭汽车生活订单
     *
     * @throws NotRuleException
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void shutdownCarLifeOrder() throws NotRuleException {
        Date end = DateUtil.addMinute(DateUtil.getNowDate(),-30);
        Date begin = DateUtil.addYear(end,-1);
        //查出未付款未关闭的订单
        List<CarLifeOrders> orders = ServiceManager.carLifeOrdersRepository.findByIsAvailableAndStatusAndCreateTimeBetween("0", 1,begin,end);
        for (CarLifeOrders carLifeOrders:orders
                ) {
            carLifeOrders.setIsAvailable(1);
            carLifeOrders.setUpdateTime(new Date());
            if(!StringUtils.isEmpty(carLifeOrders.getCouponId())){
                //1.返还优惠券
                ServiceManager.couponService.cancelMyCoupon(carLifeOrders.getCouponId());
            }
        }
        ServiceManager.carLifeOrdersRepository.save(orders);
        logger.info("关闭汽车生活订单数量："+orders.size());
    }

}
