package com.winstar.oilOutPlatform.controller;


import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.controller.MyOilCouponController;
import com.winstar.oilOutPlatform.Service.OutOilCouponService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import com.winstar.oilOutPlatform.repository.OutOilCouponLogRepository;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.oilOutPlatform.vo.ActiveParams;
import com.winstar.oilOutPlatform.vo.AssignedParams;
import com.winstar.oilOutPlatform.vo.CouponVo;
import com.winstar.redis.OilRedisTools;
import com.winstar.utils.WsdUtils;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 库存模式（暂时不用）
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/cbc/outPlatform")
public class OutOilCouponController {

    private final
    OilRedisTools oilRedisTools;

    private final OutOilCouponRepository outOilCouponRepository;

    private final OutOilCouponLogRepository outOilCouponLogRepository;

    private final ApplicationContext applicationContext;

    private final OutOilCouponService outOilCouponService;

    private static String oilCouponStockKey = "out_platform_oil_pan_list";

    private static String order_pan_suffix = "_pan_list";

    private static String lock_suffix = "_locking";

    private static String allocation_suffix = "_allocating";

    private static String secret = "5aGO539qO7F91733O13d1XZT1953hoI1pP9mgLDD9M9AI3g99MYw1zL35n1793Ps";

    @Autowired
    public OutOilCouponController(OilRedisTools oilRedisTools, OutOilCouponRepository outOilCouponRepository, OutOilCouponLogRepository outOilCouponLogRepository, ApplicationContext applicationContext, OutOilCouponService outOilCouponService) {
        this.oilRedisTools = oilRedisTools;
        this.outOilCouponRepository = outOilCouponRepository;
        this.outOilCouponLogRepository = outOilCouponLogRepository;
        this.applicationContext = applicationContext;
        this.outOilCouponService = outOilCouponService;
    }

