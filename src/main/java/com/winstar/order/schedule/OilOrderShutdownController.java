package com.winstar.order.schedule;

import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.order.entity.FlowOrder;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/12/15 16:45.
 *  定时关闭未付款的订单，使用优惠券的返还优惠券(每整点或半点关闭半小时之前的)
 */
@Component
@EnableScheduling
public class OilOrderShutdownController {

    public static final Logger logger = LoggerFactory.getLogger(OilOrderShutdownController.class);

    @Autowired
    private OilOrderRepository oilOrderRepository;

    @Autowired
    private FlowOrderRepository flowOrderRepository;

    @Autowired
    AccountCouponService accountCouponService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void shutdownOilOrder() {
        Date end = DateUtil.addMinute(DateUtil.getNowDate(),-30);
        Date begin = DateUtil.addYear(end,-1);
        //查出未付款未关闭的订单
        List<OilOrder> orders = oilOrderRepository.findByIsAvailableAndStatusAndCreateTimeBetween("0", 1,begin,end);
        for (OilOrder oilOrder:orders
             ) {
            oilOrder.setIsAvailable("1");
            oilOrder.setUpdateTime(new Date());
            if(!StringUtils.isEmpty(oilOrder.getCouponId())){
                accountCouponService.modifyCouponState(oilOrder.getAccountId(), oilOrder.getCouponId(), AccountCouponService.NORMAL, null);
            }
        }
        oilOrderRepository.save(orders);
        logger.info("关闭油券订单数量："+orders.size());
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void shutdownFlowOrder() {
        Date end = DateUtil.addHour(DateUtil.getNowDate(),-1);
        Date begin = DateUtil.addYear(end,-1);
        //查出未付款未关闭的订单
        List<FlowOrder> flowOrders = flowOrderRepository.findByIsAvailableAndStatusAndCreateTimeBetween("0", 1,begin,end);
        for (FlowOrder flowOrder:flowOrders) {
            flowOrder.setIsAvailable("1");
            flowOrder.setUpdateTime(new Date());
            if(!StringUtils.isEmpty(flowOrder.getCouponId())){
                accountCouponService.modifyCouponState(flowOrder.getAccountId(), flowOrder.getCouponId(), AccountCouponService.NORMAL, null);
            }
        }
        flowOrderRepository.save(flowOrders);
        logger.info("关闭流量订单数量："+flowOrders.size());
    }

}
