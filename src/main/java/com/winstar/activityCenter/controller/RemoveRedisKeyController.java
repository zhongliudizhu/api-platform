package com.winstar.activityCenter.controller;

import com.winstar.redis.RedisTools;
import com.winstar.utils.WsdUtils;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("remove/key")
    public void getAllResources(@RequestParam String id) {
        redisTools.remove(id);
    }

    @GetMapping("key/get")
    public Result getKey(){
        String code = WsdUtils.generateRandomCharAndNumber(10);
        redisTools.set(code, code, 1800L);
        return Result.success(code);
    }

}
