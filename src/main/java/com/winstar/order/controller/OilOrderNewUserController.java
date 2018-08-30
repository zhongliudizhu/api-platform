package com.winstar.order.controller;

import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.cashier.construction.utils.Arith;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.exception.*;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.OilOrderUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @author shoo on 2017/7/7 13:52.
 * 油券订单
 */
@RestController
@RequestMapping("/api/v1/cbc/newUserOrders")
public class OilOrderNewUserController {
    public static final Logger logger = LoggerFactory.getLogger(OilOrderNewUserController.class);
    @Autowired
    private OilOrderRepository orderRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AccountService accountService;
    @Autowired
    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;

    @Value("${info.amount}")
    private Integer amount;

    /**
     * 添加新手活动油券订单
     *
     * @param itemId     商品id
     * @param activityId 活动id
     */
    @PostMapping(produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addOrder(@RequestParam String itemId
            , @RequestParam String activityId
            , @RequestParam(required = false, defaultValue = "") String couponId
            , HttpServletRequest request) throws NotFoundException, NotRuleException, InvalidParameterException {
        logger.error("优惠券id，couponId：" + couponId);
        String accountId = accountService.getAccountId(request);
        String serialNumber = OilOrderUtil.getSerialNumber();
        long startTime = System.currentTimeMillis();
        //1.根据商品id 查询商品
        Goods goods = shopService.findByGoodsId(itemId);
        logger.info("开始添加订单，goodsId：" + goods.getId());
        if (ObjectUtils.isEmpty(goods)) {
            logger.error("查询商品失败，itemId：" + itemId);
            throw new NotFoundException("goods.order");
        }
        //2.根据活动id查询活动
        Activity activity = shopService.findByActivityId(activityId);
        if (ObjectUtils.isEmpty(activity)) {
            logger.error("查询活动失败，activityId：" + activityId);
            throw new NotFoundException("activity.order");
        }
        if (goods.getIsSale() == 1) {
            logger.error("商品" + goods.getId() + "已售罄！");
            throw new NotRuleException("isSale.order");
        }
        Integer soldAmount = 0;
        soldAmount = OilOrderUtil.getSoldAmount(itemId, DateUtil.getMonthBegin(), DateUtil.getMonthEnd());
        //3.判断是否超过限售数量
        if (goods.getLimitAmount() != null && goods.getLimitAmount() != 0 && (soldAmount >= goods.getLimitAmount())) {
            logger.error("商品" + goods.getId() + "已超售！");
            throw new NotRuleException("soldOut.order");
        }
        if (activity.getType() == ActivityIdEnum.ACTIVITY_ID_666.getActivity()) {
           long canBuy =orderRepository.countValidOrderByActivityIdAndAccountId(activityId,accountId);
            if (canBuy>0) {
                logger.error("新手活动期间只能买一次");
                throw new NotRuleException("oneMonthOnce.order");
            }
        }
        //4.初始化订单及订单项
        OilOrder oilOrder = new OilOrder(accountId, serialNumber, Constant.ORDER_STATUS_CREATE, Constant.PAY_STATUS_NOT_PAID, new Date(), Constant.REFUND_STATUS_ORIGINAL, itemId, activityId);
        oilOrder = OilOrderUtil.initOrder(oilOrder, goods, activity.getType());
        oilOrder = orderRepository.save(oilOrder);
        //5.生成订单
        long endTime = System.currentTimeMillis();
        logger.info("添加订单成功，goodsId：" + goods.getId() + "|总用时: " + (endTime - startTime));
        return new ResponseEntity<>(oilOrder, HttpStatus.OK);
    }
}
