package com.winstar.carLifeMall.controller;

import com.winstar.carLifeMall.entity.*;
import com.winstar.carLifeMall.param.CarLifeOrdersParam;
import com.winstar.carLifeMall.repository.CarLifeOrdersRepository;
import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.exception.*;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.OilOrderUtil;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author dpw on 2018/5/7 09:52.
 * 汽车生活订单
 */
@RestController
@RequestMapping("/api/v1/cbc/carLife/orders")
public class CarLifeOrderController {
    public static final Logger logger = LoggerFactory.getLogger(CarLifeOrderController.class);

    @Autowired
    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;

    private static final int ACTIVITY_EVENING_MARKET = 10;

    /**
     * 关闭超过半小时不支付的订单
     *
     * @return
     */
    @GetMapping("/autoCannel/{serialNumber}/serialNumber")
    public ResponseEntity cancelOrders() {
        Date end = DateUtil.addMinute(DateUtil.getNowDate(), -30);
        Date begin = DateUtil.addYear(end, -1);
        //查出未付款未关闭的订单
        List<CarLifeOrders> orders = ServiceManager.carLifeOrdersRepository.findByIsAvailableAndStatusAndCreateTimeBetween(0, 1, begin, end);
        for (CarLifeOrders carLifeOrders : orders
                ) {
            carLifeOrders.setIsAvailable(1);
            carLifeOrders.setUpdateTime(new Date());
            if (!StringUtils.isEmpty(carLifeOrders.getCouponId())) {
                //1.返还优惠券
                ServiceManager.couponService.cancelMyCoupon(carLifeOrders.getCouponId());
            }
        }
        ServiceManager.carLifeOrdersRepository.save(orders);
        logger.info("关闭汽车生活订单数量：" + orders.size());
        return new ResponseEntity("关闭成功", HttpStatus.OK);
    }


