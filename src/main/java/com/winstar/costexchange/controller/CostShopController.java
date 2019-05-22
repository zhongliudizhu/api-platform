package com.winstar.costexchange.controller;

import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2019/5/21
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class CostShopController {

    /**
     * 查询话费兑换商品列表
     */
    @RequestMapping(value = "getCostShops", method = RequestMethod.GET)
    public Result getCostShop(){
        return null;
    }

}
