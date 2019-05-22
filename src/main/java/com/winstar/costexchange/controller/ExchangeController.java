package com.winstar.costexchange.controller;

import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2019/5/22
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class ExchangeController {

    /**
     * 发送验证码
     */
    @RequestMapping(value = "sendCode", method = RequestMethod.GET)
    public Result getCostShop(@RequestParam String costShopId, @RequestParam String mobile){
        //1.下单（生成兑换记录）
        //2.调话费兑换的接口，把订单数据传输给话费平台，由话费平台发送短信
        return null;
    }

    /**
     * 校验验证码是否正确
     */
    @RequestMapping(value = "verifyCode", method = RequestMethod.GET)
    public Result verifyCode(@RequestParam String mobile, @RequestParam String code){
        //直接把数据组装传送给话费兑换进行校验
        return null;
    }

}
