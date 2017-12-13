package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.PageViewLog;
import com.winstar.user.utils.ServiceManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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
    @PostMapping(value = "/add", produces = "application/json")
    public PageViewLog add(@RequestBody PageViewLog pageViewLog, HttpServletRequest request) throws NotRuleException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        pageViewLog.setAccountId(accountId);
        return ServiceManager.pageViewLogService.savePageViewLog(pageViewLog);
    }
}
