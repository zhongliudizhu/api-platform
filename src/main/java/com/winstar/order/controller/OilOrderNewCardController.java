//package com.winstar.order.controller;
//
//import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
//import com.winstar.cashier.construction.utils.Arith;
//import com.winstar.coupon.entity.MyCoupon;
//import com.winstar.coupon.service.CouponService;
//import com.winstar.couponActivity.repository.CareJuanListRepository;
//import com.winstar.exception.InvalidParameterException;
//import com.winstar.exception.NotFoundException;
//import com.winstar.exception.NotRuleException;
//import com.winstar.order.entity.OilOrder;
//import com.winstar.order.repository.OilOrderRepository;
//import com.winstar.order.utils.Constant;
//import com.winstar.order.utils.DateUtil;
//import com.winstar.order.utils.OilOrderUtil;
//import com.winstar.shop.entity.Activity;
//import com.winstar.shop.entity.Goods;
//import com.winstar.shop.service.ShopService;
//import com.winstar.user.service.AccountService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//
///**
// * Created by qyc on 2018/10/20.
// */
//@RestController
//@RequestMapping("/api/v1/cbc/newCardOrder")
//public class OilOrderNewCardController {
//    public static final Logger logger = LoggerFactory.getLogger(OilOrderNewCardController.class);
//    @Autowired
//    private OilOrderRepository orderRepository;
//    @Autowired
//    private ShopService shopService;
//    @Autowired
//    private CouponService couponService;
//    @Autowired
//    private AccountService accountService;
//    @Autowired
//    CareJuanListRepository careJuanListRepository;
//    @Autowired
//    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;
//
//    @Value("${info.amount}")
//    private Integer amount;
//    /**
//     * 添加油券订单
//     *
//     * @param itemId     商品id
//     * @param activityId 活动id
//     */
//    @PostMapping(produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResponseEntity addOrder(@RequestParam String itemId
//            , @RequestParam String activityId
//            , @RequestParam String couponId
//            , HttpServletRequest request) throws NotFoundException, NotRuleException, InvalidParameterException {
//        String accountId = accountService.getAccountId(request);
//        String serialNumber = OilOrderUtil.getSerialNumber();
//        long startTime = System.currentTimeMillis();
//        //1.是否专属活动
//        if(StringUtils.isEmpty(activityId)||!activityId.equals("105")){
//            throw new NotFoundException("activity.order");
//        }
//        //2.根据商品id 查询商品
//        Goods goods = shopService.findByGoodsId(itemId);
//        logger.info("开始添加订单，goodsId："+goods.getId());
//        if (ObjectUtils.isEmpty(goods)) {
//            logger.error("查询商品失败，itemId：" + itemId);
//            throw new NotFoundException("goods.order");
//        }
//        if (StringUtils.isEmpty(goods.getCouponTempletId()) && !StringUtils.isEmpty(couponId)) {
//            throw new NotRuleException("canNotUseCoupon.order");
//        }
//        //3.根据活动id查询活动
//        Activity activity = shopService.findByActivityId(activityId);
//        if (ObjectUtils.isEmpty(activity)) {
//            logger.error("查询活动失败，activityId：" + activityId);
//            throw new NotFoundException("activity.order");
//        }
//        if (goods.getIsSale() == 1) {
//            logger.error("商品" + goods.getId() + "已售罄！");
//            throw new NotRuleException("isSale.order");
//        }
//        Integer soldAmount = OilOrderUtil.getSoldAmount(itemId, DateUtil.getWeekBegin(), DateUtil.getWeekEnd());
//        //判断是否超过限售数量
//        if (goods.getLimitAmount() != null && goods.getLimitAmount() != 0 && (soldAmount >= goods.getLimitAmount())) {
//            logger.error("商品" + goods.getId() + "已超售！");
//            throw new NotRuleException("soldOut.order");
//        }
//        //4.如果优惠券，查询优惠券
//        OilOrder oilOrder = new OilOrder(accountId, serialNumber, Constant.ORDER_STATUS_CREATE, Constant.PAY_STATUS_NOT_PAID, new Date(), Constant.REFUND_STATUS_ORIGINAL, itemId, activityId);
//        if (!StringUtils.isEmpty(couponId)) {
//            MyCoupon myCoupon = couponService.checkIfMyCouponAvailable(goods.getPrice(), couponId);
//            if (myCoupon == null) {
//                logger.error("根据couponId查询优惠券失败，couponId：" + couponId);
//                throw new NotFoundException("myCoupon");
//            }
//            oilOrder.setCouponId(couponId);
//            if (ObjectUtils.isEmpty(myCoupon.getAmount())) {
//                oilOrder.setDiscountAmount(Arith.mul(goods.getSaledPrice(), Arith.sub(1, myCoupon.getDiscountRate())));
//            } else if (ObjectUtils.isEmpty(myCoupon.getDiscountRate())) {
//                oilOrder.setDiscountAmount(myCoupon.getAmount());
//            }
//        }
//        if (!StringUtils.isEmpty(oilOrder.getCouponId())) {
//            couponService.useCoupon(couponId);
//        }
//        //5.初始化订单及订单项
//        oilOrder = OilOrderUtil.initOrderSubsidy(oilOrder, goods, activity.getType());
//        //6.生成订单
//        oilOrder = orderRepository.save(oilOrder);
//
//        long endTime = System.currentTimeMillis();
//        logger.info("添加订单成功，goodsId：" + goods.getId() + "|总用时: " + (endTime - startTime));
//        return new ResponseEntity<>(oilOrder, HttpStatus.OK);
//    }
//
//}
