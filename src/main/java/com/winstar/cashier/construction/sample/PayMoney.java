package com.winstar.cashier.construction.sample;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.winstar.cashier.construction.utils.AppUtils;
import com.winstar.cashier.construction.utils.DateUtil;
import com.winstar.cashier.construction.utils.PayConfPC;
import com.winstar.cashier.construction.utils.PayUtils;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by zl on 2017/3/21
 */
@Component
@ConfigurationProperties(prefix="info")
public class PayMoney {

    private static final Logger logger = LoggerFactory.getLogger(PayMoney.class);

    private static Boolean profilesActive;

    public static void setProfilesActive(Boolean profilesActive) {
        PayMoney.profilesActive = profilesActive;
    }

    public static ResponseEntity pay(Map<String,Object> payMap, HttpServletRequest request, PayOrderRepository payOrderRepository, PayLogRepository payLogRepository){
        Map<String, String> reqMap = getReqMap(request, MapUtils.getString(payMap,"orderAmount"), MapUtils.getString(payMap,"bankCode"), MapUtils.getString(payMap,"frontEndUrl"));
        PayOrder payOrder = JSON.parseObject(JSON.toJSONString(reqMap), PayOrder.class);
        payOrder.setCreatedAt(new Date());
        payOrder.setPayOrderNumber(payOrder.getOrderNumber());
        payOrder.setOrderNumber(MapUtils.getString(payMap,"orderNumber"));
        payOrder.setState("0");
        payOrder.setPayAmount(payOrder.getOrderAmount());
        payOrder.setSource(MapUtils.getString(payMap,"source"));
        payOrder.setConsumerType("01-消费交易");
        payOrder.setCallUrl(MapUtils.getString(payMap,"backUrl"));
        payOrder.setOrderState(MapUtils.getInteger(payMap,"orderState"));
        payOrder.setPayWay(MapUtils.getInteger(payMap,"payWay"));
        payOrder.setCheckState("0");
        payOrder.setUserId(MapUtils.getString(payMap,"userId"));
        payOrder.setOrderOwner(MapUtils.getString(payMap,"orderOwner"));
        payOrder.setSubPayWay(MapUtils.getString(payMap,"subPayWay"));
        payOrderRepository.save(payOrder);
        return payment(reqMap,MapUtils.getString(payMap,"orderNumber"),MapUtils.getString(payMap,"applyUrl"), payLogRepository);
    }

    /**
     * 封装请求参数
     * @param request request
     * @param orderAmount 订单价格
     * @param bankCode 银行编码，默认建行105
     * @param frontEndUrl 前端回调地址
     * @return Map
     */
    private static Map<String, String> getReqMap(HttpServletRequest request, String orderAmount, String bankCode, String frontEndUrl) {
        Map<String, String> reqMap = Maps.newHashMap();
        reqMap.put("payUrl", profilesActive ? PayConfPC.PROD_JSPT_PAY_URL : PayConfPC.TEST_JSPT_PAY_URL);
        reqMap.put("version", PayConfPC.VERSION_CODE);
        reqMap.put("charset", PayConfPC.CHARSET);
        reqMap.put("signMethod", PayConfPC.SIGNMETHOD);
        reqMap.put("payType", PayConfPC.PAY_TYPE);
        reqMap.put("transType", PayConfPC.PAY_TRANSTYPE);
        reqMap.put("merId", profilesActive ? PayConfPC.PROD_MER_ID : PayConfPC.TEST_MER_ID);
        reqMap.put("signKey", profilesActive ? PayConfPC.PROD_SIGN_KEY : PayConfPC.TEST_SIGN_KEY);
        reqMap.put("backEndUrl", profilesActive ? PayConfPC.PROD_BACK_END_URL : PayConfPC.TEST_BACK_END_URL);
        reqMap.put("frontEndUrl", frontEndUrl);
        reqMap.put("orderTime", DateUtil.currentTime());
        reqMap.put("orderNumber", DateUtil.currentTimeToSS() + WsdUtils.getRandomNumber(8));
        reqMap.put("orderAmount", PayUtils.convertAmountY2F(Double.valueOf(orderAmount)) + "");
        reqMap.put("customerIp", WsdUtils.getIpAddress(request));
        reqMap.put("defaultBankNumber", bankCode);
        reqMap.put("merReserved1", "支付订单");
        reqMap.put("merReserved2", StringUtils.EMPTY);
        reqMap.put("merReserved3", StringUtils.EMPTY);
        reqMap.put("orderCurrency", PayConfPC.ORDER_CURRENCY);
        reqMap.put("gateWay", StringUtils.EMPTY);
        reqMap.put("terType",StringUtils.EMPTY);
        reqMap.put("agentAmount",StringUtils.EMPTY);
        reqMap.put("orderDesc",StringUtils.EMPTY);
        reqMap.put("payPerson",StringUtils.EMPTY);
        reqMap.put("idType",StringUtils.EMPTY);
        reqMap.put("certifId",StringUtils.EMPTY);
        return reqMap;
    }

    /**
     * 请求银行并返回表单字符串
     * @param reqMap 请求参数
     * @param orderNumber 订单编号
     * @param applyUrl 访问接口的URL地址
     * @return ResponseEntity
     */
    private static ResponseEntity payment(Map<String, String> reqMap, String orderNumber, String applyUrl, PayLogRepository payLogRepository){
        //定义接口数据集合
        Map<String, String> paramMap = Maps.newHashMap();
        //定义接口参数
        String[] paramKeys = new String[]{
                "version", "charset", "signMethod", "payType", "transType", "merId", "backEndUrl",
                "frontEndUrl", "orderTime", "orderNumber", "orderAmount", "orderCurrency",
                "defaultBankNumber", "customerIp", "merReserved1", "merReserved2", "merReserved3",
                "gateWay", "terType", "agentAmount", "orderDesc", "payPerson", "idType", "certifId"};
        for (String item : paramKeys) {
            paramMap.put(item, reqMap.get(item));
        }
        // 过滤掉为空的键
        paramMap = Maps.filterEntries(paramMap, entry -> StringUtils.isNotBlank(entry.getValue()));
        //生成签名
        String sign = AppUtils.signBeforePost(paramMap, reqMap.get("signKey"), reqMap.get("charset"));
        logger.info("向清算平台发送支付请求:" + AppUtils.restMapToStr(paramMap));
        logger.info("原始签名：" + sign);
        //构造请求数据表单
        StringBuilder formbuffer = new StringBuilder();
        formbuffer.append("<form id='payform' name='payform' action='").append(reqMap.get("payUrl")).append("' method='POST'>");
        for (String item : paramKeys) {
            String value = paramMap.get(item);
            if(StringUtils.isNotBlank(value)){
                formbuffer.append("<input type='hidden' name='").append(item).append("' value='").append(paramMap.get(item)).append("' />");
            }
        }
        formbuffer.append("<input type='hidden' name='sign' value='").append(sign).append("' />");
        formbuffer.append("</form>");
        //生成访问记录
        PayLog log = new PayLog(orderNumber,reqMap.get("orderAmount"),reqMap.get("customerIp"),applyUrl, AppUtils.restMapToStr(paramMap),"OK","访问成功");
        log.setRespInfo(formbuffer.toString());
        payLogRepository.save(log);
        //返回Map集合数据
        Map<String,String> retmap = Maps.newHashMap();
        retmap.put("formStr", formbuffer.toString());
        return new ResponseEntity<>(retmap, HttpStatus.OK);
    }

}
