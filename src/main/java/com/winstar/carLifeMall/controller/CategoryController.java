package com.winstar.carLifeMall.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.carLifeMall.entity.*;
import com.winstar.carLifeMall.repository.CategoryRepository;
import com.winstar.carLifeMall.repository.EarlyAndEveningMarketConfigRepository;
import com.winstar.carLifeMall.service.CategoryService;
import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import com.winstar.user.utils.SimpleResultObj;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    /**
     * 早晚市是否开启
     *
     * @return
     */
    @RequestMapping("/checkEarlyAndEveningMarketIsOk/{type}/type")
    public SimpleResultObj check(@PathVariable Integer type) throws NotRuleException, InvalidParameterException {

        if (earlyAndEveningMarketConfigService.checkIfOk(type))
            return new SimpleResultObj("TRUE");
        EarlyAndEveningMarketConfig earlyAndEveningMarketConfig = earlyAndEveningMarketConfigRepository.findByType(type);
        long curTime = new Date().getTime();
        earlyAndEveningMarketConfig.setCurrentTime(curTime);

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
