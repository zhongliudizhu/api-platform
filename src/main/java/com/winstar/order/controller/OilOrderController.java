package com.winstar.order.controller;

import com.winstar.exception.*;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderItemsRepository;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.OilOrderUtil;
import com.winstar.order.vo.ResponseVo;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author shoo on 2017/7/7 13:52.
 * --Describe： 油券订单 --
 */
@RestController
@RequestMapping("/api/v1/orders/oilorder")
public class OilOrderController {
    public static final Logger logger = LoggerFactory.getLogger(OilOrderController.class);
    @Autowired
    private OilOrderRepository orderRepository;
    @Autowired
    private OilOrderItemsRepository oilOrderItemsRepository;
    @Value("${api.oilOrder.getGoods.url}")
    private String getGoodsUrl;
    @Value("${api.oilOrder.getAccount.url}")
    private String getAccountUrl;
    @Value("${api.order.updateOilOrder.url}")
    private  String backUrl;
    @Value("${money.environment}")
    private Integer moneyEnvironment;
    @Value("${api.oilOrder.getRule.url}")
    private String getRuleUrl;
    @Value("${api.secKill.getReceiveParam}")
    private String getReceiveParamUrl;
    @Value("${api.order.getCertificate.url}")
    private String getCertificateNumUrl;
    @Value("${api.order.giveCoupon.url}")
    private String giveCouponUrl;
    @Value("${api.getCouponDetail.url}")
    private String getCouponDetailUrl;
    @Value("${api.consumeCoupon.url}")
    private String consumeCouponUrl;
    @Value("${api.getSendAddress.url}")
    private String sendAddressUrl;
    @Value("${oilorder.blacklist}")
    private String blacklist;
    @Value("${oilorder.blacklist.describe}")
    private String blacklistDescribe;

    @Autowired
    private ApplicationEventPublisher publish;

    /**
     * 添加油券订单
     * @param itemId 商品id
     * @param secKillId 秒杀id
     * @param activityId 活动id
     * @param couponId 优惠券id
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addOrder(@RequestParam String itemId
            , @RequestParam(required = false, defaultValue = "") String secKillId
            , @RequestParam(required = false, defaultValue = "") String activityId
            , @RequestParam(required = false, defaultValue = "") String couponId
            , HttpServletRequest request) throws NotFoundException, NotRuleException {
        String serialNo = OilOrderUtil.getSerialNumber();

        return new ResponseEntity<>(null, HttpStatus.OK);
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
         return null;
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
     * 收款后修改订单
     *
     * @param map 所需参数
     * @return result
     */
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> updateOrders(@RequestBody Map<String, String> map) throws InnerServerException, MissingParameterException, InvalidParameterException, NotFoundException {
        String serialNumber = MapUtils.getString(map, "orderNumber");
        String ispay = MapUtils.getString(map, "state");//  0 未支付   1 支付成功   2 失败
        String payprice = MapUtils.getString(map, "payAmount");
        String bankSerialNumber = MapUtils.getString(map, "qid");
        String orderTime = MapUtils.getString(map, "orderTime");
        String payType = MapUtils.getString(map, "payType");
        return null;
    }

    /**
     * 付完款就发货，默认发货成功。发货失败的调用此接口修改发货状态为失败
     */
    @RequestMapping(value = "/sendStatus", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> updateSendStatus(@RequestBody Map<String, String> map) throws InnerServerException, MissingParameterException, InvalidParameterException, NotFoundException {
        return null;
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
