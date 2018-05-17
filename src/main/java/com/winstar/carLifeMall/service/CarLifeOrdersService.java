package com.winstar.carLifeMall.service;

import com.winstar.carLifeMall.entity.CarLifeOrders;
import com.winstar.cashier.construction.utils.Arith;
import com.winstar.exception.NotFoundException;
import com.winstar.order.utils.Constant;
import com.winstar.order.vo.PayInfoVo;
import com.winstar.user.utils.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * 名称： OrdersService
 * 作者： dpw
 * 日期： 2018-05-07 11:07
 * 描述： 汽车生活订单
 **/
@Service
public class CarLifeOrdersService {
    /**
     * 根据订单号获取订单信息
     *
     * @param serialNo
     * @return
     * @throws NotFoundException
     */
    public CarLifeOrders getCarLifeOrdersBySerialNo(String serialNo) throws NotFoundException {

        CarLifeOrders carLifeOrders = ServiceManager.carLifeOrdersRepository.findByOrderSerial(serialNo);
        if (ObjectUtils.isEmpty(carLifeOrders) || carLifeOrders.getIsAvailable() == Integer.valueOf(Constant.IS_NORMAL_CANCELED)) {
            throw new NotFoundException("carLifeOrders");
        }

        return carLifeOrders;
    }

    /**
     * 更新订单支付状态
     *
     * @param payInfo
     * @return
     * @throws NotFoundException
     */
    public String updateCarLifeOrderCashier(PayInfoVo payInfo) throws NotFoundException {
        Date time = new Date();
        Integer payStatus = payInfo.getPayState();
        if (payStatus != 0 && payStatus != 1) {
            return "1";
        }
        String serialNumber = payInfo.getOrderSerialNumber();
        CarLifeOrders carLifeOrders = ServiceManager.carLifeOrdersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(carLifeOrders)) {
            return "2";
        }

        carLifeOrders.setBankSerialNo(payInfo.getBankSerialNumber());
        carLifeOrders.setPayTime(payInfo.getPayTime());
        carLifeOrders.setPayType(payInfo.getPayType());
        carLifeOrders.setPayStatus(payInfo.getPayState());

        carLifeOrders.setSendStatus(2);
        carLifeOrders.setStatus(3);

        carLifeOrders.setIsAvailable(Integer.valueOf(Constant.IS_NORMAL_NORMAL));
        carLifeOrders.setUpdateTime(time);
        carLifeOrders.setFinishTime(time);
        ServiceManager.carLifeOrdersRepository.save(carLifeOrders);

        return "ok";
    }

}
