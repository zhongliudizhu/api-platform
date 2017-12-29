package com.winstar.cashier.creditpay.controller;

import com.google.common.collect.Maps;
import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.construction.utils.RequestUtils;
import com.winstar.cashier.creditpay.config.CreditConfig;
import com.winstar.cashier.creditpay.config.RSASig;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.exception.NotFoundException;
import com.winstar.oil.service.SendOilCouponService;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.vo.PayInfoVo;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by zl on 2017/12/28
 */
@RestController
@RequestMapping("api/v1/cbc/creditPay/notify")
public class CreditNotifyController {

    @Autowired
    PayLogRepository payLogRepository;

    @Autowired
    PayOrderRepository payOrderRepository;

    @Autowired
    private OilOrderService orderService;

    @Autowired
    private SendOilCouponService sendOilCouponService;

    private static final Logger logger = LoggerFactory.getLogger(CreditNotifyController.class);

    @RequestMapping(value = "")
    public void payCallback(HttpServletRequest request, HttpServletResponse resp) throws Exception{
        logger.info("异步回调开始:");
        resp.setStatus(HttpServletResponse.SC_OK);// 响应清算平台通知
        //获取通知参数
        Map<String, String> respMap = RequestUtils.getReqMap(request);
        logger.info("接收到异步通知报文:" + respMap.toString());
        //签名验证
        if(RSASig.verifySigature(respMap, CreditConfig.pubkey)){
            respMap.put("qid",MapUtils.getString(respMap, "ORDERID"));
            logger.info("签名验证成功！");
            modifyOrder(respMap,request);
        }else{
            logger.info("验证签名失败！");
            savePayLog(respMap,"ERROR","签名验证失败",request);
        }
        logger.info("接收清算平台通知消息完成.");
    }

    /**
     * 保存访问日志记录
     * @param reqMap 接收的报文
     * @param code  code
     * @param message   message
     * @param request request、获取ip地址和接口地址使用
     */
    private void savePayLog(Map<String, String> reqMap,String code,String message,HttpServletRequest request){
        PayLog log = new PayLog(MapUtils.getString(reqMap, "ORDERID"),MapUtils.getString(reqMap, "PAYMENT"), WsdUtils.getIpAndUrl(request,"/api/v1/cbc/creditPay/notify").get("ip"),WsdUtils.getIpAndUrl(request,"/api/v1/cbc/creditPay/notify").get("applyUrl"),null,code,message,MapUtils.getString(reqMap, "PAYMENT"),MapUtils.getString(reqMap, "qid"),reqMap.toString());
        payLogRepository.save(log);
    }

    private void modifyOrder(Map<String, String> respMap,HttpServletRequest request) throws Exception{
        PayOrder payOrder = payOrderRepository.findByPayOrderNumber(MapUtils.getString(respMap, "ORDERID"));
        if(WsdUtils.isEmpty(payOrder)){
            savePayLog(respMap,"ERROR","查询订单不存在",request);
            throw new NotFoundException("orderNumber");
        }
        if(payOrder.getCheckState().equals("0") && !payOrder.getState().equals("1")){
            payOrder.setState(MapUtils.getString(respMap, "SUCCESS").equals("Y") ? EnumType.PAY_STATE_SUCCESS.valueStr() : EnumType.PAY_STATE_FAIL.valueStr());
            payOrder.setQid(MapUtils.getString(respMap, "qid"));
            payOrder.setUpdaedAt(new Date());
            payOrderRepository.save(payOrder);
            if("Y".equals(MapUtils.getString(respMap, "SUCCESS"))){
                logger.info("订单支付成功.");
                savePayLog(respMap,"OK","订单支付成功",request);
            }else{
                logger.info("订单支付失败.");
                savePayLog(respMap,"ERROR","订单支付失败",request);
            }
            //通知订单那边是否支付成功 ------>>>  回调
            modifyOrder(payOrder,respMap);
            //通知油卡发送
            if(payOrder.getOrderOwner().equals("1") && "Y".equals(MapUtils.getString(respMap, "SUCCESS"))){
                Map<String,String> map = Maps.newHashMap();
                logger.info("开始通知油卡的发送。。。");
                map.put("orderId",payOrder.getOrderNumber());
                OilOrder oilOrder = orderService.getOneOrder(payOrder.getOrderNumber());
                map.put("shopId",WsdUtils.isEmpty(oilOrder) ? null : oilOrder.getItemId());
                map.put("accountId",WsdUtils.isEmpty(oilOrder) ? null : oilOrder.getAccountId());
                sendOilCouponService.checkCard(map);
            }
        }else{
            savePayLog(respMap,"ERROR","订单已对账！",request);
        }
    }

    private void modifyOrder(PayOrder payOrder,Map<String,String> respMap){
        PayInfoVo payInfoVo = new PayInfoVo();
        payInfoVo.setOrderSerialNumber(payOrder.getOrderNumber());
        payInfoVo.setBankSerialNumber(MapUtils.getString(respMap, "qid"));
        payInfoVo.setPayPrice(MapUtils.getDouble(respMap, "PAYMENT"));
        payInfoVo.setPayState(MapUtils.getString(respMap,"SUCCESS").equalsIgnoreCase("Y") ? EnumType.PAY_STATE_SUCCESS.value() : EnumType.PAY_STATE_FAIL.value());
        payInfoVo.setPayType(payOrder.getPayWay());
        payInfoVo.setPayTime(new Date());
        orderService.updateOrderCashier(payInfoVo);
    }

}
