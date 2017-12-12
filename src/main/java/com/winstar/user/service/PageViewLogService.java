package com.winstar.user.service;


import com.winstar.user.entity.PageViewLog;
import com.winstar.user.repository.PageViewLogRepository;
import io.undertow.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author laohu
 * @date 2017/12/11 17:52
 * @desc
 **/
@Service
public class PageViewLogService {
    @Autowired
    PageViewLogRepository pageViewLogRepository;

    /**
     * 保存页面访问日志
     *
     * @param pageViewLog
     * @return
     */
    @Async
    public PageViewLog savePageViewLog(PageViewLog pageViewLog) {
        return pageViewLogRepository.save(pageViewLog);
    }
}
