package com.winstar.order.service.impl;

import com.winstar.cashier.construction.utils.Arith;
import com.winstar.coupon.service.CouponService;
import com.winstar.exception.NotFoundException;
import com.winstar.order.entity.FlowOrder;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.FlowOrderUtil;
import com.winstar.order.vo.FlowResult;
import com.winstar.order.vo.PayInfoVo;
import com.winstar.user.service.OneMoneyCouponRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/12/14 9:54.
 *   付款成功回调订单
 *  ！！！修改此类一定联系张林 ！！！
 */
@Service
public class OilOrderServiceImpl implements OilOrderService {
    private static Logger logger = LoggerFactory.getLogger(OilOrderServiceImpl.class);
    @Autowired
    private OilOrderRepository oilOrderRepository;
    @Autowired
    private FlowOrderRepository flowOrderRepository;
    @Autowired
    private OneMoneyCouponRecordService oneMoneyCouponRecordService;
    @Value("${info.flowUrl}")
    private String flowUrl;
    @Override
    public String updateOrderCashier(PayInfoVo payInfo) throws NotFoundException {
        Date time = new Date();
        Integer payStatus = payInfo.getPayState();
        if (payStatus != 0 && payStatus != 1 ) {
            return "1";
        }
        String serialNumber = payInfo.getOrderSerialNumber();
        if(serialNumber.contains("wxyj")){
            OilOrder oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if(ObjectUtils.isEmpty(oilOrder)||oilOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
                return "2";
            }
            if(oilOrder.getItemId().equals(Constant.ONE_BUY_ITEMID)){
                oneMoneyCouponRecordService.changeStatus(oilOrder.getAccountId());
            }
            oilOrder.setBankSerialNo(payInfo.getBankSerialNumber());
            oilOrder.setPayPrice(Arith.div(payInfo.getPayPrice(),100));//分转换元
            oilOrder.setPayTime(payInfo.getPayTime());
            oilOrder.setPayType(payInfo.getPayType());
            oilOrder.setPayStatus(payInfo.getPayState());

            oilOrder.setSendStatus(3);
            oilOrder.setStatus(3);
            /******************/
            oilOrder.setIsAvailable("0");
            oilOrder.setUpdateTime(time);
            oilOrder.setFinishTime(time);
            oilOrderRepository.save(oilOrder);

        }else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if(ObjectUtils.isEmpty(flowOrder)||flowOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
                return "2";
            }
            flowOrder.setBankSerialNo(payInfo.getBankSerialNumber());
            flowOrder.setPayPrice(Arith.div(payInfo.getPayPrice(),100));//分转换元
            flowOrder.setPayTime(payInfo.getPayTime());
            flowOrder.setPayType(payInfo.getPayType());
            flowOrder.setPayStatus(payInfo.getPayState());//支付成功
            FlowResult flowResult = FlowOrderUtil.chargeFlow(flowOrder.getPhoneNo(),"M","1024","N","30",flowUrl);

            if(ObjectUtils.isEmpty(flowResult)||flowResult.getOrderStatus().equals("SubmitFail")){
                flowOrder.setSendStatus(4);//发货失败
                flowOrder.setStatus(3);//已完成
            }else{
                flowOrder.setSendStatus(3);//发货成功
                flowOrder.setStatus(3);//已完成
            }
            flowOrder.setUpdateTime(time);
            flowOrder.setFinishTime(time);
            flowOrderRepository.save(flowOrder);
        }
        /*//活动2发优惠券
        try{
            if(oilOrder.getActivityId().equals(Constant.CBC_ACTIVITY_SEC)){
                MyCoupon coupon = couponService.sendCoupon(oilOrder.getAccountId(),oilOrder.getActivityId(),oilOrder.getItemId());
                if(!ObjectUtils.isEmpty(coupon)){
                    logger.info("活动2，发优惠券，couponId："+coupon.getId());
                }
            }
        }catch (Exception ex){
            logger.error("发优惠券失败"+ex);
        }*/
        return "ok";
    }

    @Override
    public OilOrder getOneOrder(String serialNumber) throws NotFoundException {
        OilOrder oilOrder = new OilOrder();
        if(serialNumber.contains("wxyj")){
            oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if(ObjectUtils.isEmpty(oilOrder)||oilOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
                throw new NotFoundException("oilOrder.order");
            }
        }else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if(ObjectUtils.isEmpty(flowOrder)||flowOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
                throw new NotFoundException("flowOrder.order");
            }
            BeanUtils.copyProperties(flowOrder,oilOrder);
        }

        return oilOrder;
    }

    @Override
    public List<OilOrder> getOrderByAccountAndActivityId(String accountId, String activityId) {
        return oilOrderRepository.findByAccountIdAndActivityId(accountId,activityId, DateUtil.getMonthBegin(),DateUtil.getMonthEnd());
    }
}
