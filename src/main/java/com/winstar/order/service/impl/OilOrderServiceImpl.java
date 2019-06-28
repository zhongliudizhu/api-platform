package com.winstar.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.exception.NotFoundException;
import com.winstar.kafka.Product;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.Constant;
import com.winstar.order.vo.PayInfoVo;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.GoodsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        }
        return "ok";
    }


    @Override
    public OilOrder getOneOrder(String serialNumber) throws NotFoundException {
        OilOrder oilOrder = new OilOrder();
        if (serialNumber.contains("wxyj")) {
            oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
            if (ObjectUtils.isEmpty(oilOrder) || oilOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
                logger.info("订单 {} 不存在+++++++++++++++++++++++++++++",serialNumber);
                throw new NotFoundException("oilOrder.order");
            }
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
        }
        return oilOrder;
    }

}
