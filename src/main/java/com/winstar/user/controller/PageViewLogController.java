package com.winstar.user.controller;

import com.winstar.user.entity.PageViewLog;
import com.winstar.user.service.PageViewLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
*
*
* @author laohu
* @date 2017/12/12 11:44
* @desc
**/
@RestController
@RequestMapping("/api/v1/cbcPlatform/pageViewLog")
public class PageViewLogController {
    @Autowired
    PageViewLogService pageViewLogService;

    @PostMapping("/add")
    public PageViewLog pv(@RequestBody PageViewLog pageViewLog) {

        return pageViewLogService.savePageViewLog(pageViewLog);
    }
}
