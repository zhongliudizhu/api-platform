package com.winstar.cashier.wxpay.pay;

import com.alibaba.fastjson.JSON;
import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.construction.utils.DateUtil;
import com.winstar.cashier.construction.utils.PayUtils;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.cashier.wxpay.common.Signature;
import com.winstar.cashier.wxpay.common.XMLParser;
import com.winstar.cashier.wxpay.config.WsdWechatConfig;
import com.winstar.cashier.wxpay.sdk.PayReqData;
import com.winstar.cashier.wxpay.sdk.PayService;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zl on 2017/3/31
 */
@Component
@ConfigurationProperties(prefix="info")
public class WxPay {

    private static final Logger logger = LoggerFactory.getLogger(WxPay.class);

    private static Boolean profilesActive;

    public static void setProfilesActive(Boolean profilesActive) {
        WxPay.profilesActive = profilesActive;
    }

    private static String trade_type_APP = "APP";

    private static String trade_type_JSAPI = "JSAPI";

    private static String trade_type_H5 = "MWEB";

    public static ResponseEntity getPayParams(Map<String,Object> payMap, HttpServletRequest request, PayOrderRepository payOrderService, PayLogRepository payLogService) throws Exception{
        long startTime = new Date().getTime();
        payMap.put("payOrderNumber", DateUtil.currentTimeToSS() + WsdUtils.getRandomNumber(8));
        logger.info("微信支付开始。。。。。。");
        WxPayParams wxPayParams = new WxPayParams();
        String nonce_str = WsdUtils.generateRandomCharAndNumber(16);
        Long timeStamp = new Date().getTime() / 1000;
        WsdWechatConfig config = new WsdWechatConfig();
        PayReqData data = getPayReqData(payMap,nonce_str,request,config);
        //请求数据获取结果
        String str = PayService.request(data);
        logger.info("支付结果：" + str);
        Map<String,Object> map = XMLParser.getMapFromXML(str);
        logger.info("支付结果Map类型：" + JSON.toJSONString(map));
        //验证是否成功
        if("SUCCESS".equals(map.get("result_code")) && "SUCCESS".equals(map.get("return_code"))){
            wxPayParams.setPrepayid((String)map.get("prepay_id"));
            wxPayParams.setNonceStr(nonce_str);
            wxPayParams.setTimeStamp(timeStamp + "");
            wxPayParams.setSign(getSignStr(map,timeStamp,nonce_str,config.getSignKey(),MapUtils.getString(payMap,"subPayWay")));
            wxPayParams.setSignType(data.getSign_type());
            wxPayParams.setPartnerid(config.getMchId());
            wxPayParams.setAppid(data.getAppid());
            wxPayParams.setWeb_url(MapUtils.getString(map,"mweb_url"));
            logger.info("返回参数：" + JSON.toJSONString(wxPayParams));
            payOrderService.save(getPayOrder(payMap,request));
        }else{
            Map<String,Object> failMap = XMLParser.getMapFromXML(str);
            wxPayParams.setErrorMsg("获取支付参数失败原因：" + failMap.get("return_msg"));
            logger.info("获取支付参数失败原因：" + failMap.get("return_msg"));
            PayLog log = new PayLog(MapUtils.getString(payMap,"orderNumber"), PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")) + "",data.getSpbill_create_ip(),MapUtils.getString(payMap,"applyUrl"), JSON.toJSONString(data),"ERROR","获取支付参数失败原因：" + failMap.get("return_msg"));
            log.setRespInfo(str);
            payLogService.save(log);
            return new ResponseEntity<>("获取支付参数失败原因：" + failMap.get("return_msg"), HttpStatus.OK);
        }
        PayLog log = new PayLog(MapUtils.getString(payMap,"orderNumber"),PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")) + "",data.getSpbill_create_ip(),MapUtils.getString(payMap,"applyUrl"), JSON.toJSONString(data),"OK","访问成功");
        log.setRespInfo(str);
        payLogService.save(log);
        long endTime = new Date().getTime();
        logger.info("消耗时间：" + (endTime - startTime));
        return new ResponseEntity<>(wxPayParams, HttpStatus.OK);
    }

    //生成支付订单
    private static PayOrder getPayOrder(Map<String,Object> payMap, HttpServletRequest request){
        PayOrder payOrder = JSON.parseObject(JSON.toJSONString(payMap), PayOrder.class);
        payOrder.setCreatedAt(new Date());
        payOrder.setPayOrderNumber(MapUtils.getString(payMap,"payOrderNumber"));
        payOrder.setOrderNumber(MapUtils.getString(payMap,"orderNumber"));
        payOrder.setState(EnumType.PAY_STATE_NO.valueStr());
        payOrder.setPayAmount(PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")) + "");
        payOrder.setSource(MapUtils.getString(payMap,"source"));
        payOrder.setConsumerType("01-消费交易");
        payOrder.setCallUrl(MapUtils.getString(payMap,"backUrl"));
        payOrder.setOrderState(MapUtils.getInteger(payMap,"orderState"));
        payOrder.setPayWay(MapUtils.getInteger(payMap,"payWay"));
        payOrder.setCheckState("0");
        payOrder.setOrderAmount(PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")) + "");
        payOrder.setCustomerIp(WsdUtils.getIpAddress(request));
        payOrder.setOrderCurrency("CNY");
        payOrder.setOrderTime(new Date());
        payOrder.setDefaultBankNumber(MapUtils.getString(payMap,"bankCode"));
        payOrder.setUserId(MapUtils.getString(payMap,"openId"));
        payOrder.setOrderOwner(MapUtils.getString(payMap,"orderOwner"));
        String subPayWay = MapUtils.getString(payMap,"subPayWay");
        payOrder.setSubPayWay(subPayWay);
        if(EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr().equals(subPayWay)
                || EnumType.PAY_WAY_WEIXIN_X.valueStr().equals(subPayWay)){
            payOrder.setPayType(trade_type_JSAPI);
        }else if(EnumType.PAY_WAY_WEIXIN_APP.valueStr().equals(subPayWay)){
            payOrder.setPayType(trade_type_APP);
        }else if(EnumType.PAY_WAY_WEIXIN_H5.valueStr().equals(subPayWay)){
            payOrder.setPayType(trade_type_H5);
        }
        return payOrder;
    }

    //获取请求参数
    private static PayReqData getPayReqData(Map<String,Object> payMap, String nonce_str, HttpServletRequest request, WsdWechatConfig config) throws Exception{
        PayReqData data = new PayReqData();
        String subPayWay = MapUtils.getString(payMap,"subPayWay");
        if(subPayWay.equals(EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr())){
            logger.info("获取公众号支付请求参数");
            data.setAppid(config.getAppId());
            data.setTrade_type(trade_type_JSAPI);
            data.setOpenid(MapUtils.getString(payMap,"openId"));
            data.setMch_id(config.getMchId_wechat());
            data.setSpbill_create_ip(WsdUtils.getIpAddress(request));
        }else if(subPayWay.equals(EnumType.PAY_WAY_WEIXIN_APP.valueStr())){
            logger.info("获取App支付请求参数");
            data.setAppid(config.getAppId_app());
            data.setTrade_type(trade_type_APP);
            data.setMch_id(config.getMchId());
            data.setSpbill_create_ip(WsdUtils.getIpAddress(request));
        }else if(subPayWay.equals(EnumType.PAY_WAY_WEIXIN_X.valueStr())){
            logger.info("获取小程序支付请求参数");
            data.setAppid(config.getAppId_little());
            data.setTrade_type(trade_type_JSAPI);
            data.setOpenid(MapUtils.getString(payMap,"loginCode"));
            data.setMch_id(config.getMchId_wechat());
            data.setSpbill_create_ip(WsdUtils.getIpAddress(request));
        }else if(subPayWay.equals(EnumType.PAY_WAY_WEIXIN_H5.valueStr())){
            logger.info("获取微信H5支付请求参数");
            data.setAppid(config.getAppId_app());
            data.setTrade_type(trade_type_H5);
            data.setMch_id(config.getMchId());
            data.setSpbill_create_ip("113.142.33.142");
        }
        data.setNonce_str(nonce_str);
        data.setBody(MapUtils.getString(payMap,"payOrderName"));
        data.setOut_trade_no(MapUtils.getString(payMap,"payOrderNumber"));
        data.setTotal_fee(PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")));
        data.setNotify_url(profilesActive ? config.getBackUrl_prod() : config.getBackUrl_test()); //后台通知参数
        data.setSign(Signature.getSign(data,config.getSignKey()));
        logger.info("请求参数：" + JSON.toJSONString(data));
        return data;
    }

    //得到签名字符串
    private static String getSignStr(Map<String,Object> map,Long timeStamp,String nonce_str,String key,String subPayWay){
        Map<String,Object> signData = new HashMap<>();
        if(subPayWay.equals(EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr())
                || subPayWay.equals(EnumType.PAY_WAY_WEIXIN_X.valueStr())){
            logger.info("公众号支付或小程序支付签名开始。。。");
            signData.put("appId", map.get("appid"));
            signData.put("nonceStr", nonce_str);
            signData.put("timeStamp", timeStamp);
            signData.put("signType", "MD5");
            signData.put("package", "prepay_id=" + map.get("prepay_id"));
        }else if(subPayWay.equals(EnumType.PAY_WAY_WEIXIN_APP.valueStr()) || subPayWay.equals(EnumType.PAY_WAY_WEIXIN_H5.valueStr())){
            logger.info("App支付签名开始。。。");
            signData.put("appid", map.get("appid"));
            signData.put("partnerid", map.get("mch_id"));
            signData.put("prepayid", map.get("prepay_id"));
            signData.put("noncestr", nonce_str);
            signData.put("timestamp", timeStamp);
            signData.put("package", "Sign=WXPay");
        }
        logger.info("签名数据：" + JSON.toJSONString(signData));
        return Signature.getSign(signData,key);
    }

    //查询是否支付成功
    public static Boolean getTradeByReturn(String nonce_str, String transaction_id, WsdWechatConfig config, String subPayWay) throws Exception{
        PayReqData data = new PayReqData();
        if(EnumType.PAY_WAY_WEIXIN_X.valueStr().equals(subPayWay)){
            data.setAppid(config.getAppId_little());
            data.setMch_id(config.getMchId_wechat());
        }else if(EnumType.PAY_WAY_WEIXIN_PUBLIC_NUMBER.valueStr().equals(subPayWay)){
            data.setAppid(config.getAppId());
            data.setMch_id(config.getMchId_wechat());
        }else if(EnumType.PAY_WAY_WEIXIN_APP.valueStr().equals(subPayWay) || EnumType.PAY_WAY_WEIXIN_H5.valueStr().equals(subPayWay)){
            data.setAppid(config.getAppId_app());
            data.setMch_id(config.getMchId());
        }
        data.setNonce_str(nonce_str);
        data.setTransaction_id(transaction_id);
        data.setSign(Signature.getSign(data,config.getSignKey()));
        String str = PayService.queryRequest(data);
        Map<String,Object> map = XMLParser.getMapFromXML(str);
        if(WsdUtils.isNotEmpty(map.get("return_code")) && map.get("return_code").equals("SUCCESS")){
            if(WsdUtils.isNotEmpty(map.get("return_msg")) && map.get("return_msg").equals("OK")){
                return true;
            }
        }
        return false;
    }

}
