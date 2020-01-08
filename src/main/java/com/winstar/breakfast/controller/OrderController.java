package com.winstar.breakfast.controller;

import com.winstar.breakfast.entity.Account;
import com.winstar.breakfast.entity.Order;
import com.winstar.breakfast.entity.Shop;
import com.winstar.breakfast.repository.AccountRepository;
import com.winstar.breakfast.repository.OrderRepository;
import com.winstar.breakfast.repository.ShopRepository;
import com.winstar.breakfast.utils.BreakfastOrderUtils;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zl on 2020/1/6
 */
@RestController
@RequestMapping("/api/v1/noAuth/breakfastOrder")
@Slf4j
public class OrderController {
    @Resource(name = "breakfastAccountRepository")
    private AccountRepository accountRepository;
    private ShopRepository shopRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OrderController(ShopRepository shopRepository, OrderRepository orderRepository) {
        this.shopRepository = shopRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * 创建订单
     */
    @PostMapping(value = "/addOrder")
    public Result addOrder(@RequestParam String itemId, @RequestParam(defaultValue = "", required = false) String couponId,
                           HttpServletRequest request) {
        log.info("入参为 itemId is {}", itemId);
        String accountId = request.getHeader("accountId");
        if (StringUtils.isEmpty(accountId)) {
            return Result.fail("auth_failed", "您未登陆，请先登陆");
        }
        Account account = accountRepository.findOne(accountId);
        if (account.getDel().equals("yes")) {
            log.info("用户已被删除");
            return Result.fail("account_is_del", "用户不存在");
        }
        Shop goods = shopRepository.findOne(itemId);
        if (ObjectUtils.isEmpty(goods)) {
            log.info("商品信息不存在");
            return Result.fail("goods_not_exist", "商品信息不存在");
        }
        if (goods.getStatus() == 0) {
            log.info("商品已下架");
            return Result.fail("goods_undercarriage", "商品已下架");
        }
        String serialNumber = BreakfastOrderUtils.getSerialNumber();
        Order order = new Order(accountId, serialNumber, goods.getPrice(), itemId, account.getPhone(), 0, new Date(), "0");
        if (!StringUtils.isEmpty(couponId)) {
            order = BreakfastOrderUtils.checkCouponAndSetOrder(couponId, account, order);
        } else {
            order.setDiscountAmount(0.00);
            order.setPayPrice(goods.getPrice() - order.getDiscountAmount());
        }
        order = orderRepository.save(order);
        return Result.success(order.getSerialNumber());
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/shutDownOrder/{serialNumber}")
    public Result shutDownOrder(@PathVariable String serialNumber, HttpServletRequest request) {
        log.info("入参信息为 serialNumber is {}", serialNumber);
        String accountId = request.getHeader("accountId");
        if (StringUtils.isEmpty(accountId)) {
            return Result.fail("auth_failed", "您未登陆，请先登陆");
        }
        Order order = orderRepository.findByAccountIdAndSerialNumber(accountId, serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            log.info("无相应订单信息");
            return Result.fail("order_not_exist", "无相应订单信息");
        }
        if (order.getIsAvailable().equals("1")) {
            log.info("订单已经被关闭了");
            return Result.success(true);
        }
        if (order.getPayStatus() == 1) {
            log.info("已支付订单无法关闭");
            return Result.fail("order_is_paid", "已支付订单无法关闭");
        }
        order.setIsAvailable("1");
        orderRepository.save(order);
        return Result.success(true);
    }

    @GetMapping(value = "/getOrderInfo/{serialNumber}")
    public Result getOrderInfo(@PathVariable String serialNumber, HttpServletRequest request) {
        log.info("入参信息为 serialNumber is {}", serialNumber);
        String accountId = request.getHeader("accountId");
        if (StringUtils.isEmpty(accountId)) {
            return Result.fail("auth_failed", "您未登陆，请先登陆");
        }
        if (StringUtils.isEmpty(serialNumber)) {
            return Result.fail("order_not_exist", "无相应订单信息");
        }
        Order order = orderRepository.findByAccountIdAndSerialNumber(accountId, serialNumber);
        if (ObjectUtils.isEmpty(order)) {
            log.info("无相应订单信息");
            return Result.fail("order_not_exist", "无相应订单信息");
        }
        order.setShopName(shopRepository.findOne(order.getItemId()).getShopName());
        return Result.success(order);
    }

    /**
     * 根据条件查询用户订单集合
     * 0 全部  -1 已取消  1 待支付  2 已支付
     */
    @GetMapping(value = "/getOrders")
    public Result getOrdersByAccountId(@RequestParam(defaultValue = "0") String status, HttpServletRequest request) {
        log.info("入参信息为 status is {}", status);
        String accountId = request.getHeader("accountId");
        if (StringUtils.isEmpty(accountId)) {
            return Result.fail("auth_failed", "您未登陆，请先登陆");
        }
        List<Order> orderList = orderRepository.findByAccountId(accountId);
        int orderStatus = Integer.parseInt(status);
        if (orderStatus == -1) {
            orderList = orderList.stream().filter(s -> s.getIsAvailable().equals("1")).collect(Collectors.toList());
        } else if (orderStatus == 1) {
            orderList = orderList.stream().filter(s -> s.getIsAvailable().equals("0") && s.getPayStatus() == 0).collect(Collectors.toList());
        } else if (orderStatus == 2) {
            orderList = orderList.stream().filter(s -> s.getIsAvailable().equals("0") && s.getPayStatus() == 1).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(orderList)) {
            orderList.forEach(s -> {
                Shop goods = shopRepository.findOne(s.getItemId());
                s.setShopName(goods.getShopName());
                s.setShopImg(goods.getShopImg());
            });
            Collections.sort(orderList, (p1, p2) -> p2.getCreateTime().compareTo(p1.getCreateTime()));
        }
        return Result.success(CollectionUtils.isEmpty(orderList) ? new ArrayList<>() : orderList);

    }

}
