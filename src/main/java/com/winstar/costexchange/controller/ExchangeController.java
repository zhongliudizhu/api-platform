package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.costexchange.entity.CostShop;
import com.winstar.costexchange.entity.ExchangeRecord;
import com.winstar.costexchange.repository.CostShopRepository;
import com.winstar.costexchange.repository.ExchangeRepository;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.costexchange.utils.SignUtil;
import com.winstar.order.utils.DateUtil;
import com.winstar.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2019/5/22
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
public class ExchangeController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    @Autowired
    CostShopRepository costShopRepository;

    @Autowired
    ExchangeRepository exchangeRepository;

    @Value("${info.cost_exchange_server}")
    private String sendCodeUrl;

    /**
     * 发送验证码
     */
    @RequestMapping(value = "sendCode", method = RequestMethod.GET)
    public Result getCostShop(HttpServletRequest request, @RequestParam String costShopId, @RequestParam String mobile) throws ParseException {
        CostShop costShop = costShopRepository.findOne(costShopId);
        if(ObjectUtils.isEmpty(costShop)){
            logger.info("移动话费商品不存在！");
            return Result.fail("cost_shop_not_found", "移动话费商品不存在！");
        }
        List<ExchangeRecord> exchangeRecords = exchangeRepository.findByMobileAndStateAndCreatedAtBetween(mobile, "success", DateUtil.getDayBegin(), DateUtil.getDayEnd());
        if(exchangeRecords.size() >= 3){
            logger.info("该手机号今日兑换已达3次！");
            return Result.fail("exchange_limit_3", "该手机号今日兑换已达3次！");
        }
        Double costTotal = exchangeRecords.stream().mapToDouble(ExchangeRecord::getCostAmount).sum();
        if(costTotal >= 100){
            logger.info("该手机号今日兑换话费已达100元！");
            return Result.fail("exchange_cost_100", "该手机号今日兑换话费已达100元！");
        }
        String accountId = (String) request.getAttribute("accountId");
        String openId = (String) request.getAttribute("openId");
        logger.info("accountId=" + accountId + ",openId=" + openId);
        ExchangeRecord exchangeRecord = new ExchangeRecord(costShop, accountId, openId, mobile);
        exchangeRepository.save(exchangeRecord);
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("orderId", exchangeRecord.getOrderNumber());
        reqMap.put("mobileId", exchangeRecord.getMobile());
        reqMap.put("couponId", exchangeRecord.getTemplateId());
        reqMap.put("merId", SignUtil.merchant);
        reqMap.put("sign", SignUtil.sign(reqMap));
        Map map = RequestUtil.post(sendCodeUrl + "api/v1/phonebill/exchange/add", reqMap);
        logger.info("请求发送验证码返回结果：" + JSON.toJSONString(map));
        return Result.success(exchangeRecord);
    }

    /**
     * 校验验证码是否正确
     */
    @RequestMapping(value = "verifyCode", method = RequestMethod.GET)
    public Result verifyCode(@RequestParam String orderNumber, @RequestParam String code){
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("orderId", orderNumber);
        reqMap.put("verifyCode", code);
        reqMap.put("merId", SignUtil.merchant);
        reqMap.put("sign", SignUtil.sign(reqMap));
        Map map = RequestUtil.post(sendCodeUrl + "api/v1/phonebill/exchange/sendIdentifyingCode", reqMap);
        logger.info("请求校验验证码返回结果：" + JSON.toJSONString(map));
        return null;
    }

}
