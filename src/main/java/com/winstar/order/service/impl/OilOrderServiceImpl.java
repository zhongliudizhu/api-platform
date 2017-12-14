package com.winstar.order.service.impl;

import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.vo.PayInfoVo;
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
    @Autowired
    private OilOrderRepository oilOrderRepository;
    @Override
    public String updateOrderCashier(PayInfoVo payInfo) {
        Integer payStatus = payInfo.getPayState();
        if (payStatus != 0 && payStatus != 1 ) {
            return "1";
        }
        OilOrder oilOrder = oilOrderRepository.findBySerialNo(payInfo.getOrderSerialNumber());
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
        oilOrder.setUpdateTime(new Date());

        oilOrderRepository.save(oilOrder);
        return "ok";
    }
}
