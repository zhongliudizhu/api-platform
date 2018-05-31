package com.winstar.carLifeMall.controller;

import com.winstar.carLifeMall.entity.*;
import com.winstar.carLifeMall.repository.EarlyAndEveningMarketConfigRepository;
import com.winstar.carLifeMall.service.CategoryService;
import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.SimpleResultObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 名称： CategoryController
 * 作者： dpw
 * 日期： 2018-05-04 10:56
 * 描述： 类别
 **/
@RequestMapping("/api/v1/cbc/carLife/category")
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;
    @Autowired
    EarlyAndEveningMarketConfigRepository earlyAndEveningMarketConfigRepository;
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    /**
     * 早晚市是否开启
     *
     * @return
     */
    @RequestMapping("/checkEarlyAndEveningMarketIsOk/{type}/type")
    public SimpleResultObj check(@PathVariable Integer type) throws NotRuleException, InvalidParameterException {

        EarlyAndEveningMarketConfig earlyAndEveningMarketConfig = earlyAndEveningMarketConfigRepository.findByType(type);
        if (earlyAndEveningMarketConfigService.checkIfOk(type)) {
            earlyAndEveningMarketConfig.setLeftTime(0l);
            return new SimpleResultObj(earlyAndEveningMarketConfig);
        }

        Date curTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curTime);

        calendar.set(DateUtil.getYear(curTime), DateUtil.getMonth(curTime), DateUtil.getDay(curTime), earlyAndEveningMarketConfig.getMarketStartTime(), 0, 0);
        long leftTime = calendar.getTimeInMillis() - curTime.getTime();
        if (leftTime >= 0)
            earlyAndEveningMarketConfig.setLeftTime(leftTime);
        else {
            leftTime = calendar.getTimeInMillis()+24*60*60*1000 - curTime.getTime();
            earlyAndEveningMarketConfig.setLeftTime(leftTime);
        }
        return new SimpleResultObj(earlyAndEveningMarketConfig);
    }

    /**
     * 获取类别
     *
     * @return
     * @throws NotFoundException
     */
    @RequestMapping("/list")
    public List<Category> findList() throws NotFoundException {
        List<Category> list = categoryService.findByStatus();

        if (list.size() == 0) throw new NotFoundException("category");

        return list;
    }

}
