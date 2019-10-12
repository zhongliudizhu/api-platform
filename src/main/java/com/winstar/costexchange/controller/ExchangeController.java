package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.costexchange.entity.CostShop;
import com.winstar.costexchange.entity.ExchangeRecord;
import com.winstar.costexchange.repository.CostShopRepository;
import com.winstar.costexchange.repository.ExchangeRepository;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.order.utils.DateUtil;
import com.winstar.utils.WebUitl;
import com.winstar.vo.Result;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<ExchangeRecord> exchangeRecords = exchangeRepository.findByMobileAndStateAndCreatedAtBetweenOrderByCreatedAtDesc(mobile, ExchangeRecord.SUCCESS, DateUtil.getDayBegin(), DateUtil.getDayEnd());
        if(exchangeRecords.size() >= 3){
            logger.info("该手机号今日兑换已达3次！");
            return Result.fail("exchange_limit_3", "该手机号今日兑换已达3次！");
        }
        Double costTotal = exchangeRecords.stream().mapToDouble(ExchangeRecord::getCostAmount).sum();
        if((costTotal + costShop.getCostAmount()) >= 100){
            logger.info("同一手机号每日兑换话费不能超过100元！");
            return Result.fail("exchange_cost_100", "同一手机号每日兑换话费不能超过100元！");
        }
        if(!ObjectUtils.isEmpty(exchangeRecords) && (System.currentTimeMillis() - exchangeRecords.get(0).getCreatedAt().getTime()) < 35 * 60 * 1000) {
            logger.info("同一手机号距离上次兑换必须大于35分钟！");
            return Result.fail("exchange_cost_time30", "同一手机号距离上次兑换必须大于35分钟！");
        }
        List<ExchangeRecord> exchangeRecords_months = exchangeRepository.findByMobileAndStateAndCreatedAtBetweenOrderByCreatedAtDesc(mobile, ExchangeRecord.SUCCESS, DateUtil.getMonthBegin(), DateUtil.getMonthEnd());
        Double costTotal_months = exchangeRecords_months.stream().mapToDouble(ExchangeRecord::getCostAmount).sum();
        if((costTotal_months + costShop.getCostAmount()) >= 300){
            logger.info("同一手机号每月兑换话费不能超过300元！");
            return Result.fail("exchange_cost_300", "同一手机号每月兑换话费不能超过300元！");
        }
        List<ExchangeRecord> exchangeRecordList = exchangeRepository.findByMobileAndTemplateIdOrderByCreatedAtDesc(mobile, costShop.getTemplateId());
        List<ExchangeRecord> exchangeRecord_inChange = exchangeRecordList.stream().filter(s -> s.getState().equals(ExchangeRecord.INEXCHANGE)).collect(Collectors.toList());
        if(!ObjectUtils.isEmpty(exchangeRecord_inChange)){
            logger.info("同一手机号相同的商品已经在兑换中，请勿重复兑换！");
            return Result.fail("exchange_cost_ing", "同一手机号相同的商品已经在兑换中，请勿重复兑换！");
        }
        List<ExchangeRecord> exchangeRecord_just = exchangeRecordList.stream().filter(s -> s.getState().equals(ExchangeRecord.INORDER)).collect(Collectors.toList());
        String accountId = (String) request.getAttribute("accountId");
        String openId = (String) request.getAttribute("openId");
        logger.info("accountId=" + accountId + ",openId=" + openId);
        ExchangeRecord exchangeRecord;
        if(!ObjectUtils.isEmpty(exchangeRecord_just)){
            exchangeRecord = exchangeRecord_just.get(0);
        }else{
            exchangeRecord = new ExchangeRecord(costShop, accountId, openId, mobile);
        }
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("orderId", exchangeRecord.getOrderNumber());
        reqMap.put("mobileId", exchangeRecord.getMobile());
        reqMap.put("couponId", exchangeRecord.getTemplateId());
        reqMap.put("merId", AccountCouponService.merchant);
        reqMap.put("domain", "moveCost");
        reqMap.put("sign", SignUtil.sign(reqMap, AccountCouponService.secret));
        Map map = RequestUtil.post(sendCodeUrl + "api/v1/phonebill/exchange/add", reqMap);
        logger.info("请求发送验证码返回结果：" + JSON.toJSONString(map));
        if(MapUtils.getString(map, "retCode").equals("0000")){
            exchangeRepository.save(exchangeRecord);
            return Result.success(exchangeRecord);
        }
        return Result.fail(MapUtils.getString(map, "retCode"), MapUtils.getString(map, "retMsg"));
    }

    /**
     * 校验验证码是否正确
     */
    @RequestMapping(value = "verifyCode", method = RequestMethod.GET)
    public Result verifyCode(@RequestParam String orderNumber, @RequestParam String code){
        logger.info("orderNumber:" + orderNumber + ",code:" + code);
        if(StringUtils.isEmpty(orderNumber)){
            logger.info("订单号不能为空！");
            return Result.fail("orderNumber_not_found", "订单号不能为空！");
        }
        if(StringUtils.isEmpty(code)){
            logger.info("验证码不能为空！");
            return Result.fail("code_not_found", "验证码不能为空！");
        }
        ExchangeRecord exchangeRecord = exchangeRepository.findByOrderNumber(orderNumber);
        if(ObjectUtils.isEmpty(exchangeRecord)){
            logger.info("订单不存在！");
            return Result.fail("order_not_found", "订单不存在！");
        }
        if(exchangeRecord.getState().equals(ExchangeRecord.INEXCHANGE)){
            logger.info("订单已在兑换中，请耐心等待结果！");
            return Result.fail("order_not_found", "订单已在兑换中，请耐心等待结果！");
        }
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("orderId", orderNumber);
        reqMap.put("verifyCode", code);
        reqMap.put("merId", AccountCouponService.merchant);
        reqMap.put("sign", SignUtil.sign(reqMap, AccountCouponService.secret));
        long beginTime = System.currentTimeMillis();
        Map map = RequestUtil.post(sendCodeUrl + "api/v1/phonebill/exchange/sendIdentifyingCode", reqMap);
        long endTime = System.currentTimeMillis();
        logger.info("消耗时间：" + (endTime - beginTime));
        logger.info("请求校验验证码返回结果：" + JSON.toJSONString(map));
        //防止同步链路比异步链路慢时导致把成功状态改成其它状态
        ExchangeRecord record = exchangeRepository.findByOrderNumber(orderNumber);
        if(MapUtils.getString(map, "retCode").equals("0000")){
            if(record.getState().equals(ExchangeRecord.INORDER)){
                exchangeRecord.setState(ExchangeRecord.INEXCHANGE);
                exchangeRepository.save(exchangeRecord);
            }
            return Result.success(map);
        }
        if(record.getState().equals(ExchangeRecord.INORDER)) {
            exchangeRecord.setState(ExchangeRecord.FAIL);
            exchangeRecord.setFailMessage(MapUtils.getString(map, "retCode") + "：" + MapUtils.getString(map, "retMsg"));
            exchangeRepository.save(exchangeRecord);
        }
        return Result.fail(MapUtils.getString(map, "retCode"), MapUtils.getString(map, "retMsg"));
    }

    /**
     * 查询兑换记录列表
     */
    @RequestMapping(value = "getExchangeRecord", method = RequestMethod.GET)
    public Result getCoupons(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer nextPage, @RequestParam(defaultValue = "10") Integer pageSize){
        String accountId = (String) request.getAttribute("accountId");
        Pageable pageable = WebUitl.buildPageRequest(nextPage, pageSize, "[{property:'createdAt',direction:'DESC'}]");
        Page<ExchangeRecord> exchangeRecordPage = exchangeRepository.findByAccountId(accountId, pageable);
        return Result.success(exchangeRecordPage);
    }

}
