package com.winstar.activityCenter.controller;

import com.winstar.activityCenter.repository.CommunalActivityRepository;
import com.winstar.activityCenter.service.CommunalActivityService;
import com.winstar.activityCenter.vo.ActivityVo;
import com.winstar.exception.NotRuleException;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author UU
 * @Classname CommunalActivityController
 * @Description TODO
 * @Date 2019/7/2 11:31
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cbc/communalActivity")
public class CommunalActivityController {

    CommunalActivityRepository communalActivityRepository;
    CommunalActivityService communalActivityService;
    HttpServletRequest request;

    @GetMapping(value = "/availableActivities")
    public Result getActivities() throws NotRuleException {
        String accountId = (String) request.getAttribute("accountId");
        List<ActivityVo> activityVos = communalActivityService.findAvailableActivities(accountId);
        return Result.success(activityVos);
    }


}
