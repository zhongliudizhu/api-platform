package com.winstar.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.exception.NotFoundException;
import com.winstar.kafka.Product;
import com.winstar.order.entity.FlowOrder;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.FlowOrderUtil;
import com.winstar.order.vo.FlowResult;
import com.winstar.order.vo.PayInfoVo;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.service.OneMoneyCouponRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author shoo on 2017/12/14 9:54.
 * 付款成功回调订单
 * ！！！修改此类一定联系张林 ！！！
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

    @Autowired
    AccountCouponService accountCouponService;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    Product product;

    @Value("${spring.kafka.template.default-topic}")
    private String topicName;

    @Override
    public String updateOrderCashier(PayInfoVo payInfo) {
        Date time = new Date();
        Integer payStatus = payInfo.getPayState();
        if (payStatus != 0 && payStatus != 1) {
            return "1";
        }
        String serialNumber = payInfo.getOrderSerialNumber();
        if (serialNumber.contains("wxyj")) {
            OilOrder oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder)) {
                return "2";
            }
            if (oilOrder.getItemId().equals(Constant.ONE_BUY_ITEMID)) {
                oneMoneyCouponRecordService.changeStatus(oilOrder.getAccountId());
            }
            oilOrder.setBankSerialNo(payInfo.getBankSerialNumber());
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
            //更新优惠券的状态为已使用
            if(!StringUtils.isEmpty(oilOrder.getCouponId()) && oilOrder.getActivityId().equals("2")){
                logger.info(oilOrder.getSerialNumber() + "优惠券信息不为空，更新优惠券状态并通知优惠券平台！");
                accountCouponService.modifyCouponState(oilOrder.getAccountId(), oilOrder.getCouponId(), AccountCouponService.USED, oilOrder.getSerialNumber());
                Goods goods = goodsRepository.findOne(oilOrder.getItemId());
                accountCouponService.writeOffCoupon(oilOrder.getCouponId(), oilOrder.getItemTotalValue().toString(), goods.getTags());
                logger.info(oilOrder.getSerialNumber() + "优惠券信息更新完毕！");
            }
            try {
                product.sendMessage(topicName, oilOrder.getSerialNumber(), JSON.toJSONString(oilOrder));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(flowOrder)) {
                return "2";
            }
            flowOrder.setBankSerialNo(payInfo.getBankSerialNumber());
            flowOrder.setPayTime(payInfo.getPayTime());
            flowOrder.setPayType(payInfo.getPayType());
            flowOrder.setPayStatus(payInfo.getPayState());//支付成功
            FlowResult flowResult = FlowOrderUtil.chargeFlow(flowOrder.getPhoneNo(), "M", "1024", "N", "30", flowUrl);

            if (ObjectUtils.isEmpty(flowResult) || flowResult.getOrderStatus().equals("SubmitFail")) {
                flowOrder.setSendStatus(4);//发货失败
                flowOrder.setStatus(3);//已完成
            } else {
                flowOrder.setSendStatus(3);//发货成功
                flowOrder.setStatus(3);//已完成
            }
            flowOrder.setUpdateTime(time);
            flowOrder.setFinishTime(time);
            flowOrderRepository.save(flowOrder);
            //更新优惠券的状态为已使用
            if(!StringUtils.isEmpty(flowOrder.getCouponId())){
                accountCouponService.modifyCouponState(flowOrder.getAccountId(), flowOrder.getCouponId(), AccountCouponService.USED, flowOrder.getSerialNumber());
                Goods goods = goodsRepository.findOne(flowOrder.getItemId());
                accountCouponService.writeOffCoupon(flowOrder.getCouponId(), flowOrder.getItemTotalValue().toString(), goods.getTags());
            }
            try {
                product.sendMessage(topicName, flowOrder.getSerialNumber(), JSON.toJSONString(flowOrder));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "ok";
    }


    @Override
    public OilOrder getOneOrder(String serialNumber) throws NotFoundException {
        OilOrder oilOrder = new OilOrder();
        if (serialNumber.contains("wxyj")) {
            oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder) || oilOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
                throw new NotFoundException("oilOrder.order");
            }
        } else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(flowOrder) || flowOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
                throw new NotFoundException("flowOrder.order");
            }
            BeanUtils.copyProperties(flowOrder, oilOrder);
        }

        return oilOrder;
    }

    @Override
    public OilOrder getOrder(String serialNumber) {
        OilOrder oilOrder = new OilOrder();
        if (serialNumber.contains("wxyj")) {
            oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder)) {
                return null;
            }
        } else {
            FlowOrder flowOrder = flowOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(flowOrder)) {
                return null;
            }
            BeanUtils.copyProperties(flowOrder, oilOrder);
        }
        return oilOrder;
    }

}
