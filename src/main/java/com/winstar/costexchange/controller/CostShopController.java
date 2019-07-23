package com.winstar.costexchange.controller;

import com.winstar.costexchange.repository.CostShopRepository;
import com.winstar.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2019/5/21
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
public class CostShopController {

    @Autowired
    CostShopRepository costShopRepository;

    /**
     * 查询话费兑换商品列表
     */
    @RequestMapping(value = "getShops", method = RequestMethod.GET)
    public Result getCostShop(){
        return Result.success(costShopRepository.findByRemarkOrderByAmountAsc("normal"));
    }

}
