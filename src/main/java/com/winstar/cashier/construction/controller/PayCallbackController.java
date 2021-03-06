package com.winstar.cashier.construction.controller;

import com.winstar.cashier.construction.utils.AppUtils;
import com.winstar.cashier.construction.utils.DateUtil;
import com.winstar.cashier.construction.utils.PayConfPC;
import com.winstar.cashier.construction.utils.RequestUtils;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.event.ModifyOrderEvent;
import com.winstar.event.SendOilCouponEvent;
import com.winstar.exception.NotFoundException;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 支付回调
 *
 * @author zl
 */
@RestController
@RequestMapping("api/v1/cbc/payCallback")
@ConfigurationProperties(prefix="info")
public class PayCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(PayCallbackController.class);

    @Autowired
    PayLogRepository payLogRepository;

    @Autowired
    PayOrderRepository payOrderRepository;

    private static Boolean profilesActive;

    @Autowired
    private ApplicationContext context;

    public static void setProfilesActive(Boolean profilesActive) {
        PayCallbackController.profilesActive = profilesActive;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void payCallback(HttpServletRequest request, HttpServletResponse resp) throws Exception{
        resp.setStatus(HttpServletResponse.SC_OK);// 响应清算平台通知
        logger.info("异步回调开始:");
        //获取通知参数
        Map<String, String> respMap = RequestUtils.getReqMap(request);
        logger.info("接收到异步通知报文:" + respMap.toString());
        //签名验证
        boolean validate = AppUtils.validate(respMap, profilesActive ? PayConfPC.PROD_SIGN_KEY : PayConfPC.TEST_SIGN_KEY, respMap.get("charset"));
        if (validate) {
            logger.info("签名验证成功.");
            modifyOrder(respMap,request);
        } else {
            logger.info("签名验证失败.");
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
        PayLog log = new PayLog(MapUtils.getString(reqMap, "orderNumber"),MapUtils.getString(reqMap, "orderAmount"), WsdUtils.getIpAndUrl(request,"/payCallback").get("ip"),WsdUtils.getIpAndUrl(request,"/payCallback").get("applyUrl"),null,code,message,MapUtils.getString(reqMap, "payAmount"),MapUtils.getString(reqMap, "qid"),reqMap.toString());
        payLogRepository.save(log);
    }

    private void modifyOrder(Map<String, String> respMap,HttpServletRequest request) throws Exception{
        PayOrder payOrder = payOrderRepository.findByPayOrderNumber(MapUtils.getString(respMap, "orderNumber"));
        if(WsdUtils.isEmpty(payOrder)){
            savePayLog(respMap,"ERROR","查询订单不存在",request);
            throw new NotFoundException("orderNumber");
        }
        if(payOrder.getCheckState().equals("0") && !payOrder.getState().equals("1")){
            payOrder.setState(MapUtils.getString(respMap, "state"));
            payOrder.setQid(MapUtils.getString(respMap, "qid"));
            payOrder.setUpdaedAt(DateUtil.parseTime(MapUtils.getString(respMap, "orderTime")));
            payOrderRepository.save(payOrder);
            if("1".equals(MapUtils.getString(respMap, "state"))){
                logger.info("订单支付成功.");
                savePayLog(respMap,"OK","订单支付成功",request);
            }else{
                logger.info("订单支付失败.");
                savePayLog(respMap,"ERROR","订单支付失败",request);
            }
            //通知订单那边是否支付成功 ------>>>  回调
            context.publishEvent(new ModifyOrderEvent(this, payOrder));
            //发券
            context.publishEvent(new SendOilCouponEvent(this, payOrder));
        }else{
            savePayLog(respMap,"ERROR","订单已对账！",request);
        }
    }

}