    /**
     * 添加汽车服务订单
     *
     * @param carLifeOrdersParam 商品id
     */
    @PostMapping(value = "/add")
    public ResponseEntity addCarLifeOrder(@RequestBody CarLifeOrdersParam carLifeOrdersParam, HttpServletRequest request) throws NotFoundException, NotRuleException, InvalidParameterException {
        String accountId = ServiceManager.accountService.getAccountId(request);

        checkParam(carLifeOrdersParam);
        Item itemCheck = ServiceManager.itemRepository.findOne(carLifeOrdersParam.getItemId());

        if (null == itemCheck) throw new NotFoundException("item");

        earlyAndEveningMarketConfigService.checkEarlyAndEveningMarketIsOk(itemCheck.getActiveType());
        checkRepeatedBuy(itemCheck.getActiveType(), accountId);

        long count = ServiceManager.itemSellerRelationRepository.countByItemIdAndSellerId(carLifeOrdersParam.getItemId(), carLifeOrdersParam.getSellerId());
        if (count == 0)
            throw new NotRuleException("illegalItemSeller");

        Seller sellerCheck = ServiceManager.sellerRepository.findOne(carLifeOrdersParam.getSellerId());
        if (null == sellerCheck) throw new NotFoundException("seller");
        checkStorageCount(itemCheck);
        CarLifeOrders result = initCarLifeOrders(itemCheck, sellerCheck, carLifeOrdersParam, accountId);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    void checkRepeatedBuy(int activityType, String accountId) throws NotRuleException {
        if(activityType != ACTIVITY_EVENING_MARKET){
            return;
        }
        long times = ServiceManager.carLifeOrdersRepository.countByIsAvailableAndActivityTypeAndAccountId(0, activityType, accountId);
        if (times > 0) {
            throw new NotRuleException("justOnce.earlyAndEveningMarket");
        }
    }

    void checkStorageCount(Item item) throws NotRuleException {
        long num = ServiceManager.carLifeOrdersRepository.countByIsAvailableAndItemId(0, item.getId());
        if (item.getSaleStatus() == 1 || num >= item.getStorageCount())
            throw new NotRuleException("isSale.carLifeOrders");
    }

    private void checkParam(CarLifeOrdersParam carLifeOrdersParam) throws NotRuleException {
        if (null == carLifeOrdersParam)
            throw new NotRuleException("carLifeOrdersParam");
        if (StringUtils.isEmpty(carLifeOrdersParam.getItemId()))
            throw new NotRuleException("itemId");
        if (StringUtils.isEmpty(carLifeOrdersParam.getReserveMobile()))
            throw new NotRuleException("reserveMobile");
        if (StringUtils.isEmpty(carLifeOrdersParam.getSellerId()))
            throw new NotRuleException("sellerId");
    }

    private CarLifeOrders initCarLifeOrders(Item item, Seller seller, CarLifeOrdersParam carLifeOrdersParam, String accountId) {
        CarLifeOrders carLifeOrders = new CarLifeOrders();
        carLifeOrders.setAccountId(accountId);
        carLifeOrders.setCreateTime(new Date());
        carLifeOrders.setDiscountAmount(0d);
        carLifeOrders.setIsAvailable(0);
        carLifeOrders.setIsPromotion(item.getActiveType());
        carLifeOrders.setOrderSerial(OilOrderUtil.getCarLifeSerialNumber());
        carLifeOrders.setSendStatus(2);
        carLifeOrders.setStatus(1);
        carLifeOrders.setOrderFrom(1);
        carLifeOrders.setPayStatus(0);
        carLifeOrders.setPhoneNo(carLifeOrdersParam.getReserveMobile());
        carLifeOrders.setSalePrice(item.getSalePrice());
        carLifeOrders.setPayPrice(item.getSalePrice());
        CarLifeOrders carLifeOrdersSaved = ServiceManager.carLifeOrdersRepository.save(carLifeOrders);

        OrdersItems ordersItems = new OrdersItems();
        ordersItems.setOrderSerial(carLifeOrdersSaved.getOrderSerial());
        ordersItems.setItemId(item.getId());
        ordersItems.setItemName(item.getName());
        ordersItems.setSellerId(seller.getId());
        ordersItems.setSellerName(seller.getName());
        ordersItems.setReserveTime(DateUtil.StringToDate(carLifeOrdersParam.getReserveTime()));
        ordersItems.setReserveMobile(carLifeOrdersParam.getReserveMobile());
        ordersItems.setAddress(seller.getAddress());
        ordersItems.setTelephone(seller.getTelephone());

        OrdersItems ordersItemsSaved = ServiceManager.ordersItemsRepository.save(ordersItems);
        carLifeOrdersSaved.setOrdersItems(ordersItemsSaved);
        return carLifeOrders;
    }

    /**
     * 判断订单是否能继续支付
     *
     * @param serialNumber 订单序列号
     */
    @GetMapping(value = "/judge/{serialNumber}/serialNumber")
    public ResponseEntity judgeOrder(@PathVariable String serialNumber) throws MissingParameterException, NotRuleException, NotFoundException {
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.carLifeOrders");
        }
        CarLifeOrders order = ServiceManager.carLifeOrdersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("carLifeOrders");
        }
        if (order.getIsAvailable() == Integer.valueOf(Constant.IS_NORMAL_CANCELED)) {
            throw new NotRuleException("closed.carLifeOrders");
        }
        return new ResponseEntity<>(new SimpleResult("OK"), HttpStatus.OK);
    }

    /* *
     * 查询单个订单-根据序列号
     */
    @GetMapping(value = "get/{serialNumber}/serialNumber", produces = "application/json;charset=utf-8")
    public ResponseEntity getOrders(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotFoundException {
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.carLifeOrders");
        }
        CarLifeOrders order = ServiceManager.carLifeOrdersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("carLifeOrders");
        }
        order.setOrdersItems(ServiceManager.ordersItemsRepository.findByOrderSerial(order.getOrderSerial()));

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /* *
     * 确认收货
     */
    @PostMapping(value = "confirm", produces = "application/json;charset=utf-8")
    public ResponseEntity confirmOrdersReceived(@RequestParam String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.carLifeOrders");
        }
        CarLifeOrders order = ServiceManager.carLifeOrdersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("carLifeOrders");
        }
        if (order.getIsAvailable() == 1 || order.getStatus() < 3)
            throw new NotRuleException("illegalOrderStatus.carLifeOrders");
        order.setSendStatus(3);
        order.setUpdateTime(new Date());
        CarLifeOrders carLifeOrdersSaved = ServiceManager.carLifeOrdersRepository.save(order);
        carLifeOrdersSaved.setOrdersItems(ServiceManager.ordersItemsRepository.findByOrderSerial(order.getOrderSerial()));

        return new ResponseEntity<>(carLifeOrdersSaved, HttpStatus.OK);
    }

    /**
     * 根据条件查询用户订单集合
     * 0 全部  -1 已取消  1 待支付  3 已完成
     */
    @GetMapping(value = "list/{status}/status")
    public ResponseEntity getOrdersByAccountId(@PathVariable Integer status, HttpServletRequest request)
            throws NotFoundException, NotRuleException, MissingParameterException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        if (StringUtils.isEmpty(status)) {
            throw new MissingParameterException("status.carLifeOrders");
        }
        List<CarLifeOrders> carLifeOrders = ServiceManager.carLifeOrdersRepository.findByAccountIdOrderByCreateTimeDesc(accountId);
        if (-1 == status) {
            carLifeOrders = carLifeOrders.stream().filter(o -> o.getIsAvailable() == Integer.valueOf(Constant.IS_NORMAL_CANCELED)).collect(toList());
        } else if (0 == status) {

        } else {
            carLifeOrders = carLifeOrders.stream().filter(o -> o.getStatus() == status).filter(o -> o.getIsAvailable() == 0).collect(toList());
        }
        if (carLifeOrders.size() == 0) {
            throw new NotFoundException("carLifeOrder");
        }
        carLifeOrders.forEach(t -> {
            t.setOrdersItems(ServiceManager.ordersItemsRepository.findByOrderSerial(t.getOrderSerial()));
        });
        return new ResponseEntity<>(carLifeOrders, HttpStatus.OK);
    }

    /**
     * 关闭汽车生活订单:只有未付款的订单才能关闭
     *
     * @param serialNumber 订单序列号
     * @return 订单
     */
    @PutMapping(value = "/shutdown/{serialNumber}/serialNumber", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity shutdownOrder(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) {
            throw new NotFoundException("accountId.carLifeOrders");
        }
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.carLifeOrders");
        }
        CarLifeOrders carLifeCarLifeOrders = ServiceManager.carLifeOrdersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(carLifeCarLifeOrders)) {
            throw new NotFoundException("carLifeOrders");
        }
        if (carLifeCarLifeOrders.getStatus() != 1) {
            throw new NotRuleException("cannotShutdown.carLifeOrders");
        }
        if (carLifeCarLifeOrders.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
            throw new NotRuleException("alreadyClosed.carLifeOrders");
        }
        if (!carLifeCarLifeOrders.getAccountId().equals(accountId)) {
            throw new NotRuleException("notYourOrder.carLifeOrders");
        }
        carLifeCarLifeOrders.setIsAvailable(Integer.valueOf(Constant.IS_NORMAL_CANCELED));
        carLifeCarLifeOrders = ServiceManager.carLifeOrdersRepository.save(carLifeCarLifeOrders);
        //返还优惠券
        if (!StringUtils.isEmpty(carLifeCarLifeOrders.getCouponId())) {
            ServiceManager.couponService.cancelMyCoupon(carLifeCarLifeOrders.getCouponId());
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}