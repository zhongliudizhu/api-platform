package com.winstar.user.service;


import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.PageViewLog;
import com.winstar.user.repository.PageViewLogRepository;
import io.undertow.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        }
        pageViewLog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        PageViewLog pageViewLogSaved = pageViewLogRepository.save(pageViewLog);
        return pageViewLogSaved;
    }
}
