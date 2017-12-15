package com.winstar.order.schedule;

import com.winstar.coupon.service.CouponService;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/12/15 16:45.
 * @Describe： 定时关闭未付款的订单，使用优惠券的返还优惠券(每整点关闭半小时之前的)
 */
@Component
@EnableScheduling
public class OilOrderShutdownController {
    public static final Logger logger = LoggerFactory.getLogger(OilOrderShutdownController.class);
    @Autowired
    private OilOrderRepository oilOrderRepository;
    @Autowired
    private CouponService couponService;


    @Scheduled(cron = "0 0/30 * * * ?")
    public void shutdownOilOrder() throws NotRuleException {
        Date end = DateUtil.addMinute(DateUtil.getNowDate(),-30);
        Date begin = DateUtil.addYear(end,-1);
        //查出未付款未关闭的订单
        List<OilOrder> orders = oilOrderRepository.findByIsAvailableAndStatusAndCreateTimeBetween("0", 1,begin,end);
        for (OilOrder oilOrder:orders
             ) {
            oilOrder.setIsAvailable("1");
            oilOrder.setUpdateTime(new Date());
            if(!StringUtils.isEmpty(oilOrder.getCouponId())){
                //1.返还优惠券
                couponService.cancelMyCoupon(oilOrder.getCouponId());
            }
        }
        oilOrderRepository.save(orders);
        logger.info("关闭油券订单数量："+orders.size());
    }

}
