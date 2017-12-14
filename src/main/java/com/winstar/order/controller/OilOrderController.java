package com.winstar.order.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

        if(activity.getType()==1&&!StringUtils.isEmpty(couponId)){
            throw new NotRuleException("canNotUseCoupon.order");
        }

        //5.初始化订单及订单项
        OilOrder oilOrder = new OilOrder(accountId,serialNumber, Constant.ORDER_STATUS_CREATE,Constant.PAY_STATUS_NOT_PAID,new Date(),Constant.REFUND_STATUS_ORIGINAL,itemId,activityId);
        //4.如果优惠券，查询优惠券
        if(!StringUtils.isEmpty(couponId)){
            MyCoupon myCoupon = couponService.findMyCouponById(couponId,itemId);
            oilOrder.setCouponId(couponId);
            if(ObjectUtils.isEmpty(myCoupon.getAmount())){
                oilOrder.setDiscountAmount(goods.getSaledPrice()*(1-myCoupon.getDiscountRate()));
            }else if (ObjectUtils.isEmpty(myCoupon.getDiscountRate())){
                oilOrder.setDiscountAmount(myCoupon.getAmount());
            }

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

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /* 查询单个订单-根据序列号
     *
     * @param serialNumber 订单序列号
     * @return 订单
     * @throws MissingParameterException
     */
    @RequestMapping(value = "/{serialNumber}/serialNumber", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity getOrders(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if(StringUtils.isEmpty(serialNumber)){
            throw new MissingParameterException("serialNumber.order");
        }
        OilOrder order = orderRepository.findBySerialNo(serialNumber);
        if(ObjectUtils.isEmpty(order)){
            throw  new NotFoundException("oilOrder.order");
        }
        return new ResponseEntity(order,HttpStatus.OK);
    }

    /**
     * 根据条件查询用户订单集合
     */
    @RequestMapping(value = "/{status}/status", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity getOrdersByAccountId(@PathVariable String status, HttpServletRequest request)
            throws NotFoundException, ServiceUnavailableException, NotRuleException, MissingParameterException {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }





    /**
     * 关闭油券订单:只有未付款的订单才能关闭
     * @param serialNumber 订单序列号
     * @return 订单
     * @throws MissingParameterException miss
     * @throws NotRuleException not rule
     */
    @RequestMapping(value = "/shutdown/{serialNumber}/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity shutdownOrder(@PathVariable String serialNumber, HttpServletRequest request ) throws MissingParameterException, NotRuleException, NotFoundException {

        String accountId = (String) request.getHeader("accountId");
        if(StringUtils.isEmpty(accountId)){
            throw new NotFoundException("accountId.oilOrder");
        }
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.oilOrder");
        }

         OilOrder oilOrder = orderRepository.findBySerialNo(serialNumber);
         if(ObjectUtils.isEmpty(oilOrder)){
             throw new NotFoundException("oilOrder.oilOrder");
         }
         if(oilOrder.getStatus()!=1){
             throw new NotRuleException("cannotShutdown.oilOrder");
         }
         if(!oilOrder.getAccountId().equals(accountId)){
             throw new NotRuleException("notYourOrder.oilOrder");
         }
         return new ResponseEntity<>(oilOrder, HttpStatus.OK);

    }


}
