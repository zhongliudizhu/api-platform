package com.winstar.cashier.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.winstar.carLifeMall.entity.CarLifeOrders;
import com.winstar.carLifeMall.service.CarLifeOrdersService;
import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.construction.sample.PayMoney;
import com.winstar.cashier.creditpay.pay.CreditPay;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.cashier.wxpay.pay.WxPay;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.service.OilOrderService;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2017/7/14
 */
@RestController
@RequestMapping("/api/v1/cbc/payOrder")
public class WsdPayController {

    private static final Logger logger = LoggerFactory.getLogger(WsdPayController.class);

    @Autowired
    private PayLogRepository payLogService;

    @Autowired
    private PayOrderRepository payOrderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OilOrderService orderService;

    @Autowired
    private CarLifeOrdersService carLifeOrdersService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity payOrderUrl(
        @RequestBody Map map,
        HttpServletRequest request
    )  throws Exception {
        Map<String,Object> payMap = validator(request,map);
        logger.info("payMap:" + JSON.toJSONString(payMap));
        String orderNumber = MapUtils.getString(payMap,"orderNumber");
        String bankCode = MapUtils.getString(payMap,"bankCode");
        String applyUrl = MapUtils.getString(payMap,"applyUrl");
        String ip = MapUtils.getString(payMap,"ip");
        String orderOwner = MapUtils.getString(payMap, "orderOwner");
        long beginTime = System.currentTimeMillis();
        Double payPrice;
        if(orderOwner.equals(EnumType.PAY_SHOPNAME_CARSERVICE.valueStr())){
            CarLifeOrders carLifeOrders = carLifeOrdersService.getCarLifeOrdersBySerialNo(orderNumber);
            if(WsdUtils.isEmpty(carLifeOrders)){
                logger.info(orderNumber + "，汽车服务订单不存在！");
                throw new NotFoundException("orderNumber");
            }
            if(carLifeOrders.getIsAvailable().equals("1")){
                logger.info(orderNumber + "，汽车服务订单已失效！");
                throw new NotFoundException("orderNumber");
            }
            payPrice = carLifeOrders.getPayPrice();
        }else{
            OilOrder oilOrder = orderService.getOneOrder(orderNumber);
            if(WsdUtils.isEmpty(oilOrder)){
                logger.info(orderNumber + "，油卡服务订单不存在！");
                throw new NotFoundException("orderNumber");
            }
            if(oilOrder.getIsAvailable().equals("1")){
                logger.info(orderNumber + "，油卡服务订单已失效！");
                throw new NotFoundException("orderNumber");
            }
            payPrice = oilOrder.getPayPrice();
        }
        //判断订单是否是否支付成功过
        List<PayOrder> orders = payOrderService.findByOrderNumberAndState(orderNumber, EnumType.PAY_STATE_SUCCESS.valueStr());
        if(WsdUtils.isNotEmpty(orders) && orders.size() > 0){
            PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR",orderNumber + "订单已经支付！");
            payLogService.save(log);
            logger.info(orderNumber + "订单已经支付！");
            throw new InvalidParameterException("orderNumber已支付");
        }
        //查询订单对象、订单金额，裁决状态，订单来源，回调订单地址
        payMap.put("orderAmount",payPrice);
        if(bankCode.equals(EnumType.PAY_BANKCODE_UNIONPAY.valueStr())){
            return null;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_ALIPAY.valueStr())){
            return null;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_WECHAT.valueStr())){
            return WxPay.getPayParams(payMap,request,payOrderService,payLogService);
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION.valueStr())){
            return PayMoney.pay(payMap,request,payOrderService,payLogService);
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr())){
            ResponseEntity entity = CreditPay.pay(payMap,request,payOrderService,payLogService);
            long endTime = System.currentTimeMillis();
            logger.info("信用卡整体消耗时间：" + (endTime - beginTime) + "ms");
            return entity;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_DEBIT.valueStr())){
            ResponseEntity entity = CreditPay.pay(payMap,request,payOrderService,payLogService);
            long endTime = System.currentTimeMillis();
            logger.info("储蓄卡整体消耗时间：" + (endTime - beginTime) + "ms");
            return entity;
        }
        return null;
    }

    private Map<String,Object> validator(HttpServletRequest request, Map map) throws MissingParameterException, InvalidParameterException, NotFoundException, NotRuleException {
        Map<String,Object> payMap = Maps.newHashMap();
        String accountId = accountService.getAccountId(request);
        String orderNumber = MapUtils.getString(map,"orderNumber");
        String frontEndUrl = MapUtils.getString(map,"frontEndUrl");
        String bankCode = WsdUtils.isEmpty(MapUtils.getString(map,"bankCode")) ? EnumType.PAY_BANKCODE_CONDTRUCTION.valueStr() : MapUtils.getString(map,"bankCode");
        String subBankCode = MapUtils.getString(map,"subBankCode");
        String source = WsdUtils.isEmpty(MapUtils.getString(map,"source")) ? EnumType.SOURCE_WECHAT.valueStr() : MapUtils.getString(map,"source");
        //1:油卡缴费、2：保险缴费
        String paymentType = WsdUtils.isEmpty(MapUtils.getString(map,"paymentType")) ? "1" : MapUtils.getString(map,"paymentType");
        logger.info("accountId:" + accountId + ",orderNumber:" + orderNumber + ",frontEndUrl:" + frontEndUrl + ",bankCode:" + bankCode + ",subBankCode:" + subBankCode + ",source:" + source + ",paymentType:" + paymentType);
        //获取访问的接口地址
        String applyUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1/cashier/payOrder";
        String ip = WsdUtils.getIpAddress(request);
        //校验数据
        if(StringUtils.isEmpty(orderNumber)){
            PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR","orderNumber为空");
            payLogService.save(log);
            logger.info("orderNumber 为空!");
            throw new MissingParameterException("orderNumber");
        }
        if(orderNumber.length() > 32){
            PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR","orderNumber长度不能超过32字符!");
            payLogService.save(log);
            logger.info("orderNumber 长度不能超过32字符!");
            throw new InvalidParameterException("orderNumber");
        }
        if(!bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION.valueStr())
                && !bankCode.equals(EnumType.PAY_BANKCODE_UNIONPAY.valueStr())
                && !bankCode.equals(EnumType.PAY_BANKCODE_ALIPAY.valueStr())
                && !bankCode.equals(EnumType.PAY_BANKCODE_WECHAT.valueStr())
                && !bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr())
                && !bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_DEBIT.valueStr())){
            logger.info("bankCode 参数不合法!");
            PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR","支付方式不合法!");
            payLogService.save(log);
            throw new InvalidParameterException("bankCode");
        }
        subBankCode = setPayWay(payMap,bankCode,subBankCode,accountId,map,source);
        if(bankCode.equals(EnumType.PAY_BANKCODE_WECHAT.valueStr())){
            if(!subBankCode.equals(EnumType.PAY_WAY_WEIXIN.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_WEIXIN_CARD.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_WEIXIN_H5.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_WEIXIN_SM.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_WEIXIN_APP.valueStr())
            ){
                logger.info("subBankCode 参数不合法!");
                PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR","微信支付子方式不合法!");
                payLogService.save(log);
                throw new InvalidParameterException("subBankCode");
            }
        }
        if(bankCode.equals(EnumType.PAY_BANKCODE_ALIPAY.valueStr())){
            if(!subBankCode.equals(EnumType.PAY_WAY_ALIPAY.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_ALIPAY_FACE.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_ALIPAY_HB.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_ALIPAY_MOBILE.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_ALIPAY_PC.valueStr()) &&
                !subBankCode.equals(EnumType.PAY_WAY_ALIPAY_APP.valueStr())
            ){
                logger.info("subBankCode 参数不合法!");
                PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR","支付宝支付子方式不合法!");
                payLogService.save(log);
                throw new InvalidParameterException("subBankCode");
            }
        }
        if(!source.equals(EnumType.SOURCE_WECHAT.valueStr())
                && !source.equals(EnumType.SOURCE_ANDROID.valueStr())
                && !source.equals(EnumType.SOURCE_IOS.valueStr())
                && !source.equals(EnumType.SOURCE_XIAOCHENGXU.valueStr())
                && !source.equals(EnumType.SOURCE_PC.valueStr())){
            PayLog log = new PayLog(orderNumber,"",ip,applyUrl,"","ERROR","来源不合法!");
            payLogService.save(log);
            logger.info("source 参数不合法!");
            throw new InvalidParameterException("source");
        }
        setPayOrderName(payMap,paymentType);
        setOpenId(payMap,subBankCode,accountId);
        payMap.put("userId", accountId);
        payMap.put("orderNumber",orderNumber);
        payMap.put("bankCode",bankCode);
        payMap.put("frontEndUrl",frontEndUrl);
        payMap.put("applyUrl",applyUrl);
        payMap.put("ip", ip);
        payMap.put("source", source);
        payMap.put("orderOwner",paymentType);
        payMap.put("subPayWay", subBankCode);
        return payMap;
    }

    private String setPayWay(Map<String,Object> payMap,String bankCode,String subBankCode,String accountId,Map map,String source){
        if(bankCode.equals(EnumType.PAY_BANKCODE_UNIONPAY.valueStr())){
            payMap.put("payWay", EnumType.PAY_WAY_UNIONPAY.value());
            payMap.put("cardNumber", MapUtils.getString(map,"cardNumber"));
            if(WsdUtils.isNotEmpty(MapUtils.getString(map,"cardNumber"))){
                payMap.put("userId", MapUtils.getString(map,"cardNumber") + (WsdUtils.isNotEmpty(accountId) ? "," + accountId : ""));
            }
            subBankCode = WsdUtils.isEmpty(subBankCode) ? EnumType.PAY_WAY_UNIONPAY.valueStr() : subBankCode;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_WECHAT.valueStr())){
            payMap.put("payWay", EnumType.PAY_WAY_WEIXIN.valueStr());
            if(WsdUtils.isEmpty(subBankCode)){
                subBankCode = !source.equals(EnumType.SOURCE_WECHAT.valueStr()) ? EnumType.PAY_WAY_WEIXIN_APP.valueStr() : EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr();
            }
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_ALIPAY.valueStr())){
            payMap.put("payWay", EnumType.PAY_WAY_ALIPAY.valueStr());
            subBankCode = WsdUtils.isEmpty(subBankCode) ? EnumType.PAY_WAY_ALIPAY_APP.valueStr() : subBankCode;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION.valueStr())){
            payMap.put("payWay", EnumType.PAY_WAY_CONSTRUCTION.value());
            subBankCode = WsdUtils.isEmpty(subBankCode) ? EnumType.PAY_WAY_CONSTRUCTION.valueStr() : subBankCode;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr())){
            payMap.put("payWay", EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr());
            subBankCode = WsdUtils.isEmpty(subBankCode) ? EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr() : subBankCode;
        }else if(bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_DEBIT.valueStr())){
            payMap.put("payWay", EnumType.PAY_BANKCODE_CONDTRUCTION_DEBIT.valueStr());
            subBankCode = WsdUtils.isEmpty(subBankCode) ? EnumType.PAY_BANKCODE_CONDTRUCTION_DEBIT.valueStr() : subBankCode;
        }
        return subBankCode;
    }

    private void setPayOrderName(Map<String,Object> payMap,String paymentType){
        switch (paymentType) {
            case "1":
                payMap.put("payOrderName", EnumType.PAY_SHOPNAME_OILCARD.valueStr());
                break;
            case "2":
                payMap.put("payOrderName", EnumType.PAY_SHOPNAME_SAFE.valueStr());
                break;
            case "3":
                payMap.put("payOrderName", EnumType.PAY_SHOPNAME_CHARGE.valueStr());
                break;
            case "4":
                payMap.put("payOrderName", EnumType.PAY_SHOPNAME_VALIDATECAR.valueStr());
                break;
            case "5":
                payMap.put("payOrderName", EnumType.PAY_SHOPNAME_CARSERVICE.valueStr());
                break;
        }
    }

    private void setOpenId(Map<String,Object> payMap,String subBankCode,String accountId) throws NotFoundException{
        if(subBankCode.equals(EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr())){
            Account account = accountService.findById(accountId);
            logger.info("微信公众号支付openId:" + (WsdUtils.isEmpty(account) ? null : account.getOpenid()));
            payMap.put("openId",WsdUtils.isEmpty(account) ? null : account.getOpenid());
            //payMap.put("openId","olQf5t8qj6zXhs4Idms7RfbNa5ek");
        }
    }

}