    /**
     * 库存模式（暂时不用）
     * 查询油券详情
     * 验证签名
     * 返回：id，金额，名称，销售状态，销售时间，使用状态，使用时间，使用油站id，使用油站名称
     */
    @RequestMapping(value = "getOilCoupon", method = RequestMethod.GET)
    public Result getOilCoupon(@RequestParam String oilId, @RequestParam String merchant,
                               @RequestParam String sign) throws Exception {
        log.info("入参：merchant is {}, sign is {}, oilId is {}", merchant, sign, oilId);
        Map<String, String> sigMap = new HashMap<>();
        sigMap.put("oilId", oilId);
        sigMap.put("merchant", merchant);
        sigMap.put("sign", sign);
        if (!SignUtil.checkSign(sigMap, secret)) {
            log.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        OutOilCoupon oilCoupon = outOilCouponRepository.findOne(oilId);
        if (ObjectUtils.isEmpty(oilCoupon)) {
            return Result.fail("missing oilCoupon", "查询油券不存在");
        }
        return Result.success(outOilCouponService.getOutOilCouponVo(oilCoupon));
    }

    /**
     * 库存模式（暂时不用）
     * 判断油券库存
     * 验证签名
     * 返回：true/false
     */
    @RequestMapping(value = "judgeStock", method = RequestMethod.GET)
    public Result judgeStock(
            @RequestParam String merchant,
            @RequestParam String sign,
            @RequestParam String number,
            @RequestParam(required = false, defaultValue = "off") String lock,
            @RequestParam(required = false) String orderId
    ) {
        log.info("入参：merchant is {}, sign is {}, number is {}, lock is {}, orderId is {}", merchant, sign, number, lock, orderId);
        if (lock.equals("on") && StringUtils.isEmpty(orderId)) {
            log.info("锁定油券时必须传订单号");
            return Result.fail("missing_param_orderId", "锁定油券时必须传订单号！");
        }
        Long num = Long.valueOf(number);
        Map<String, String> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("sign", sign);
        map.put("number", number);
        map.put("lock", lock);
        map.put("orderId", orderId);
        if (!SignUtil.checkSign(map, secret)) {
            log.info("验签失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        Long stock = oilRedisTools.getSetSize(oilCouponStockKey);
        log.info("库存：" + stock);
        if (stock < num) {
            log.info("库存不足，剩余数量：" + stock);
            return Result.success(false);
        }
        if (lock.equals("on")) {
            if (oilRedisTools.setIfAbsent(orderId + lock_suffix)) {
                for (int i = 0; i < num; i++) {
                    Object popValue = oilRedisTools.getRandomKeyFromSet(oilCouponStockKey);
                    oilRedisTools.addSet(orderId + order_pan_suffix, popValue);
                }
                log.info("剩余库存：" + oilRedisTools.getSetSize(oilCouponStockKey));
            } else {
                log.info("订单" + orderId + "正在锁定库存，请稍后！！！");
            }
        }
        return Result.success(true);
    }

    /**
     * 库存模式（暂时不用）
     * 售油
     * 验证签名
     * 返回：id，金额，名称，销售状态
     */
    @RequestMapping(value = "assigned", method = RequestMethod.POST)
    public Result saleOilCoupon(@RequestBody @Valid AssignedParams assignedParams) throws NotRuleException {
        String orderId = assignedParams.getOrderId();
        long number = Long.valueOf(assignedParams.getNumber());
        String merchant = assignedParams.getMerchant();
        String sign = assignedParams.getSign();
        log.info("merchant is {} and orderId is {} and number is {} and sign is {}", merchant, orderId, number, sign);
        List<CouponVo> couponVos;
        List<OutOilCoupon> coupons;
        OutOilCouponLog outOilCouponLog;
        Map<String, String> map = WsdUtils.objectToMap(assignedParams);
        if (!SignUtil.checkSign(map, secret)) {
            log.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        List<OutOilCouponLog> logs = outOilCouponLogRepository.findByOrderId(assignedParams.getOrderId());
        if (!ObjectUtils.isEmpty(logs)) {
            log.info("油券已分配");
            coupons = outOilCouponRepository.findByOrderId(orderId);
        } else {
            if (!oilRedisTools.setIfAbsent(orderId + allocation_suffix)) {
                return Result.fail("order_allocating", "订单正在分配油券！");
            }
            Set<Object> set = oilRedisTools.setMembers(orderId + order_pan_suffix);
            log.info("redis获取的券码为:{}", set);
            if (set.size() == 0) {
                log.info("订单不存在或已过期");
                oilRedisTools.remove(orderId + allocation_suffix);
                return Result.fail("orderId_not_exists", "订单不存在或已过期");
            }
            if (set.size() != number) {
                log.info("入参数量为：{} ，redis获取数量为：{}", number, set.size());
                oilRedisTools.remove(orderId + allocation_suffix);
                return Result.fail("number_wrong", "订单对应数量错误");
            }
            coupons = outOilCouponRepository.findByOilStateAndPanIn("0", set);
            if (coupons.size() != set.size()) {
                log.error("分配券码异常！！ ");
                oilRedisTools.remove(orderId + allocation_suffix);
                throw new NotRuleException("分配券码异常！！");
            }
            coupons.forEach(e -> {
                e.setOilState("1");
                e.setSaleTime(new Date());
                e.setUseState("0");
                e.setOrderId(orderId);
            });
            outOilCouponRepository.save(coupons);
            oilRedisTools.remove(orderId + order_pan_suffix);
            oilRedisTools.remove(orderId + lock_suffix);
            log.info("分配成功！！");
        }
        couponVos = new ArrayList<>();
        for (OutOilCoupon coupon : coupons) {
            CouponVo couponVo = new CouponVo();
            BeanUtils.copyProperties(coupon, couponVo);
            couponVos.add(couponVo);
        }
        outOilCouponLog = new OutOilCouponLog();
        outOilCouponLog.setOrderId(orderId);
        outOilCouponLog.setNumber(String.valueOf(number));
        outOilCouponLog.setType("sale");
        outOilCouponLog.setCreateTime(new Date());
        outOilCouponLogRepository.save(outOilCouponLog);
        oilRedisTools.remove(orderId + allocation_suffix);
        return Result.success(couponVos);
    }

    /**
     * 库存模式（暂时不用）
     * 解除订单锁定库存
     * 验证签名
     * 返回：true/false
     */
    @RequestMapping(value = "unStock", method = RequestMethod.GET)
    public Result unStock(
            @RequestParam String merchant,
            @RequestParam String sign,
            @RequestParam(required = false) String orderId
    ) {
        log.info("入参：merchant is {}, sign is {}, orderId is {}", merchant, sign, orderId);
        Map<String, String> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("sign", sign);
        map.put("orderId", orderId);
        if (!SignUtil.checkSign(map, secret)) {
            log.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        Set<Object> set = oilRedisTools.setMembers(orderId + order_pan_suffix);
        if (oilRedisTools.exists(orderId + allocation_suffix)) {
            return Result.fail("order_allocating", "订单正在分配油券！");
        }
        log.info("redis获取的券码为:{}", set);
        if (ObjectUtils.isEmpty(set)) {
            List<OutOilCouponLog> logs = outOilCouponLogRepository.findByOrderId(orderId);
            if (!ObjectUtils.isEmpty(logs)) {
                log.info("订单" + orderId + "订单已完成！！");
                return Result.fail("order_finished", "订单已完成！！！");
            }
            log.info("订单" + orderId + "不存在！！");
            return Result.fail("order_not_exists", "订单不存在！！！");
        } else {
            set.forEach(e -> oilRedisTools.addSet(oilCouponStockKey, e));
            log.info("剩余库存：" + oilRedisTools.getSetSize(oilCouponStockKey));
            oilRedisTools.remove(orderId + order_pan_suffix);
            oilRedisTools.remove(orderId + lock_suffix);
        }
        return Result.success(true);
    }

    /**
     * 库存模式（暂时不用）
     * 激活油券
     * 验证签名
     * 返回：id，券码
     */
    @RequestMapping(value = "active", method = RequestMethod.POST)
    public Result activeOilCoupon(@RequestBody @Valid ActiveParams activeParams) throws Exception {
        log.info("入参为" + JSON.toJSONString(activeParams));
        String oilId = activeParams.getOilId();
        String orderId = activeParams.getOrderId();
        if (!SignUtil.checkSign(WsdUtils.objectToMap(activeParams), secret)) {
            log.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        OutOilCoupon outOilCoupon = outOilCouponRepository.findByIdAndOrderId(oilId, orderId);
        if (ObjectUtils.isEmpty(outOilCoupon)) {
            return Result.fail("coupon_not_exist", "油券不存在");
        }
        ws.result.Result result = applicationContext.getBean(MyOilCouponController.class).activateOilCoupon(outOilCoupon.getPan(), outOilCoupon.getPanAmt());
        outOilCouponService.saveOutOilCouponLog(outOilCoupon, result);
        if (!result.getCode().equalsIgnoreCase("SUCCESS")) {
            log.info("激活失败！");
            return Result.fail("active_failed", "激活失败");
        }
        log.info("===激活油券成功===");
        Map<String, Object> map = new HashMap<>();
        map.put("id", oilId);
        map.put("pan", outOilCoupon.getPan());
        return Result.success(map);
    }

}
