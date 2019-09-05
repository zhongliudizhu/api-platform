package com.winstar.user.controller;

import com.winstar.user.entity.PageViewLog;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @return PageViewLog
     */
    @PostMapping(value = "/add", produces = "application/json")
    public PageViewLog add() {
//        String accountId = ServiceManager.accountService.getAccountId(request);
//        pageViewLog.setAccountId(accountId);
//        return ServiceManager.pageViewLogService.savePageViewLog(pageViewLog);
        return null;
    }
}
