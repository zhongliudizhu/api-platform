package com.winstar.cashier.creditpay.pay;

import com.google.common.collect.Maps;
import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.construction.utils.AppUtils;
import com.winstar.cashier.construction.utils.DateUtil;
import com.winstar.cashier.construction.utils.PayConfPC;
import com.winstar.cashier.construction.utils.PayUtils;
import com.winstar.cashier.creditpay.config.CreditConfig;
import com.winstar.cashier.creditpay.config.DebitConfig;
import com.winstar.cashier.entity.PayLog;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayLogRepository;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.cashier.wxpay.common.MD5;
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
 * Created by zl on 2017/12/20
 */
@Component
@ConfigurationProperties(prefix="info")
public class CreditPay {

    private static Boolean profilesActive;

    public static void setProfilesActive(Boolean profilesActive) {
        CreditPay.profilesActive = profilesActive;
    }

    private static final Logger logger = LoggerFactory.getLogger(CreditPay.class);

    public static ResponseEntity pay(Map<String,Object> payMap,HttpServletRequest request, PayOrderRepository payOrderRepository, PayLogRepository payLogRepository){
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNumber(MapUtils.getString(payMap,"orderNumber"));
        payOrder.setPayOrderNumber(DateUtil.currentTimeToSS() + WsdUtils.getRandomNumber(8));
        payOrder.setPayAmount(PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")) + "");
        payOrder.setOrderAmount(PayUtils.convertAmountY2F(MapUtils.getDouble(payMap,"orderAmount")) + "");
        payOrder.setState("0");
        payOrder.setCreatedAt(new Date());
        payOrder.setOrderTime(DateUtil.parseTime(DateUtil.currentTime()));
        payOrder.setOrderCurrency(PayConfPC.ORDER_CURRENCY);
        payOrder.setPayType(PayConfPC.PAY_TYPE);
        payOrder.setCustomerIp(WsdUtils.getIpAddress(request));
        payOrder.setDefaultBankNumber(MapUtils.getString(payMap,"bankCode"));
        payOrder.setConsumerType("01-消费交易");
        payOrder.setSource(MapUtils.getString(payMap,"source"));
        payOrder.setCallUrl(MapUtils.getString(payMap,"backUrl"));
        payOrder.setUserId(MapUtils.getString(payMap,"userId"));
        payOrder.setOrderState(MapUtils.getInteger(payMap,"orderState"));
        payOrder.setPayWay(MapUtils.getInteger(payMap,"payWay"));
        payOrder.setCheckState("0");
        payOrder.setOrderOwner(MapUtils.getString(payMap,"orderOwner"));
        payOrder.setSubPayWay(MapUtils.getString(payMap,"subPayWay"));
        payOrder = payOrderRepository.save(payOrder);
        return payment(getReqMap(payOrder.getPayOrderNumber(),MapUtils.getString(payMap,"orderAmount"),MapUtils.getString(payMap,"bankCode")),MapUtils.getString(payMap,"orderNumber"),MapUtils.getString(payMap,"applyUrl"), payLogRepository);
    }

    private static Map<String, String> getReqMap(String orderId, String payment, String bankCode) {
        Map<String, String> reqMap = Maps.newHashMap();
        reqMap.put("MERCHANTID", bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr()) ? CreditConfig.merchantid : DebitConfig.merchantid);
        reqMap.put("POSID", bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr()) ? CreditConfig.posid : DebitConfig.posid);
        reqMap.put("BRANCHID", bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr()) ? CreditConfig.branchid : DebitConfig.branchid);
        reqMap.put("ORDERID", orderId);
        reqMap.put("PAYMENT", payment);
        reqMap.put("CURCODE", bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr()) ? CreditConfig.curcode : DebitConfig.curcode);
        reqMap.put("TXCODE", bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr()) ? CreditConfig.txcode : DebitConfig.txcode);
        reqMap.put("REMARK1", "");
        reqMap.put("REMARK2", "");
        reqMap.put("TYPE", "1");
        reqMap.put("PUB", bankCode.equals(EnumType.PAY_BANKCODE_CONDTRUCTION_CREDIT.valueStr()) ? CreditConfig.pubkey30 : DebitConfig.pubkey30);
        reqMap.put("GATEWAY", "");
        reqMap.put("CLIENTIP", profilesActive ? CreditConfig.clientIp_prod : CreditConfig.clientIp_test);
        reqMap.put("REGINFO", "");
        reqMap.put("PROINFO", "");
        reqMap.put("REFERER", "");
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
        //定义接口参数
        StringBuilder formbuffer = new StringBuilder();
        String[] paramKeys = new String[]{
            "MERCHANTID", "POSID", "BRANCHID", "ORDERID", "PAYMENT", "CURCODE", "TXCODE",
            "REMARK1", "REMARK2", "TYPE", "GATEWAY", "CLIENTIP", "REGINFO", "PROINFO", "REFERER"};
        formbuffer.append("<form id='payform' name='payform' action='").append(CreditConfig.sendUrl).append("' method='POST'>");
        for (String item : paramKeys) {
            String value = reqMap.get(item);
            if(StringUtils.isNotBlank(value)){
                formbuffer.append("<input type='hidden' name='").append(item).append("' value='").append(value).append("' />");
            }
        }
        formbuffer.append("<input type='hidden' name='MAC' value='").append(createMac(reqMap)).append("' />");
        formbuffer.append("</form>");
        logger.info("生成的form表单：" + formbuffer.toString());
        //生成访问记录
        PayLog log = new PayLog(orderNumber,reqMap.get("orderAmount"),reqMap.get("customerIp"),applyUrl, AppUtils.restMapToStr(reqMap),"OK","访问成功");
        log.setRespInfo(formbuffer.toString());
        payLogRepository.save(log);
        //返回Map集合数据
        Map<String,String> retmap = Maps.newHashMap();
        retmap.put("formStr", formbuffer.toString());
        return new ResponseEntity<>(retmap, HttpStatus.OK);
    }

    private static String createMac(Map<String, String> reqMap){
        StringBuffer mac = new StringBuffer();
        mac.append("MERCHANTID=").append(MapUtils.getString(reqMap,"MERCHANTID")).append("&");
        mac.append("POSID=").append(MapUtils.getString(reqMap,"POSID")).append("&");
        mac.append("BRANCHID=").append(MapUtils.getString(reqMap,"BRANCHID")).append("&");
        mac.append("ORDERID=").append(MapUtils.getString(reqMap,"ORDERID")).append("&");
        mac.append("PAYMENT=").append(MapUtils.getString(reqMap,"PAYMENT")).append("&");
        mac.append("CURCODE=").append(MapUtils.getString(reqMap,"CURCODE")).append("&");
        mac.append("TXCODE=").append(MapUtils.getString(reqMap,"TXCODE")).append("&");
        mac.append("REMARK1=").append(MapUtils.getString(reqMap,"REMARK1")).append("&");
        mac.append("REMARK2=").append(MapUtils.getString(reqMap,"REMARK2")).append("&");
        mac.append("TYPE=").append(MapUtils.getString(reqMap,"TYPE")).append("&");
        mac.append("PUB=").append(MapUtils.getString(reqMap,"PUB")).append("&");
        mac.append("GATEWAY=").append(MapUtils.getString(reqMap,"GATEWAY")).append("&");
        mac.append("CLIENTIP=").append(MapUtils.getString(reqMap,"CLIENTIP")).append("&");
        mac.append("REGINFO=").append(MapUtils.getString(reqMap,"REGINFO")).append("&");
        mac.append("PROINFO=").append(MapUtils.getString(reqMap,"PROINFO")).append("&");
        mac.append("REFERER=").append(MapUtils.getString(reqMap,"REFERER"));
        return MD5.MD5Encode(mac.toString());
    }

}
