package com.winstar.carLifeMall.controller;

import com.winstar.carLifeMall.entity.Item;
import com.winstar.carLifeMall.entity.Orders;
import com.winstar.carLifeMall.entity.OrdersItems;
import com.winstar.carLifeMall.entity.Seller;
import com.winstar.carLifeMall.param.CarLifeOrdersParam;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.OilOrderUtil;
import com.winstar.user.utils.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author dpw on 2018/5/7 13:52.
 * 汽车生活订单
 */
@RestController
@RequestMapping("/api/v1/cbc/carLiseOrders")
public class CarLifeOrderController {
    public static final Logger logger = LoggerFactory.getLogger(CarLifeOrderController.class);

    /**
     * 添加汽车服务订单
     *
     * @param carLifeOrdersParam 商品id
     */
    @PostMapping(produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addCarLifeOrder(@RequestBody CarLifeOrdersParam carLifeOrdersParam, HttpServletRequest request) throws NotFoundException, NotRuleException {
        String accountId = ServiceManager.accountService.getAccountId(request);

        checkParam(carLifeOrdersParam);
        Item itemCheck = ServiceManager.itemRepository.findOne(carLifeOrdersParam.getItemId());
        if (null == itemCheck) throw new NotFoundException("item");

        Seller sellerCheck = ServiceManager.sellerRepository.findOne(carLifeOrdersParam.getSellerId());
        if (null == sellerCheck) throw new NotFoundException("seller");

        Map map = initCarLifeOrders(itemCheck, sellerCheck, carLifeOrdersParam, accountId);
        return new ResponseEntity(map, HttpStatus.OK);
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

    private Map initCarLifeOrders(Item item, Seller seller, CarLifeOrdersParam carLifeOrdersParam, String accountId) {
        Orders orders = new Orders();
        orders.setAccountId(accountId);
        orders.setCreateTime(new Date());
        orders.setDiscountAmount(0d);
        orders.setIsAvailable(0);
        orders.setIsPromotion(item.getActiveType());
        orders.setOrderSerial(OilOrderUtil.getCarLifeSerialNumber());
        orders.setSendStatus(3);
        orders.setStatus(1);
        orders.setOrderFrom(1);
        orders.setPayStatus(1);
        orders.setSalePrice(item.getSalePrice());
        orders.setPayPrice(item.getSalePrice());
        Orders ordersSaved = ServiceManager.ordersRepository.save(orders);

        OrdersItems ordersItems = new OrdersItems();
        ordersItems.setItemId(item.getId());
        ordersItems.setItemName(item.getName());
        ordersItems.setSellerId(seller.getId());
        ordersItems.setSellerName(seller.getName());
        ordersItems.setReserveTime(carLifeOrdersParam.getReserveTime());
        ordersItems.setReserveMobile(carLifeOrdersParam.getReserveMobile());

        OrdersItems ordersItemsSaved = ServiceManager.ordersItemsRepository.save(ordersItems);

        Map map = new HashMap();
        map.put("orders", ordersSaved);
        map.put("ordersItems", ordersItemsSaved);
        return map;
    }

    /**
     * 判断订单是否能继续支付
     *
     * @param serialNumber 订单序列号
     */
    @GetMapping(value = "/judge/{serialNumber}/", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity judgeOrder(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.order");
        }
        Orders order = ServiceManager.ordersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("carLifeOrders");
        }
        if (order.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
            throw new NotRuleException("closed.order");
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /* *
     * 查询单个订单-根据序列号
     */
    @GetMapping(value = "/{serialNumber}/serialNumber", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity getOrders(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.carLifeOrders");
        }
        Orders order = ServiceManager.ordersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("carLifeOrders");
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * 根据条件查询用户订单集合
     * 0 全部  -1 已取消  1 待支付  3 已完成
     */
    @GetMapping(value = "/{status}/status", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity getOrdersByAccountId(@PathVariable Integer status, HttpServletRequest request)
            throws NotFoundException, ServiceUnavailableException, NotRuleException, MissingParameterException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) {
            throw new NotFoundException("accountId.carLifeOrders");
        }
        if (StringUtils.isEmpty(status)) {
            throw new MissingParameterException("status.carLifeOrders");
        }
        List<Orders> carLifeOrders = ServiceManager.ordersRepository.findByAccountIdOrderByCreateTimeDesc(accountId);
        if (-1 == status) {
            carLifeOrders = carLifeOrders.stream().filter(o -> o.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)).collect(toList());
        } else if (0 == status) {

        } else {
            carLifeOrders = carLifeOrders.stream().filter(o -> o.getStatus() == status).filter(o -> o.getIsAvailable().equals("0")).collect(toList());
        }
        if (carLifeOrders.size() == 0) {
            throw new NotFoundException("carLifeOrder");
        }
        return new ResponseEntity<>(carLifeOrders, HttpStatus.OK);
    }

    /**
     * 关闭油券订单:只有未付款的订单才能关闭
     *
     * @param serialNumber 订单序列号
     * @return 订单
     */
    @PutMapping(value = "/shutdown/{serialNumber}/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity shutdownOrder(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) {
            throw new NotFoundException("accountId.carLifeOrders");
        }
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.carLifeOrders");
        }
        Orders carLifeOrders = ServiceManager.ordersRepository.findByOrderSerial(serialNumber);
        if (ObjectUtils.isEmpty(carLifeOrders)) {
            throw new NotFoundException("carLifeOrders");
        }
        if (carLifeOrders.getStatus() != 1) {
            throw new NotRuleException("cannotShutdown.carLifeOrders");
        }
        if (carLifeOrders.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)) {
            throw new NotRuleException("alreadyClosed.carLifeOrders");
        }
        if (!carLifeOrders.getAccountId().equals(accountId)) {
            throw new NotRuleException("notYourOrder.carLifeOrders");
        }
        carLifeOrders.setIsAvailable(Integer.valueOf(Constant.IS_NORMAL_CANCELED));
        carLifeOrders = ServiceManager.ordersRepository.save(carLifeOrders);
        //返还优惠券
        if (!StringUtils.isEmpty(carLifeOrders.getCouponId())) {
            ServiceManager.couponService.cancelMyCoupon(carLifeOrders.getCouponId());
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
