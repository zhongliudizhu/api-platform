package com.winstar.order.controller;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.service.CouponService;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
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
import org.omg.CosNaming.NamingContextPackage.NotFound;
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

    /**
     * 添加油券订单
     * @param itemId 商品id
     * @param activityId 活动id
     * @param couponId 优惠券id
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addOrder(@RequestParam String itemId
            , @RequestParam String activityId
            , @RequestParam(required = false, defaultValue = "") String couponId
            , HttpServletRequest request) throws NotFoundException, NotRuleException {
        String accountId = accountService.getAccountId(request);
        String serialNumber = OilOrderUtil.getSerialNumber();

        //1.根据accountId 查询account
        Account account = accountService.findById(accountId);

        //2.根据商品id 查询商品
        Goods goods = shopService.findByGoodsId(itemId);

        //3.根据活动id查询活动
        Activity activity = shopService.findByActivityId(activityId);

        if(ObjectUtils.isEmpty(activity)){
            throw new NotFoundException("activity.order");
        }
        if(activity.getType()==1&&!StringUtils.isEmpty(couponId)){
            throw new NotRuleException("canNotUseCoupon.order");
        }
        if(activity.getType()==1&&!StringUtils.isEmpty(couponId)){
            throw new NotRuleException("canNotUseCoupon.order");
        }
        if(itemId.equals(Constant.ONE_BUY_ITEMID)){
            if(!OilOrderUtil.isEnable(accountId)){
                throw new NotRuleException("canNotBuy.order");
            }
        }

        //5.初始化订单及订单项
        OilOrder oilOrder = new OilOrder(accountId,serialNumber, Constant.ORDER_STATUS_CREATE,Constant.PAY_STATUS_NOT_PAID,new Date(),Constant.REFUND_STATUS_ORIGINAL,itemId,activityId);
        //4.如果优惠券，查询优惠券
        if(!StringUtils.isEmpty(couponId)){
            MyCoupon myCoupon = couponService.checkIfMyCouponAvailable(itemId, couponId);
            oilOrder.setCouponId(couponId);
            if(ObjectUtils.isEmpty(myCoupon.getAmount())){
                oilOrder.setDiscountAmount(goods.getSaledPrice()*(1-myCoupon.getDiscountRate()));
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
    @RequestMapping(value = "/judge/{serialNumber}/", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
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
    @RequestMapping(value = "/{serialNumber}/serialNumber", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
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
    @RequestMapping(value = "/{status}/status", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
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
    @RequestMapping(value = "/shutdown/{serialNumber}/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
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
