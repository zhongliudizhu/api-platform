package com.winstar.oilOutPlatform.controller;

import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.controller.MyOilCouponController;
import com.winstar.oilOutPlatform.Service.OutOilCouponService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.redis.OilRedisTools;
import com.winstar.utils.AESUtil;
import com.winstar.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zl on 2019/10/9
 * 油券输出平台
 */
@RestController
@RequestMapping("/api/v1/cbc/outPlatform")
public class OverSoldController {

    private static final Logger logger = LoggerFactory.getLogger(OverSoldController.class);

    private final OilRedisTools oilRedisTools;

    private final OutOilCouponRepository outOilCouponRepository;

    private final ApplicationContext applicationContext;

    private final OutOilCouponService outOilCouponService;

    private static final String oilCouponStockKey = "out_platform_oil_pan_list";

    private static final String secret = "5aGO539qO7F91733O13d1XZT1953hoI1pP9mgLDD9M9AI3g99MYw1zL35n1793Ps";

    @Autowired
    public OverSoldController(OilRedisTools oilRedisTools, OutOilCouponRepository outOilCouponRepository, ApplicationContext applicationContext, OutOilCouponService outOilCouponService) {
        this.oilRedisTools = oilRedisTools;
        this.outOilCouponRepository = outOilCouponRepository;
        this.applicationContext = applicationContext;
        this.outOilCouponService = outOilCouponService;
    }

    /**
     * 超卖模式
     * 查询油券详情
     * 验证签名
     * 返回：id，金额，名称，销售状态，销售时间，使用状态，使用时间，使用油站id，使用油站名称
     */
    @RequestMapping(value = "getCouponInfo", method = RequestMethod.GET)
    public Result getOilCouponByOutId(@RequestParam String outId,
                                      @RequestParam String merchant,
                                      @RequestParam String sign) throws Exception {
        logger.info("入参：merchant is {}, sign is {}, outId is {}", merchant, sign, outId);
        Map<String, String> sigMap = new HashMap<>();
        sigMap.put("outId", outId);
        sigMap.put("sign", sign);
        sigMap.put("merchant", merchant);
        if (!SignUtil.checkSign(sigMap, secret)) {
            logger.info("验证签名失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        OutOilCoupon oilCoupon = outOilCouponRepository.findByOutId(outId);
        if (!ObjectUtils.isEmpty(oilCoupon)) {
            return Result.success(outOilCouponService.getOutOilCouponVo(oilCoupon));
        }
        return Result.fail("missing oilCoupon", "查询油券不存在");
    }

    /**
     * 超卖模式
     * 获取pan
     * 验证签名
     * 返回：pan
     */
    @RequestMapping(value = "getPan", method = RequestMethod.GET)
    public Result saleOil(@RequestParam String merchant, @RequestParam String outId, @RequestParam String sign) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("outId", outId);
        map.put("sign", sign);
        if (!SignUtil.checkSign(map, secret)) {
            logger.info("验签失败！");
            return Result.fail("sign_fail", "验证签名失败！");
        }
        if (!oilRedisTools.setIfAbsent(outId, 300)) {
            logger.info("点击过于频繁，请稍后再试！操作Id:" + outId);
            return Result.fail("oilCoupon_loading", "点击过于频繁，请稍后再试!");
        }
        OutOilCoupon oilCoupon = outOilCouponRepository.findByOutId(outId);
        Object popValue = null;
        if (ObjectUtils.isEmpty(oilCoupon)) {
            if (!outOilCouponService.getCouponSwitch()) {
                logger.info("查看油券功能已关闭！！");
                oilRedisTools.remove(outId);
                return Result.success(null);
            }
            long beginTime = System.currentTimeMillis();
            try {
                logger.info("剩余油券数量：" + oilRedisTools.getSetSize(oilCouponStockKey));
                popValue = oilRedisTools.getRandomKeyFromSet(oilCouponStockKey);
            } catch (Exception e) {
                logger.error("redis异常！", e);
            }
            if (ObjectUtils.isEmpty(popValue)) {
                logger.info("没有该面值的券码，发券失败！");
                oilRedisTools.remove(outId);
                return Result.success(null);
            }
            logger.info("获取的油券编码：" + popValue);
            oilCoupon = outOilCouponRepository.findByPan(popValue.toString());
            if (!ObjectUtils.isEmpty(oilCoupon.getOutId())) {
                throw new NotRuleException("oil_allocated_again");
            }
            outOilCouponService.saveOilAndLog(oilCoupon, outId, merchant, new OutOilCouponLog());
            logger.info("执行发券成功，分配的券码为：" + oilCoupon.getPan() + "，执行分券操作耗时时间：" + (System.currentTimeMillis() - beginTime) + "ms");
        }
        ws.result.Result activeResult = null;
        try {
            activeResult = applicationContext.getBean(MyOilCouponController.class).activateOilCoupon(oilCoupon.getPan(), oilCoupon.getPanAmt());
            outOilCouponService.saveOutOilCouponLog(oilCoupon, activeResult);
        } catch (Exception e) {
            logger.error("易通激活接口异常，拿出的券回归缓存中");
            oilRedisTools.addSet(oilCouponStockKey, popValue);
            oilRedisTools.remove(outId);
        }
        if (!ObjectUtils.isEmpty(activeResult) && "SUCCESS".equals(activeResult.getCode())) {
            logger.info("===激活油券成功===");
        } else {
            logger.info("激活失败！");
            return Result.fail("active_failed", "激活失败");
        }
        oilRedisTools.remove(outId);
        return Result.success(AESUtil.encrypt(AESUtil.decrypt(oilCoupon.getPan(), AESUtil.dekey), AESUtil.key));
    }


}
