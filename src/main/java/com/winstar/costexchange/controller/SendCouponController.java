package com.winstar.costexchange.controller;

import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by zl on 2019/5/22
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class SendCouponController {

    /**
     * 查询话费兑换商品列表
     */
    @RequestMapping(value = "sendCoupon", method = RequestMethod.POST)
    public Result getCostShop(@RequestBody Map map){
        //根据话费兑换传来的参数给用户发放优惠券，同时修改兑换记录的状态
        return null;
    }

}
