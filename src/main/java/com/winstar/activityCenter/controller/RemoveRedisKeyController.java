package com.winstar.activityCenter.controller;

import com.winstar.redis.CouponRedisTools;
import com.winstar.redis.OilRedisTools;
import com.winstar.redis.RedisTools;
import com.winstar.utils.WsdUtils;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2019/8/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/noAuth/")
@Slf4j
public class RemoveRedisKeyController {

    @Autowired
    RedisTools redisTools;

    @Autowired
    OilRedisTools oilRedisTools;

    @Autowired
    CouponRedisTools couponRedisTools;

    @GetMapping("key/set")
    public void setKey(@RequestParam String key, @RequestParam String value, @RequestParam(required = false) Long times) {
        if (ObjectUtils.isEmpty(times)) {
            redisTools.set(key, value);
        } else {
            redisTools.set(key, value, times);
        }
    }

    @GetMapping("remove/key")
    public void getAllResources(@RequestParam String id) {
        redisTools.remove(id);
    }

    @GetMapping("key/get")
    public Result getKey() {
        String code = WsdUtils.generateRandomCharAndNumber(10);
        redisTools.set(code, code, 1800L);
        return Result.success(code);
    }

    @GetMapping("key/exists")
    public Result existsKey(@RequestParam String key) {
        boolean b = couponRedisTools.exists(key);
        log.info("couponRedisTools.exists(key)  is {}" ,b);
        return Result.success(b);
    }

    @GetMapping("remove/oil/key")
    public void removeOilKey(@RequestParam String id) {
        oilRedisTools.remove(id);
    }

    @GetMapping("remove/coupon/key")
    public void removeCouponKey(@RequestParam String id) {
        couponRedisTools.remove(id);
    }

}
