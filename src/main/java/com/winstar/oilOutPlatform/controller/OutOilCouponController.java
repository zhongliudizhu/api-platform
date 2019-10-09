package com.winstar.oilOutPlatform.controller;

import com.winstar.oil.controller.MyOilCouponController;
import com.winstar.oil.service.OilStationService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import com.winstar.oilOutPlatform.repository.OutOilCouponLogRepository;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.oilOutPlatform.vo.ActiveParams;
import com.winstar.oilOutPlatform.vo.AssignedParams;
import com.winstar.oilOutPlatform.vo.OutOilCouponVo;
import com.winstar.redis.OilRedisTools;
import com.winstar.utils.AESUtil;
import com.winstar.vo.Result;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2019/10/9
 * 油券输出平台
 */
@RestController
@RequestMapping("/api/v1/cbc/outPlatform")
public class OutOilCouponController {

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
    public Result judgeStock(@RequestParam AssignedParams assignedParams) {
        return null;
    }

    /**
     * 售油
     * 验证签名
     * 返回：id，金额，名称，销售状态
     */
    @RequestMapping(value = "assigned", method = RequestMethod.POST)
    public Result saleOilCoupon(@RequestBody AssignedParams assignedParams) {
        return null;
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
