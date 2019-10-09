package com.winstar.oilOutPlatform.controller;

import com.winstar.oilOutPlatform.repository.OutOilCouponLogRepository;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.oilOutPlatform.vo.ActiveParams;
import com.winstar.oilOutPlatform.vo.AssignedParams;
import com.winstar.redis.OilRedisTools;
import com.winstar.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by zl on 2019/10/9
 * 油券输出平台
 */
@RestController
@RequestMapping("/api/v1/cbc/outPlatform")
public class OutOilCouponController {

    private static final Logger logger = LoggerFactory.getLogger(OutOilCouponController.class);

    @Autowired
    OilRedisTools oilRedisTools;

    @Autowired
    OutOilCouponRepository outOilCouponRepository;

    @Autowired
    OutOilCouponLogRepository outOilCouponLogRepository;

    private static String oilCouponStockKey = "out_platform_oil_pan_list";

    private static String order_pan_suffix = "_pan_list";

    private static String lock_suffix = "_locking";

    /**
     * 查询油券详情
     * 验证签名
     * 返回：id，金额，名称，销售状态，销售时间，使用状态，使用时间，使用油站id，使用油站名称
     */
    @RequestMapping(value = "getOilCoupon", method = RequestMethod.GET)
    public Result getOilCoupon(@RequestParam String oilId){
        return null;
    }

    /**
     * 判断油券库存
     * 验证签名
     * 返回：true/false
     */
    @RequestMapping(value = "judgeStock", method = RequestMethod.GET)
    public Result judgeStock(@RequestParam Long number, @RequestParam(required = false, defaultValue = "off") String lock, @RequestParam(required = false) String orderId){
        logger.info("入参：number is {}, lock is {}, orderId is {}", number, lock, orderId);
        if(lock.equals("on") && StringUtils.isEmpty(orderId)){
            logger.info("锁定油券时必须传订单号");
            return Result.fail("missing_param_orderId", "锁定油券时必须传订单号！");
        }
        Long stock = oilRedisTools.getSetSize(oilCouponStockKey);
        logger.info("库存：" + stock);
        if(stock < number){
            logger.info("库存不足，剩余数量：" + stock);
            return Result.success(new HashMap<>().put("result", false));
        }
        if(lock.equals("on")){
            if(oilRedisTools.setIfAbsent(orderId + lock_suffix)){
                for(int i=0;i<number;i++){
                    Object popValue = oilRedisTools.getRandomKeyFromSet(oilCouponStockKey);
                    oilRedisTools.addSetExpire(orderId + order_pan_suffix, 3600L, popValue);
                }
                logger.info("剩余库存：" + oilRedisTools.getSetSize(oilCouponStockKey));
            }else{
                logger.info("订单" + orderId + "正在锁定库存，请稍后！！！");
            }
        }
        return Result.success(new HashMap<>().put("result", true));
    }

    /**
     * 售油
     * 验证签名
     * 返回：id，金额，名称，销售状态
     */
    @RequestMapping(value = "assigned", method = RequestMethod.POST)
    public Result saleOilCoupon(@RequestBody AssignedParams assignedParams){
        return null;
    }

    /**
     * 激活油券
     * 验证签名
     * 返回：id，券码
     */
    @RequestMapping(value = "active", method = RequestMethod.POST)
    public Result activeOilCoupon(@RequestParam ActiveParams activeParams){
        return null;
    }

}
