package com.winstar.activityCenter.controller;

import com.winstar.activityCenter.entity.ActivityResource;
import com.winstar.activityCenter.repository.ActivityResourceRepository;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("getAllResources")
    public Result getAllResources(@RequestParam(required = false, defaultValue = "") String activityType) {
        List<ActivityResource> activityResources;
        if (StringUtils.isEmpty(activityType)) {
            activityResources = resourceRepository.findByStatusOrderByTypeAsc("yes");
        } else {
            activityResources = resourceRepository.findByActivityTypeAndStatusOrderByTypeAsc(activityType, "yes");
        }
        if (CollectionUtils.isEmpty(activityResources)) {
            return Result.fail(Result.FAIL, "无相应资源");
        }
        return Result.success(activityResources);
    }


}
