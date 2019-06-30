package com.winstar.carActivity.controller;

import com.winstar.carActivity.entity.ActivityResource;
import com.winstar.carActivity.repository.ActivityResourceRepository;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cbc/activityResource/")
@Slf4j
public class ActivityResourceController {

    ActivityResourceRepository resourceRepository;

    @GetMapping("getAllResources")
    public Result getAllResources() {
        List<ActivityResource> all = resourceRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return Result.fail(Result.FAIL, "无相应资源");
        }
        return Result.success(all);
    }


}
