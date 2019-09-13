package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.costexchange.entity.CostShop;
import com.winstar.costexchange.repository.CostShopRepository;
import com.winstar.redis.RedisTools;
import com.winstar.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zl on 2019/5/21
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
public class CostShopController {

    @Autowired
    CostShopRepository costShopRepository;

    @Autowired
    RedisTools redisTools;

    /**
     * 查询话费兑换商品列表
     */
    @RequestMapping(value = "getShops", method = RequestMethod.GET)
    public Result getCostShop(){
        if(!redisTools.exists("cost_goods_list")){
            List<CostShop> costShopList = costShopRepository.findByRemarkOrderByAmountAsc("normal");
            if(!ObjectUtils.isEmpty(costShopList)){
                redisTools.set("cost_goods_list", JSON.toJSONString(costShopList));
            }
        }
        List<CostShop> list = JSON.parseArray(redisTools.get("cost_goods_list").toString(), CostShop.class);
        return Result.success(list);
    }

}
