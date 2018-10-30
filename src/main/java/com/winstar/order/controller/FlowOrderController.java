package com.winstar.order.controller;

import com.winstar.cashier.construction.utils.Arith;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.service.CouponService;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.order.entity.FlowOrder;
import com.winstar.order.repository.FlowOrderRepository;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.FlowOrderUtil;
import com.winstar.order.vo.FlowResult;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
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
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author shoo on 2018/1/24 9:44.
 *         -- 流量充值
 */
@RestController
@RequestMapping(value = "/api/v1/cbc/flow/order")
public class FlowOrderController {

    public static final Logger logger = LoggerFactory.getLogger(FlowOrderController.class);
    @Autowired
    private FlowOrderRepository flowOrderRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private AccountService accountService;

    /**
     *
     * @param itemId 商品id
     * @param activityId 活动id
     * @param couponId 优惠券id（可选）
     * @param phoneNumber 手机号
     */
    @PostMapping(produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addFlowOrder(@RequestParam String itemId
            , @RequestParam String activityId
            , @RequestParam(required = false, defaultValue = "") String couponId
            , @RequestParam String phoneNumber
            , HttpServletRequest request) throws NotRuleException, NotFoundException {

        String accountId = accountService.getAccountId(request);
        String serialNumber = FlowOrderUtil.getSerialNumber();
        //2.根据商品id 查询商品
        Goods goods = shopService.findByGoodsId(itemId);
        if(ObjectUtils.isEmpty(goods)){
            logger.error("查询商品失败，itemId：" + itemId);
            throw new NotFoundException("goods.flowOrder");
        }
        //3.根据活动id查询活动
        Activity activity = shopService.findByActivityId(activityId);
        if(ObjectUtils.isEmpty(activity)){
            logger.error("查询活动失败，activityId：" + activityId);
            throw new NotFoundException("activity.flowOrder");
        }
        if(goods.getType()!=2&&!StringUtils.isEmpty(couponId)){
            logger.error("该商品不能使用优惠券！" );
            throw new NotRuleException("canNotUseCoupon.flowOrder");
        }
        if(goods.getType()==1){
            String canBuy = FlowOrderUtil.judgeItemId(accountId,goods.getId());
            if(canBuy.equals("1")){
                logger.info("该类商品一月只能购买一次");
                throw new NotRuleException("oneMonthOnce.flowOrder");
            }else if(canBuy.equals("2")){
                logger.info("你账户下有未付款且未关闭订单");
                throw new NotRuleException("haveNotPay.flowOrder");
            }
        }
        FlowOrder flowOrder = new FlowOrder(accountId, serialNumber, Constant.ORDER_STATUS_CREATE, Constant.PAY_STATUS_NOT_PAID,new Date(),Constant.REFUND_STATUS_ORIGINAL,itemId,activityId);
        flowOrder.setPhoneNo(phoneNumber);
        //4.如果优惠券，查询优惠券
        if(!StringUtils.isEmpty(couponId)){
            MyCoupon myCoupon = couponService.checkIfMyCouponAvailable(goods.getSaledPrice(), couponId);
            if(myCoupon == null) {
                logger.error("根据couponId查询优惠券失败，couponId：" + couponId);
                throw new NotFoundException("myCoupon.flowOrder");

            }
            flowOrder.setCouponId(couponId);
            if(ObjectUtils.isEmpty(myCoupon.getAmount())){
                flowOrder.setDiscountAmount(Arith.mul(goods.getSaledPrice(),Arith.sub(1,myCoupon.getDiscountRate())));
            }else if (ObjectUtils.isEmpty(myCoupon.getDiscountRate())){
                flowOrder.setDiscountAmount(myCoupon.getAmount());
            }
        }
        if(!StringUtils.isEmpty(flowOrder.getCouponId())){
            couponService.useCoupon(couponId);
        }
        flowOrder = FlowOrderUtil.initFlowOrder(flowOrder,goods);
        flowOrder = flowOrderRepository.save(flowOrder);

        return new ResponseEntity<>(flowOrder, HttpStatus.OK);
    }

    /**
     * 判断订单是否能继续支付
     * @param serialNumber 订单序列号
     */
    @GetMapping(value = "/judge/{serialNumber}/", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity judgeOrder(@PathVariable String serialNumber, HttpServletRequest request) throws MissingParameterException, NotRuleException, NotFoundException {
        if(StringUtils.isEmpty(serialNumber)){
            throw new MissingParameterException("serialNumber.flowOrder");
        }
        FlowOrder order = flowOrderRepository.findBySerialNumber(serialNumber);
        if(ObjectUtils.isEmpty(order)){
            throw new NotFoundException("oilOrder.flowOrder");
        }
        if(order.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
            throw  new NotRuleException("closed.flowOrder");
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
            throw new MissingParameterException("serialNumber.flowOrder");
        }
        FlowOrder order = flowOrderRepository.findBySerialNumber(serialNumber);
        if(ObjectUtils.isEmpty(order)){
            throw  new NotFoundException("oilOrder.flowOrder");
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
            throw new NotFoundException("accountId.flowOrder");
        }
        if(StringUtils.isEmpty(status)){
            throw new MissingParameterException("status.flowOrder");
        }
        Integer orderStatus = Integer.parseInt(status);
        List<FlowOrder> oilOrders = flowOrderRepository.findByAccountId(accountId);
        if(-1 == orderStatus){
            oilOrders = oilOrders.stream().filter(o -> o.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)).collect(toList());
        }else if( 0 == orderStatus){

        }else{
            oilOrders = oilOrders.stream().filter( o -> o.getStatus()==orderStatus).filter(o -> o.getIsAvailable().equals("0")).collect(toList());
        }
        if(oilOrders.size()<=0){
            throw new NotFoundException("orders.flowOrder");
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
            throw new NotFoundException("accountId.flowOrder");
        }
        if (StringUtils.isEmpty(serialNumber)) {
            throw new MissingParameterException("serialNumber.flowOrder");
        }

        FlowOrder order = flowOrderRepository.findBySerialNumber(serialNumber);
        if(ObjectUtils.isEmpty(order)){
            throw new NotFoundException("oilOrder.flowOrder");
        }
        if(order.getStatus()!=1){
            throw new NotRuleException("cannotShutdown.flowOrder");
        }
        if(order.getIsAvailable().equals(Constant.IS_NORMAL_CANCELED)){
            throw new NotRuleException("alreadyClosed.flowOrder");
        }
        order.setIsAvailable(Constant.IS_NORMAL_CANCELED);
        order = flowOrderRepository.save(order);
        //返还优惠券
        if(!StringUtils.isEmpty(order.getCouponId())){
            couponService.cancelMyCoupon(order.getCouponId());
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @GetMapping(produces = "application/json;charset=utf-8",value = "/test")
    @ResponseBody
    public ResponseEntity test(){

        FlowResult flow = FlowOrderUtil.chargeFlow("13572466259","M","10","P","30","http://192.168.118.7:2300/api/v1/flow/order");
        return new ResponseEntity<>(flow,HttpStatus.OK);
    }


}
