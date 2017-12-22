package com.winstar.order.service.impl;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.service.CouponService;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.Constant;
import com.winstar.order.vo.PayInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * @author shoo on 2017/12/14 9:54.
 *
 */
@Service
public class OilOrderServiceImpl implements OilOrderService {
    private static Logger logger = LoggerFactory.getLogger(OilOrderServiceImpl.class);
    @Autowired
    private OilOrderRepository oilOrderRepository;
    @Autowired
    private CouponService couponService;
    @Override
    public String updateOrderCashier(PayInfoVo payInfo) {
        Date time = new Date();
        Integer payStatus = payInfo.getPayState();
        if (payStatus != 0 && payStatus != 1 ) {
            return "1";
        }
        OilOrder oilOrder = oilOrderRepository.findBySerialNumber(payInfo.getOrderSerialNumber());
        if(ObjectUtils.isEmpty(oilOrder)){
            return "2";
        }
        oilOrder.setBankSerialNo(payInfo.getBankSerialNumber());
        oilOrder.setPayPrice(payInfo.getPayPrice());
        oilOrder.setPayTime(payInfo.getPayTime());
        oilOrder.setPayType(payInfo.getPayType());
        oilOrder.setPayStatus(payInfo.getPayState());

        oilOrder.setSendStatus(3);
        oilOrder.setStatus(3);
        oilOrder.setUpdateTime(time);
        oilOrder.setFinishTime(time);
        oilOrderRepository.save(oilOrder);
        //活动2发优惠券
        try{
            if(oilOrder.getActivityId().equals(Constant.CBC_ACTIVITY_SEC)){
                MyCoupon coupon = couponService.sendCoupon(oilOrder.getAccountId(),oilOrder.getActivityId(),oilOrder.getItemId());
                if(!ObjectUtils.isEmpty(coupon)){
                    logger.info("活动2，发优惠券，couponId："+coupon.getId());
                }
            }
        }catch (Exception ex){
            logger.error("发优惠券失败"+ex);
        }
        return "ok";
    }

    @Override
    public OilOrder getOneOrder(String serialNumber) {
        return oilOrderRepository.findBySerialNumber(serialNumber);
    }
}
