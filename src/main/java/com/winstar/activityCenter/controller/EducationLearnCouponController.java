package com.winstar.activityCenter.controller;

import com.winstar.redis.RedisTools;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by zl on 2019/8/19
 * 违法教育学校获优惠券
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/noAuth/")
@Slf4j
public class EducationLearnCouponController {

    @Autowired
    RedisTools redisTools;

    @RequestMapping(value = "education/getCoupon", method = RequestMethod.POST)
    public Result getCoupon(@RequestBody Map map){
        String openId = MapUtils.getString(map, "openId");
        String verifyCode = MapUtils.getString(map, "verifyCode");
        if(StringUtils.isEmpty(openId)){
            log.info("openId参数为空！");
            return Result.fail("missing_parameter_openId", "openId参数为空！");
        }
        if(StringUtils.isEmpty(verifyCode)){
            log.info("verifyCode参数为空！");
            return Result.fail("missing_parameter_verifyCode", "verifyCode参数为空！");
        }
        if(!redisTools.exists(verifyCode)){
            log.info("verifyCode无效！");
            return Result.fail("verifyCode_is_invalid", "verifyCode无效！");
        }
        return Result.success(true);
    }

}
