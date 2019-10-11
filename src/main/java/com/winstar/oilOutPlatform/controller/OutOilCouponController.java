package com.winstar.oilOutPlatform.controller;

import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.controller.MyOilCouponController;
import com.winstar.oil.service.OilStationService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import com.winstar.oilOutPlatform.repository.OutOilCouponLogRepository;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.oilOutPlatform.vo.ActiveParams;
import com.winstar.oilOutPlatform.vo.AssignedParams;
import com.winstar.oilOutPlatform.vo.CouponVo;
import com.winstar.oilOutPlatform.vo.OutOilCouponVo;
import com.winstar.redis.OilRedisTools;
import com.winstar.utils.AESUtil;
import com.winstar.vo.Result;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OilStationService stationService;

    private static String oilCouponStockKey = "out_platform_oil_pan_list";

    private static String order_pan_suffix = "_pan_list";

    private static String lock_suffix = "_locking";

    private static String allocation_suffix = "_allocating";

    private static String findOilCouponUrl = "https://mobile.sxwinstar.net/wechat_access/api/v1/items/verification/cards/onlySearch";

    /**
     * 查询油券详情
     * 验证签名
     * 返回：id，金额，名称，销售状态，销售时间，使用状态，使用时间，使用油站id，使用油站名称
     */
    @RequestMapping(value = "getOilCoupon", method = RequestMethod.GET)
    public Result getOilCoupon(@RequestParam String oilId, @RequestParam String merchant,
                               @RequestParam String sign) throws Exception {

        logger.info("入参：merchant is {}, sign is {}, oilId is {}", merchant, sign, oilId);
        Map<String, String> sigMap = new HashMap<>();
        sigMap.put("merchant", merchant);
        sigMap.put("sign", sign);
        sigMap.put("oilId", oilId);
        if (!SignUtil.checkSign(sigMap)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        OutOilCoupon oilCoupon = outOilCouponRepository.findOne(oilId);
        if (ObjectUtils.isEmpty(oilCoupon)) {
            return Result.fail("missing oilCoupon", "查询油券不存在");
        }
        String useState = oilCoupon.getUseState();
        if (!StringUtils.isEmpty(useState) && useState.equals("0")) {
            String panText = AESUtil.decrypt(oilCoupon.getPan(), AESUtil.dekey);
            Map map = new RestTemplate().getForObject(findOilCouponUrl + "/" + panText, Map.class);
            if (MapUtils.getString(map, "rc").equals("00") && MapUtils.getString(map, "cardStatus").equals("1")) {
                oilCoupon.setUseState("1");
                String txnDate = MapUtils.getString(map, "txnDate");
                String txnTime = MapUtils.getString(map, "txnTime");
                oilCoupon.setUseDate(formatTxnDateAndTime(txnDate, txnTime));
                outOilCouponRepository.save(oilCoupon);
            }
        }
        OutOilCouponVo oilCouponVo = new OutOilCouponVo();
        BeanUtils.copyProperties(oilCoupon, oilCouponVo);
        if (!StringUtils.isEmpty(oilCoupon.getTId())) {
            String otlName = stationService.getOilStation(oilCoupon.getTId()).getName();
            oilCouponVo.setTName(otlName);
        }
        return Result.success(oilCouponVo);
    }


    /**
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
    ){
        logger.info("入参：merchant is {}, sign is {}, number is {}, lock is {}, orderId is {}", merchant, sign, number, lock, orderId);
        if(lock.equals("on") && StringUtils.isEmpty(orderId)){
            logger.info("锁定油券时必须传订单号");
            return Result.fail("missing_param_orderId", "锁定油券时必须传订单号！");
        }
        Long num = Long.valueOf(number);
        Map<String, String> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("sign", sign);
        map.put("number", number.toString());
        map.put("lock", lock);
        map.put("orderId", orderId);
        if (!SignUtil.checkSign(map)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        Long stock = oilRedisTools.getSetSize(oilCouponStockKey);
        logger.info("库存：" + stock);
        if(stock < num){
            logger.info("库存不足，剩余数量：" + stock);
            return Result.success(false);
        }
        if(lock.equals("on")){
            if(oilRedisTools.setIfAbsent(orderId + lock_suffix)){
                for(int i=0;i<num;i++){
                    Object popValue = oilRedisTools.getRandomKeyFromSet(oilCouponStockKey);
                    oilRedisTools.addSet(orderId + order_pan_suffix, popValue);
                }
                logger.info("剩余库存：" + oilRedisTools.getSetSize(oilCouponStockKey));
            }else{
                logger.info("订单" + orderId + "正在锁定库存，请稍后！！！");
            }
        }
        return Result.success(true);
    }

    /**
     * 测试用
     */
    @RequestMapping("send")
    public String sendRedis() {
        List<OutOilCoupon> outOilCoupons = outOilCouponRepository.findByOilState("0");
        outOilCoupons.forEach(e -> oilRedisTools.addSet(oilCouponStockKey, e.getPan()));
        long stock = oilRedisTools.getSetSize(oilCouponStockKey);
        return stock + "";
    }

    /**
     * 售油
     * 验证签名
     * 返回：id，金额，名称，销售状态
     */
    @RequestMapping(value = "assigned", method = RequestMethod.POST)
    public Result saleOilCoupon(@RequestBody @Valid AssignedParams assignedParams) throws NotRuleException {
        String orderId = assignedParams.getOrderId();
        String outUserId = assignedParams.getOutUserId();
        long number = Long.valueOf(assignedParams.getNumber());
        String merchant = assignedParams.getMerchant();
        String sign = assignedParams.getSign();
        logger.info("merchant is {} and orderId is {} and number is {} and sign is {} and outUserId is {}", merchant, orderId, number, sign, outUserId);
        List<CouponVo> couponVos;
        List<OutOilCoupon> coupons;
        OutOilCouponLog outOilCouponLog;
        Map<String, String> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("sign", sign);
        map.put("number", String.valueOf(number));
        map.put("orderId", orderId);
        map.put("outUserId", outUserId);
        if (!SignUtil.checkSign(map)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        List<OutOilCouponLog> logs = outOilCouponLogRepository.findByOrderId(assignedParams.getOrderId());
        if (!ObjectUtils.isEmpty(logs)) {
            logger.info("油券已分配");
            coupons = outOilCouponRepository.findByOrderId(orderId);
        } else {
            if (!oilRedisTools.setIfAbsent(orderId + allocation_suffix)) {
                return Result.fail("order_allocating", "订单正在分配油券！");
            }
            Set<Object> set = oilRedisTools.setMembers(orderId + order_pan_suffix);
            logger.info("redis获取的券码为:{}", set);
            if (set.size() == 0) {
                logger.info("订单不存在或已过期");
                oilRedisTools.remove(orderId + allocation_suffix);
                return Result.fail("orderId_not_exists", "订单不存在或已过期");
            }
            if (set.size() != number) {
                logger.info("入参数量为：{} ，redis获取数量为：{}", number, set.size());
                oilRedisTools.remove(orderId + allocation_suffix);
                return Result.fail("number_wrong", "订单对应数量错误");
            }
            coupons = outOilCouponRepository.findByOilStateAndPanIn("0", set);
            if (coupons.size() != set.size()) {
                logger.error("分配券码异常！！ ");
                oilRedisTools.remove(orderId + allocation_suffix);
                throw new NotRuleException("分配券码异常！！");
            }
            coupons.forEach(e -> {
                e.setOilState("1");
                e.setSaleTime(new Date());
                e.setUseState("0");
                e.setOutUserId(outUserId);
                e.setOrderId(orderId);
            });
            outOilCouponRepository.save(coupons);
            oilRedisTools.remove(orderId + order_pan_suffix);
            oilRedisTools.remove(orderId + lock_suffix);
            logger.info("分配成功！！");
        }
        couponVos = new ArrayList<>();
        for (OutOilCoupon coupon : coupons) {
            if (!outUserId.equals(coupon.getOutUserId())) {
                return Result.fail("user_not_this", "油券非此用户！！");
            }
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
        logger.info("入参：merchant is {}, sign is {}, orderId is {}", merchant, sign, orderId);
        Map<String, String> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("sign", sign);
        map.put("orderId", orderId);
        if (!SignUtil.checkSign(map)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        Set<Object> set = oilRedisTools.setMembers(orderId + order_pan_suffix);
        if (oilRedisTools.exists(orderId + allocation_suffix)) {
            return Result.fail("order_allocating", "订单正在分配油券！");
        }
        logger.info("redis获取的券码为:{}", set);
        if (ObjectUtils.isEmpty(set)) {
            List<OutOilCouponLog> logs = outOilCouponLogRepository.findByOrderId(orderId);
            if (!ObjectUtils.isEmpty(logs)) {
                logger.info("订单" + orderId + "订单已完成！！");
                return Result.fail("order_finished", "订单已完成！！！");
            }
            logger.info("订单" + orderId + "不存在！！");
            return Result.fail("order_not_exists", "订单不存在！！！");
        } else {
            set.forEach(e -> oilRedisTools.addSet(oilCouponStockKey, e));
            logger.info("剩余库存：" + oilRedisTools.getSetSize(oilCouponStockKey));
            oilRedisTools.remove(orderId + order_pan_suffix);
            oilRedisTools.remove(orderId + lock_suffix);
        }
        return Result.success(true);
    }

    /**
     * 激活油券
     * 验证签名
     * 返回：id，券码
     */
    @RequestMapping(value = "active", method = RequestMethod.POST)
    public Result activeOilCoupon(@RequestBody @Valid ActiveParams activeParams) throws Exception {
        String oilId = activeParams.getOilId();
        String orderId = activeParams.getOrderId();
        String userId = activeParams.getOutUserId();
        logger.info("merchant is {} and orderId is {} and oilId is {} and sign is {} and userId is {}", activeParams.getMerchant(), orderId, oilId, activeParams.getSign(), userId);
        Map<String, String> signMap = new HashMap<>();
        signMap.put("merchant", activeParams.getMerchant());
        signMap.put("sign", activeParams.getSign());
        signMap.put("oilId",oilId);
        signMap.put("orderId",orderId);
        signMap.put("outUserId",userId);
        if (!SignUtil.checkSign(signMap)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        OutOilCoupon outOilCoupon = outOilCouponRepository.findOne(oilId);
        if (ObjectUtils.isEmpty(outOilCoupon)) {
            return Result.fail("coupon_not_exist", "油券不存在");
        }
        if (!outOilCoupon.getOutUserId().equals(userId)) {
            return Result.fail("coupon_non_belong_user", "油券不属于此用户");
        }
        List<OutOilCouponLog> oilCouponLogs = outOilCouponLogRepository.findByOilIdAndOrderId(oilId, orderId);
        if (oilCouponLogs.stream().noneMatch(e -> e.getCode().equals("success"))) {
            ws.result.Result result = applicationContext.getBean(MyOilCouponController.class).activateOilCoupon(outOilCoupon.getPan(), outOilCoupon.getPanAmt());
            saveOutOilCouponLog(outOilCoupon, result);
            if (!result.getCode().equalsIgnoreCase("SUCCESS")) {
                logger.info("激活失败！");
                return Result.fail("active_failed", "激活失败");
            }
            logger.info("===激活油券成功===");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", oilId);
        map.put("pan", outOilCoupon.getPan());
        map.put("outUserId", userId);
        return Result.success(map);
    }

    @Async
    public void saveOutOilCouponLog(OutOilCoupon outOilCoupon, ws.result.Result result) {
        OutOilCouponLog log = new OutOilCouponLog();
        log.setOilId(outOilCoupon.getId());
        log.setOrderId(outOilCoupon.getOrderId());
        log.setCreateTime(new Date());
        log.setType("active");
        if (result.getCode().equalsIgnoreCase("SUCCESS")) {
            log.setCode("success");
        } else {
            log.setCode("failed");
        }
        outOilCouponLogRepository.save(log);
    }


    private static String formatTxnDateAndTime(String txnDate, String txnTime) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simple.parse(txnDate + txnTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

}
