package com.winstar.order.controller;

import com.winstar.cashier.construction.utils.Arith;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.service.CouponService;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.OilOrderUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
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
 *  油券订单
 */
@RestController
@RequestMapping("/api/v1/cbc/orders")
public class OilOrderController {
    public static final Logger logger = LoggerFactory.getLogger(OilOrderController.class);
    @Autowired
    private OilOrderRepository orderRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private AccountService accountService;
    @Value("${info.amount}")
    private Integer amount;
    /**
     * 添加油券订单
     * @param itemId 商品id
     * @param activityId 活动id
     * @param couponId 优惠券id
     */
    @PostMapping( produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addOrder(@RequestParam String itemId
            , @RequestParam String activityId
            , @RequestParam(required = false, defaultValue = "") String couponId
            , HttpServletRequest request) throws NotFoundException, NotRuleException {
        String accountId = accountService.getAccountId(request);
        Account account = accountService.findById(accountId);
        String serialNumber = OilOrderUtil.getSerialNumber();
        //2.根据商品id 查询商品
        Goods goods = shopService.findByGoodsId(itemId);
        if(ObjectUtils.isEmpty(goods)){
            logger.error("查询商品失败，itemId：" + itemId);
            throw new NotFoundException("goods.order");
        }

        Integer soldAmount = OilOrderUtil.getSoldAmount(goods.getPrice());

        /*if(goods.getPrice()==100){
           if(soldAmount>13900){
              throw new NotRuleException("soldOut.order");
           }
        }
        if(goods.getPrice()==200){
            if(soldAmount>6900){
                throw new NotRuleException("soldOut.order");
            }
        }
        if(goods.getPrice()==300){
            if(soldAmount>4567){
                throw new NotRuleException("soldOut.order");
            }
        }
        if(goods.getPrice()==500){
            if(soldAmount>2700){
                throw new NotRuleException("soldOut.order");
            }
        }*/
        if(goods.getPrice()==1000){
            if(soldAmount>1300){
                throw new NotRuleException("soldOut.order");
            }
        }
        if(goods.getPrice()==2000){
            if(soldAmount>600){
                throw new NotRuleException("soldOut.order");
            }
        }


        //3.根据活动id查询活动
        Activity activity = shopService.findByActivityId(activityId);
        if(ObjectUtils.isEmpty(activity)){
            logger.error("查询活动失败，activityId：" + activityId);
            throw new NotFoundException("activity.order");
        }
       /* // 活动一：判断每日每个商品只能前一百名购买
        if(activity.getType()==1){
            if(!OilOrderUtil.judgeOneDay(itemId,amount)){
               throw new NotRuleException("oneDay100.order");
            }
        }*/

        if(activity.getType()!=2&&!StringUtils.isEmpty(couponId)){
            logger.error("只有活动2能使用优惠券！" );
            throw new NotRuleException("canNotUseCoupon.order");
        }
        if(StringUtils.isEmpty(goods.getCouponTempletId())&&!StringUtils.isEmpty(couponId)){
            throw new NotRuleException("canNotUseCoupon.order");
        }
        //activity 1 and 3 auth infoCard
        if(activityId.equals("1") || activityId.equals("3")){
            if(StringUtils.isEmpty(account.getAuthInfoCard())){
                throw new NotRuleException("notBindInfoCard.order");
            }
        }
        if(itemId.equals(Constant.ONE_BUY_ITEMID)){
            if(soldAmount>8321){
                throw new NotRuleException("soldOut.order");
            }
            String isEnable = OilOrderUtil.isEnable(accountId);
            if(isEnable.equals("500")){
                logger.info("one:todayMoreThan500");
                throw new NotRuleException("todayMoreThan500.order");
            }else if(isEnable.equals("1")){
                logger.info("one:oneMonthOnce");
                throw new NotRuleException("oneMonthOnce.order");
            }else if(isEnable.equals("2")){
                logger.info("one:haveNotPay");
                throw new NotRuleException("haveNotPay.order");
            }
        }
        if(activityId.equals(Constant.CBC_ACTIVITY_FIR)){
            String canBuy = OilOrderUtil.judgeActivity(accountId,activityId);
            if(canBuy.equals("1")){
                logger.error("活动一商品，每用户一个月只能买一次" );
                throw new NotRuleException("oneMonthOnce.order");
            }else if(canBuy.equals("2")){
                logger.error("活动一商品，有未关闭订单" );
                throw new NotRuleException("haveNotPay.order");
            }
        }

        //5.初始化订单及订单项
        OilOrder oilOrder = new OilOrder(accountId,serialNumber, Constant.ORDER_STATUS_CREATE,Constant.PAY_STATUS_NOT_PAID,new Date(),Constant.REFUND_STATUS_ORIGINAL,itemId,activityId);
        //4.如果优惠券，查询优惠券
        if(!StringUtils.isEmpty(couponId)){
            MyCoupon myCoupon = couponService.checkIfMyCouponAvailable(goods.getPrice(), couponId);
            if(myCoupon == null) {
                logger.error("根据couponId查询优惠券失败，couponId：" + couponId);
                throw new NotFoundException("myCoupon");

            }
            oilOrder.setCouponId(couponId);
            if(ObjectUtils.isEmpty(myCoupon.getAmount())){
                oilOrder.setDiscountAmount(Arith.mul(goods.getSaledPrice(),Arith.sub(1,myCoupon.getDiscountRate())));
            }else if (ObjectUtils.isEmpty(myCoupon.getDiscountRate())){
                oilOrder.setDiscountAmount(myCoupon.getAmount());
            }

        }
        if(!StringUtils.isEmpty(oilOrder.getCouponId())){
            couponService.useCoupon(couponId);
        }
        oilOrder = OilOrderUtil.initOrder(oilOrder,goods,activity.getType());
        oilOrder = orderRepository.save(oilOrder);
        //6.生成订单
        return new ResponseEntity<>(oilOrder, HttpStatus.OK);
    }

    /**
     * 判断订单是否能继续支付
     * @param serialNumber 订单序列号
     */
    @GetMapping(value = "/judge/{serialNumber}/", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity judgeOrder(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if(StringUtils.isEmpty(serialNumber)){
            throw new MissingParameterException("serialNumber.order");
        }
        OilOrder order = orderRepository.findBySerialNumber(serialNumber);
        if(ObjectUtils.isEmpty(order)){
            throw new NotFoundException("oilOrder.order");
        }
        if(order.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
            throw  new NotRuleException("closed.order");
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /* *
     * 查询单个订单-根据序列号
     */
    @GetMapping(value = "/{serialNumber}/serialNumber", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity getOrders(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if(StringUtils.isEmpty(serialNumber)){
            throw new MissingParameterException("serialNumber.order");
        }
        OilOrder order = orderRepository.findBySerialNumber(serialNumber);
        if(ObjectUtils.isEmpty(order)){
            throw  new NotFoundException("oilOrder.order");
        }
        return new ResponseEntity(order,HttpStatus.OK);
    }

    /**
     * 根据条件查询用户订单集合
     *  0 全部  -1 已取消  1 待支付  3 已完成
     */
    @GetMapping(value = "/{status}/status", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity getOrdersByAccountId(@PathVariable String status, HttpServletRequest request)
            throws NotFoundException, ServiceUnavailableException, NotRuleException, MissingParameterException {
        String accountId = accountService.getAccountId(request);
        if(StringUtils.isEmpty(accountId)){
            throw new NotFoundException("accountId.oilOrder");
        }
        if(StringUtils.isEmpty(status)){
            throw new MissingParameterException("status.oilOrder");
        }
        Integer orderStatus = Integer.parseInt(status);
        List<OilOrder> oilOrders = orderRepository.findByAccountId(accountId);
        if(-1 == orderStatus){
            oilOrders = oilOrders.stream().filter(o -> o.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)).collect(toList());
        }else if( 0 == orderStatus){

        }else{
            oilOrders = oilOrders.stream().filter( o -> o.getStatus()==orderStatus).filter(o -> o.getIsAvailable().equals("0")).collect(toList());
        }
        if(oilOrders.size()<=0){
            throw new NotFoundException("orders.order");
        }
        return new ResponseEntity<>(oilOrders, HttpStatus.OK);
    }

    /**
     * 关闭油券订单:只有未付款的订单才能关闭
     * @param serialNumber 订单序列号
     * @return 订单
     */
    @PutMapping(value = "/shutdown/{serialNumber}/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity shutdownOrder(@PathVariable String serialNumber, HttpServletRequest request ) throws MissingParameterException, NotRuleException, NotFoundException {
         String accountId = accountService.getAccountId(request);
         if(StringUtils.isEmpty(accountId)){
             throw new NotFoundException("accountId.oilOrder");
         }
         if (StringUtils.isEmpty(serialNumber)) {
             throw new MissingParameterException("serialNumber.oilOrder");
         }

         OilOrder oilOrder = orderRepository.findBySerialNumber(serialNumber);
         if(ObjectUtils.isEmpty(oilOrder)){
             throw new NotFoundException("oilOrder.oilOrder");
         }
         if(oilOrder.getStatus()!=1){
             throw new NotRuleException("cannotShutdown.oilOrder");
         }
         if(oilOrder.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
             throw new NotRuleException("alreadyClosed.oilOrder");
         }
         if(!oilOrder.getAccountId().equals(accountId)){
             throw new NotRuleException("notYourOrder.oilOrder");
         }
         oilOrder.setIsAvailable(Constant.IS_NORMAL_CANCELED);
         oilOrder = orderRepository.save(oilOrder);
         //返还优惠券
         if(!StringUtils.isEmpty(oilOrder.getCouponId())){
             couponService.cancelMyCoupon(oilOrder.getCouponId());
         }
         return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
