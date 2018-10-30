package com.winstar.user.service;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.PageViewLog;
import com.winstar.user.repository.PageViewLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author laohu
 * @date 2017/12/11 17:52
 * @desc
 **/
@Service
public class PageViewLogService {
    static final Logger logger = LoggerFactory.getLogger(PageViewLogService.class);

    @Autowired
    PageViewLogRepository pageViewLogRepository;

    /**
     * 保存页面访问日志
     *
     * @param pageViewLog
     * @return
     */
    public PageViewLog savePageViewLog(PageViewLog pageViewLog) throws NotRuleException {
        if (null == pageViewLog) {
            throw new NotRuleException("pageViewLog");
        } else if (StringUtils.isEmpty(pageViewLog.getUrl())) {
            throw new NotRuleException("url");
        } else if (StringUtils.isEmpty(pageViewLog.getAccountId())) {
            throw new NotRuleException("accountId");
        }

        pageViewLog.setCreateTime(new Date());
        PageViewLog pageViewLogSaved = pageViewLogRepository.save(pageViewLog);
        return pageViewLogSaved;
    }

    /**
     * 保存页面访问日志
     *
     * @param pageViewLog
     * @return
     */
    @Async
    public PageViewLog saveAsyncPageViewLog(PageViewLog pageViewLog) throws NotRuleException {
        return savePageViewLog(pageViewLog);
    }
}
