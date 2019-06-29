package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.costexchange.entity.ExchangeRecord;
import com.winstar.costexchange.repository.ExchangeRepository;
import com.winstar.costexchange.service.CouponSendService;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.redis.RedisTools;
import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2019/5/22
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class SendCouponController {

    private static final Logger logger = LoggerFactory.getLogger(SendCouponController.class);

    @Autowired
    ExchangeRepository exchangeRepository;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Autowired
    CouponSendService sendService;

    @Autowired
    RedisTools redisTools;

    /**
     * 查询话费兑换商品列表
     */
    @RequestMapping(value = "sendCoupon", method = RequestMethod.POST)
    public Result getCostShop(@RequestBody Map map) {
        logger.info("获取推送优惠券的参数：" + JSON.toJSONString(map));
        Result result = checkingParameters(map);
        if (!ObjectUtils.isEmpty(result)) {
            return result;
        }
        String orderNumber = MapUtils.getString(map, "orderNumber");
        if (!redisTools.setIfAbsent(orderNumber, 20)) {
            logger.info("20秒之内同一订单的重复通知:" + orderNumber);
            return Result.fail("many_notify", "20秒之内的重复通知！");
        }
        ExchangeRecord exchangeRecord = exchangeRepository.findByOrderNumber(orderNumber);
        if (ObjectUtils.isEmpty(exchangeRecord)) {
            logger.info("订单不存在！");
            return Result.fail("orderNumber_not_found", "订单不存在！");
        }
        if (exchangeRecord.getState().equals(ExchangeRecord.SUCCESS)) {
            logger.info("订单已经成功，又再次推送！订单号：" + orderNumber);
            return Result.success(new HashMap<>());
        }
        List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(map, exchangeRecord.getAccountId());
        exchangeRecord.setState(ExchangeRecord.SUCCESS);
        exchangeRecord.setResultTime(new Date());
        if (!ObjectUtils.isEmpty(accountCoupons)) {
            List<ExchangeRecord> exchangeRecords = null;
            if (!ObjectUtils.isEmpty(accountCoupons.get(0).getCouponId())) {
                exchangeRecords = exchangeRepository.findByCouponId(accountCoupons.get(0).getCouponId());
            }
            if (!ObjectUtils.isEmpty(exchangeRecords)) {
                for (ExchangeRecord ex : exchangeRecords) {
                    logger.info("优惠券重复发放，之前用户为：{}，订单号为：{}", ex.getAccountId(), ex.getOrderNumber());
                }
                return Result.fail("coupon_push_again", "优惠券重复发放");
            }
            exchangeRecord.setCouponId(accountCoupons.get(0).getCouponId());
        }
        sendService.sendCoupon(accountCoupons, exchangeRecord);
        logger.info("接收话费通知成功！");
        return Result.success(new HashMap<>());
    }

    @RequestMapping(value = "exchangeFail", method = RequestMethod.POST)
    public Result exchangeFail(@RequestBody Map map) {
        logger.info("兑换优惠券失败的参数：" + JSON.toJSONString(map));
        Result result = checkingParameters(map);
        if (!ObjectUtils.isEmpty(result)) {
            return result;
        }
        ExchangeRecord exchangeRecord = exchangeRepository.findByOrderNumber(MapUtils.getString(map, "orderNumber"));
        if (ObjectUtils.isEmpty(exchangeRecord)) {
            logger.info("订单不存在！");
            return Result.fail("orderNumber_not_found", "订单不存在！");
        }
        exchangeRecord.setState(ExchangeRecord.FAIL);
        exchangeRecord.setResultTime(new Date());
        exchangeRepository.save(exchangeRecord);
        logger.info("话费兑换失败！");
        return Result.success(new HashMap<>());
    }

    private static Result checkingParameters(Map map) {
        if (StringUtils.isEmpty(MapUtils.getString(map, "merchant"))) {
            logger.info("缺失商户号参数！");
            return Result.fail("Missing_parameter_merchant", "缺失商户号参数！");
        }
        if (StringUtils.isEmpty(MapUtils.getString(map, "sign"))) {
            logger.info("缺失验签参数！");
            return Result.fail("Missing_parameter_sign", "缺失验签参数！");
        }
        if (StringUtils.isEmpty(MapUtils.getString(map, "orderNumber"))) {
            logger.info("缺失订单号参数！");
            return Result.fail("Missing_parameter_merchant", "缺失订单号参数！");
        }
        if (StringUtils.isEmpty(MapUtils.getString(map, "domain"))) {
            logger.info("缺失domain参数！");
            return Result.fail("Missing_parameter_domain", "缺失domain参数！");
        }
        if (!SignUtil.checkSign(map)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        return null;
    }

}
