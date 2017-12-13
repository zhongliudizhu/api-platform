package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.PageViewLog;
import com.winstar.user.utils.ServiceManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 页面访问日志
 *
 * @author laohu
 **/
@RestController
@RequestMapping("/api/v1/cbc/pageViewLog")
public class PageViewLogController {
    /**
     * 添加页面访问日志
     *
     * @param pageViewLog pageViewLog
     * @return PageViewLog
     * @throws NotRuleException NotRuleException
     */
    @PostMapping("/add")
    public PageViewLog add(@RequestBody PageViewLog pageViewLog) throws NotRuleException {
        return ServiceManager.pageViewLogService.savePageViewLog(pageViewLog);
    }
}
