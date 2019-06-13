package com.winstar.costexchange.utils;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.vo.CouponVo;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2019/5/23
 */
public class RequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    @Async
    public static Map post(String url, Map<String, String> reqMap){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(reqMap),headers);
        ResponseEntity<Map> resp = new RestTemplate().exchange(url, HttpMethod.POST, entity, Map.class);
        return resp.getBody();
    }

    public static List<AccountCoupon> getAccountCoupons(Map map, String accountId) {
        List<CouponVo> couponVos = JSON.parseArray(map.get("coupons").toString(), CouponVo.class);
        List<AccountCoupon> accountCoupons = new ArrayList<>();
        for(CouponVo couponVo : couponVos){
            AccountCoupon accountCoupon = new AccountCoupon();
            accountCoupon.setCouponId(couponVo.getId());
            accountCoupon.setAmount(couponVo.getAmount());
            accountCoupon.setFullMoney(couponVo.getDoorSkill());
            accountCoupon.setTitle(couponVo.getName());
            accountCoupon.setSubTitle(couponVo.getSubTitle());
            accountCoupon.setBeginTime(couponVo.getStartTime());
            accountCoupon.setEndTime(couponVo.getEndTime());
            accountCoupon.setTags(couponVo.getSuitItems());
            accountCoupon.setShowStatus(couponVo.getShowStatus());
            accountCoupon.setState(AccountCouponService.NORMAL);
            accountCoupon.setCreatedAt(new Date());
            accountCoupon.setAccountId(accountId);
            accountCoupon.setType(MapUtils.getString(map, "domain"));
            logger.info("优惠券：" + accountCoupon.toString());
            accountCoupons.add(accountCoupon);
        }
        return accountCoupons;
    }

}
