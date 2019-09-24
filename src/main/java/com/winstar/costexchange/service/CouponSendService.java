package com.winstar.costexchange.service;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.entity.ExchangeRecord;
import com.winstar.costexchange.repository.ExchangeRepository;
import com.winstar.redis.CouponRedisTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zl on 2019/5/30
 */
@Service
public class CouponSendService {

    private static final Logger logger = LoggerFactory.getLogger(CouponSendService.class);

    @Autowired
    ExchangeRepository exchangeRepository;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Autowired
    CouponRedisTools couponRedisTools;

    @Transactional
    public void sendCoupon(List<AccountCoupon> accountCoupons, ExchangeRecord exchangeRecord){
        logger.info("保存的用户优惠券：" + JSON.toJSONString(accountCoupons));
        accountCouponRepository.save(accountCoupons);
        logger.info("保存优惠券成功！");
        logger.info("更新的兑换记录：" + JSON.toJSONString(exchangeRecord));
        exchangeRepository.save(exchangeRecord);
        logger.info("更新兑换记录成功！");
        String key = AccountCouponService.COUPON_LIST_PREFIX + exchangeRecord.getAccountId();
        if(couponRedisTools.exists(key)){
            couponRedisTools.hmPutAll(key, accountCoupons.stream().collect(Collectors.toMap(AccountCoupon::getCouponId, Function.identity())));
        }
    }

}
