package com.winstar.carLifeMall.service;

import com.winstar.carLifeMall.entity.CarLifeOrders;
import com.winstar.exception.NotFoundException;
import com.winstar.order.utils.Constant;
import com.winstar.user.utils.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

        CarLifeOrders carLifeOrders = ServiceManager.ordersRepository.findByOrderSerial(serialNo);
            if(ObjectUtils.isEmpty(carLifeOrders)|| carLifeOrders.getIsAvailable()== Integer.valueOf(Constant.IS_NORMAL_CANCELED)){
                throw new NotFoundException("oilOrder.order");
            }

        return carLifeOrders;
    }
}
