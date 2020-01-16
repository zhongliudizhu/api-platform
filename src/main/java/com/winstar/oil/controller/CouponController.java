package com.winstar.oil.controller;

import com.winstar.oil.entity.OilBlackList;
import com.winstar.oil.entity.OilCoupon;
import com.winstar.oil.repository.OilBlackListRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.redis.OilRedisTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zl on 2019/9/20
 */
@RestController
@RequestMapping("/api/v1/noAuth/")
@Slf4j
public class CouponController {

    @Autowired
    OilCouponRepository oilCouponRepository;

    @Autowired
    OilBlackListRepository oilBlackListRepository;

    @Autowired
    OilRedisTools oilRedisTools;

    @GetMapping("getOilCouponToRedis")
    public void getCoupons(Double panAmt){
        log.info("从数据库中获取油券放入缓存中。。。");
        List<OilCoupon> oilCouponList = oilCouponRepository.findByOilStateAndPanAmt("0", panAmt);
        String key = "oil_pan_list_" + panAmt;
        if(!ObjectUtils.isEmpty(oilCouponList)){
            List<String> pans = oilCouponList.stream().map(OilCoupon::getPan).collect(Collectors.toList());
            oilRedisTools.addSet(key, pans.toArray());
            log.info("放入缓存成功！");
        }
    }

    @GetMapping("getOilBlackOrVipToRedis")
    public void getBlackList(String accountId, String tag){
        if(!StringUtils.isEmpty(accountId)){
            oilRedisTools.addSet(tag.equals("no") ? "cbc_oil_black_list" : "cbc_oil_vip_list", accountId);
        }else{
            List<OilBlackList> blackLists = oilBlackListRepository.findByVip(tag);
            if(!ObjectUtils.isEmpty(blackLists)){
                List<String> accountIds = blackLists.stream().map(OilBlackList::getAccountId).collect(Collectors.toList());
                oilRedisTools.addSet(tag.equals("no") ? "cbc_oil_black_list" : "cbc_oil_vip_list", accountIds.toArray());
            }
        }
    }

}
