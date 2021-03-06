package com.winstar.cashier.wxpay.controller;

import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.construction.utils.DateUtil;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.cashier.wxpay.common.Signature;
import com.winstar.cashier.wxpay.common.XMLParser;
import com.winstar.cashier.wxpay.config.WsdWechatConfig;
import com.winstar.cashier.wxpay.pay.WxPay;
import com.winstar.event.ModifyOrderEvent;
import com.winstar.event.SendOilCouponEvent;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zl on 2017/3/31
 */
@RestController
@RequestMapping("api/v1/cbc/wxPay/notify")
public class NotifyController {

    private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    private PayOrderRepository payOrderService;

    @Autowired
    private PayLogRepository payLogService;

    @Autowired
    private ApplicationContext context;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public void notify(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception{
        logger.info("开始异步通知。。。。");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(),"utf-8");
        Map<String, Object> map = new HashMap<>();
        try {
            map = XMLParser.getMapFromXML(result);
            for(Object keyValue : map.keySet()){
                logger.info("获取的参数:" + keyValue + "=" + map.get(keyValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取回调参数失败！");
            savePayLog(map,"ERROR","获取回调参数失败！",request);
            return;
        }
        if (map.get("return_code")==null || !map.get("return_code").toString().equalsIgnoreCase("SUCCESS")){
            logger.info(MapUtils.getString(map,"return_msg"));
            savePayLog(map,"ERROR",MapUtils.getString(map,"return_msg"),request);
            return;
        }
        if (map.get("result_code")==null || !map.get("result_code").toString().equalsIgnoreCase("SUCCESS")){
            logger.info(MapUtils.getString(map,"err_code") + "：" + MapUtils.getString(map,"err_code_des"));
            savePayLog(map,"ERROR",MapUtils.getString(map,"err_code") + "：" + MapUtils.getString(map,"err_code_des"),request);
            return;
        }
        PayOrder payOrder = payOrderService.findByPayOrderNumber(MapUtils.getString(map, "out_trade_no"));
        WsdWechatConfig wechatConfig = new WsdWechatConfig();
        PrintWriter out = response.getWriter();
        String sign = Signature.getSign(map,wechatConfig.getSignKey());
        if(sign.equals(MapUtils.getString(map,"sign")) && WsdUtils.isNotEmpty(payOrder) && MapUtils.getString(map,"total_fee").equals(payOrder.getPayAmount())){
            String transaction_id = map.get("transaction_id").toString();
            String nonce_str = map.get("nonce_str").toString();
            //处理业务逻辑
            String str = modifyOrder(payOrder,map,request);
            if("0000".equals(str)){
                out.write(this.setXML("SUCCESS", "OK"));
            }else{
                //如果系统发生异常，查询订单是否支付成功
                if(WxPay.getTradeByReturn(nonce_str, transaction_id, wechatConfig, payOrder.getSubPayWay())){
                    out.write(this.setXML("SUCCESS", "OK"));
                }
            }
        }else{
            logger.info("签名错误！");
            savePayLog(map,"ERROR","签名错误！",request);
            out.write(this.setXML("FAIL","签名错误！"));
        }
    }

    private String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

    /**
     * 保存访问日志记录
     * @param reqMap 接收的报文
     * @param code  code
     * @param message   message
     * @param request request、获取ip地址和接口地址使用
     */
    private void savePayLog(Map<String, Object> reqMap,String code,String message,HttpServletRequest request){
        PayLog log = new PayLog(MapUtils.getString(reqMap, "out_trade_no"),MapUtils.getString(reqMap, "total_fee"), WsdUtils.getIpAndUrl(request,"/wxPay/notify").get("ip"),WsdUtils.getIpAndUrl(request,"/wxPay/notify").get("applyUrl"),null,code,message,MapUtils.getString(reqMap, "total_fee"),MapUtils.getString(reqMap, "transaction_id"),reqMap.toString());
        payLogService.save(log);
    }

    /**
     * 修改订单状态
     * @param respMap 回调的参数
     * @param request request
     */
    private String modifyOrder(PayOrder payOrder,Map<String, Object> respMap,HttpServletRequest request){
        try{
            long beginTime = System.currentTimeMillis();
            if(WsdUtils.isNotEmpty(payOrder) && payOrder.getState().equals(EnumType.PAY_STATE_SUCCESS.valueStr())){
                return "0000";
            }
            if(WsdUtils.isNotEmpty(payOrder) && payOrder.getCheckState().equals("0")){
                payOrder.setState(MapUtils.getString(respMap,"result_code").equalsIgnoreCase("SUCCESS") ? EnumType.PAY_STATE_SUCCESS.valueStr() : EnumType.PAY_STATE_FAIL.valueStr());
                payOrder.setQid(MapUtils.getString(respMap, "transaction_id"));
                payOrder.setUpdaedAt(DateUtil.parseTime(MapUtils.getString(respMap, "time_end")));
                String couponId = MapUtils.getString(respMap, "coupon_id_0");
                if(!StringUtils.isEmpty(couponId)){
                    payOrder.setCouponId(couponId);
                }
                String couponFee = MapUtils.getString(respMap, "coupon_fee");
                if(!StringUtils.isEmpty(couponFee)){
                    payOrder.setCouponFee(couponFee);
                }
                payOrder.setBankType(MapUtils.getString(respMap, "bank_type"));
                payOrderService.save(payOrder);
                logger.info("订单支付成功...");
                savePayLog(respMap,"OK","订单支付成功",request);
                long endTime = System.currentTimeMillis();
                logger.info("修改支付订单消耗时间：" + (endTime - beginTime) + "ms，支付订单号：" + payOrder.getPayOrderNumber() + "，订单号：" + payOrder.getOrderNumber());
                //通知订单那边是否支付成功 ------>>>  回调
                context.publishEvent(new ModifyOrderEvent(this, payOrder));
                //发券
                context.publishEvent(new SendOilCouponEvent(this, payOrder));
            }else{
                savePayLog(respMap,"ERROR","查询订单不存在",request);
            }
        }catch (Exception e){
            return "0002";
        }
        return "0000";
    }

}
