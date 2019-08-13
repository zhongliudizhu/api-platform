package com.winstar.activityCenter.controller;

import com.winstar.redis.RedisTools;
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
@RequestMapping("/api/v1/cbc/")
@Slf4j
public class RemoveRedisKeyController {

    @Autowired
    RedisTools redisTools;

    @GetMapping("remove/key")
    public void getAllResources(@RequestParam String id) {
        redisTools.remove(id);
    }

}
