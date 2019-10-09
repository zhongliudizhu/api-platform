package com.winstar.oilOutPlatform.controller;

import com.winstar.costexchange.vo.CouponVo;
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
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    MyOilCouponController myOilCouponController;

    @Autowired
    OilStationService stationService;

    private static String oilCouponStockKey = "out_platform_oil_pan_list";

    private static String order_pan_suffix = "_pan_list";

    private static String lock_suffix = "_locking";

    private static String findOilCouponUrl = "https://mobile.sxwinstar.net/wechat_access/api/v1/items/verification/cards/onlySearch";

    /**
     * 查询油券详情
     * 验证签名
     * 返回：id，金额，名称，销售状态，销售时间，使用状态，使用时间，使用油站id，使用油站名称
     */
    @RequestMapping(value = "getOilCoupon", method = RequestMethod.GET)
    public Result getOilCoupon(@RequestParam String oilId) throws Exception {
        OutOilCoupon oilCoupon = outOilCouponRepository.findOne(oilId);
        if (ObjectUtils.isEmpty(oilCoupon)) {
            return Result.fail("missing oilCoupon", "查询油券不存在");
        }
        String useState = oilCoupon.getUseState();
        if (useState.equals("0")) {
            String panText = AESUtil.decrypt(oilCoupon.getPan(), AESUtil.dekey);
            Map map = new RestTemplate().getForObject(findOilCouponUrl + "/" + panText, Map.class);
            if(MapUtils.getString(map,"rc").equals("00")&&MapUtils.getString(map,"cardStatus").equals("1")){
                oilCoupon.setUseState("1");
                oilCoupon.setUseDate(new Date().toString());
            }
        }
        String otlName = stationService.getOilStation(oilCoupon.getTId()).getName();
        OutOilCouponVo oilCouponVo = new OutOilCouponVo();
        BeanUtils.copyProperties(oilCoupon, oilCouponVo);
        oilCouponVo.setTName(otlName);
        return Result.success(oilCouponVo);
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
    public Result saleOilCoupon(@RequestBody AssignedParams assignedParams) throws NotRuleException {
        String orderId = assignedParams.getOrderId();
        long number = assignedParams.getNumber();
        logger.info("merchant is {} and orderId is {} and number is {}", assignedParams.getMerchant(), orderId, number);
        List<CouponVo> couponVos;
        List<OutOilCoupon> coupons;
        OutOilCouponLog outOilCouponLog;
        OutOilCouponLog log = outOilCouponLogRepository.findByOrderId(assignedParams.getOrderId());
        if (!ObjectUtils.isEmpty(log)) {
            logger.info("油券已分配");
            coupons = outOilCouponRepository.findByOrderId(orderId);
        } else {
            Set<Object> set = oilRedisTools.setMembers(orderId + order_pan_suffix);
            logger.info("redis获取的券码为:{}", set);
            if (set.size() == 0) {
                logger.info("订单已过期");
                return Result.fail("orderId_not_exists", "订单已过期");
            }
            if (set.size() != number) {
                logger.info("入参数量为：{} ，redis获取数量为：{}", number, set.size());
                return Result.fail("number_wrong", "订单对应数量错误");
            }
            coupons = outOilCouponRepository.findByOilStateAndPanIn("0", set);
            if (coupons.size() != set.size()) {
                logger.error("分配券码异常！！ ");
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
            logger.info("分配成功！！");
        }
        couponVos = new ArrayList<>();
        coupons.forEach(e -> {
            CouponVo couponVo = new CouponVo();
            BeanUtils.copyProperties(e, couponVo);
            couponVos.add(couponVo);
        });
        outOilCouponLog = new OutOilCouponLog();
        outOilCouponLog.setOrderId(orderId);
        outOilCouponLog.setNumber(String.valueOf(number));
        outOilCouponLog.setType("sale");
        outOilCouponLog.setCreateTime(new Date());
        outOilCouponLogRepository.save(outOilCouponLog);
        return Result.success(couponVos);
    }

    /**
     * 激活油券
     * 验证签名
     * 返回：id，券码
     */
    @RequestMapping(value = "active", method = RequestMethod.POST)
    public Result activeOilCoupon(@RequestParam ActiveParams activeParams) {
        String oilId = activeParams.getOilId();
        String orderId = activeParams.getOrderId();
        if (StringUtils.isEmpty(oilId) || StringUtils.isEmpty(orderId)) {
            return Result.fail("param_missing", "参数缺失");
        }
        List<OutOilCouponLog> oilCouponLogs = outOilCouponLogRepository.findByOilIdAndOrderId(oilId, orderId);
        if(!CollectionUtils.isEmpty(oilCouponLogs)&&oilCouponLogs.get(0).getCode().equals("success")){
            return Result.fail("active_failed","油券已激活，请勿重复激活");
        }
        OutOilCoupon outOilCoupon = outOilCouponRepository.findOne(oilId);
        ws.result.Result result;
        try {
            result = myOilCouponController.activateOilCoupon(outOilCoupon.getPan(), outOilCoupon.getPanAmt());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("", "");
        }
        if(result.getCode().equalsIgnoreCase("SUCCESS")){

        }

        OutOilCouponLog log=new OutOilCouponLog();


        return null;
    }

}
