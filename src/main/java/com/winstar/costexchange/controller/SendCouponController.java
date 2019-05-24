package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.costexchange.entity.AccountCoupon;
import com.winstar.costexchange.entity.ExchangeRecord;
import com.winstar.costexchange.repository.AccountCouponRepository;
import com.winstar.costexchange.repository.ExchangeRepository;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.costexchange.utils.SignUtil;
import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    /**
     * 查询话费兑换商品列表
     */
    @RequestMapping(value = "sendCoupon", method = RequestMethod.POST)
    public Result getCostShop(@RequestBody Map map){
        logger.info("获取推送优惠券的参数：" + JSON.toJSONString(map));
        if(StringUtils.isEmpty(MapUtils.getString(map, "merchant"))){
            logger.info("缺失商户号参数！");
            return Result.fail("Missing_parameter_merchant", "缺失商户号参数！");
        }
        if(StringUtils.isEmpty(MapUtils.getString(map, "sign"))){
            logger.info("缺失验签参数！");
            return Result.fail("Missing_parameter_sign", "缺失验签参数！");
        }
        if(StringUtils.isEmpty(MapUtils.getString(map, "orderNumber"))){
            logger.info("缺失订单号参数！");
            return Result.fail("Missing_parameter_merchant", "缺失订单号参数！");
        }
        if(StringUtils.isEmpty(MapUtils.getString(map, "domain"))){
            logger.info("缺失domain参数！");
            return Result.fail("Missing_parameter_domain", "缺失domain参数！");
        }
        if(!SignUtil.checkSign(map)){
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        ExchangeRecord exchangeRecord = exchangeRepository.findByOrderNumber(MapUtils.getString(map, "orderNumber"));
        if(ObjectUtils.isEmpty(exchangeRecord)){
            logger.info("订单不存在！");
            return Result.fail("orderNumber_not_found", "订单不存在！");
        }
        List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(map, exchangeRecord.getAccountId());
        accountCouponRepository.save(accountCoupons);
        exchangeRecord.setState("success");
        exchangeRecord.setResultTime(new Date());
        exchangeRepository.save(exchangeRecord);
        logger.info("接收话费通知成功！");
        return Result.success("接收话费通知成功！");
    }

}
