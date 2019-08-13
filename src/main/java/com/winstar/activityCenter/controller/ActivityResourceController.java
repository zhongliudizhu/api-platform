package com.winstar.activityCenter.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.ActivityResource;
import com.winstar.activityCenter.repository.ActivityResourceRepository;
import com.winstar.redis.RedisTools;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cbc/activityResource/")
@Slf4j
public class ActivityResourceController {

    ActivityResourceRepository resourceRepository;

    @Autowired
    RedisTools redisTools;

    @GetMapping("getAllResources")
    public Result getAllResources(@RequestParam(required = false, defaultValue = "") String activityType) {
        List<ActivityResource> activityResources;
        String resources = (String) redisTools.get("activity_resource_home_" + activityType);
        if(!StringUtils.isEmpty(resources)){
            log.info("缓存中有资源，直接返回。。。");
            activityResources = JSON.parseArray(resources, ActivityResource.class);
            return Result.success(activityResources);
        }
        if (StringUtils.isEmpty(activityType)) {
            activityResources = resourceRepository.findByStatusOrderByTypeAsc("yes");
        } else {
            activityResources = resourceRepository.findByActivityTypeAndStatusOrderByTypeAsc(activityType, "yes");
        }
        if (CollectionUtils.isEmpty(activityResources)) {
            return Result.fail(Result.FAIL, "无相应资源");
        }
        log.info("数据库中查询的资源放入缓存中！！！");
        redisTools.set("activity_resource_home_" + activityType, JSON.toJSONString(activityResources));
        return Result.success(activityResources);
    }

}
