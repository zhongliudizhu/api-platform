package com.winstar.oilOutPlatform.Service;


import com.alibaba.fastjson.JSON;
import com.winstar.SearchOilCoupon;
import com.winstar.oil.service.OilStationService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import com.winstar.oilOutPlatform.repository.OutOilCouponLogRepository;
import com.winstar.oilOutPlatform.vo.OutNotifyVo;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ws.result.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class OutOilCouponService {

    private final RestTemplate restTemplate;

    private final OilStationService oilStationService;


    private final OutOilCouponLogRepository outOilCouponLogRepository;
    @Value("${info.outNotifyUrl}")
    private String outNotifyUrl;

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    private static final String appId = "b31fd00d564f4efc808934610cd9076d";
    private static final String authToken = "4e6fbbcc4093406cbcc5c53fc0c4f2ef";

    @Autowired
    public OutOilCouponService(RestTemplate restTemplate, OilStationService oilStationService, OutOilCouponLogRepository outOilCouponLogRepository) {
        this.restTemplate = restTemplate;
        this.oilStationService = oilStationService;
        this.outOilCouponLogRepository = outOilCouponLogRepository;
    }

    public ResponseEntity checkOutCard(OutOilCoupon outOilCoupon) throws Exception {
        log.info("输出平台油券核销");
        String aesPan = outOilCoupon.getPan();
        String pan = AESUtil.decrypt(aesPan, AESUtil.dekey);
        String outPan = AESUtil.encrypt(pan, AESUtil.key);
        Result result = new Result();
        Map<String, String> map = SearchOilCoupon.verification(pan.length() == 20 ? oilSendNewUrl : oilSendUrl, pan);
        OutOilCouponLog oilCouponLog = new OutOilCouponLog();
        oilCouponLog.setCreateTime(new Date());
        oilCouponLog.setType("verification");
        oilCouponLog.setNumber("1");
        oilCouponLog.setOilId(outOilCoupon.getId());
        log.info(aesPan + "核销结果:" + JSON.toJSONString(map));
        if (MapUtils.getString(map, "rc").equals("00") || MapUtils.getString(map, "rc").equals("43")) { //00代表成功，43代表已核销
            if ("1".equals(MapUtils.getString(map, "cardStatus"))) { //卡状态 0代表正常，1代表已使用，2代表其他
                log.info(aesPan + "核销成功！");
                String useDate;
                if (WsdUtils.isNotEmpty(MapUtils.getString(map, "txnDate")) && WsdUtils.isNotEmpty(MapUtils.getString(map, "txnTime"))) {
                    useDate = WsdUtils.formatDate(MapUtils.getString(map, "txnDate") + MapUtils.getString(map, "txnTime"), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
                } else {
                    useDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                }
                String tId = MapUtils.getString(map, "tid");
                outOilCoupon.setUseState("1");
                outOilCoupon.setUseDate(useDate);
                outOilCoupon.setTId(tId);
                result.setCode("SUCCESS");
                String tName = oilStationService.getOilStation(tId).getName();
                if (!notifyOut(new OutNotifyVo(outPan, tId, tName, "1", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(useDate)), oilCouponLog)) {
                    log.error("输出平台通知核销失败！！");
                }
            } else if ("0".equals(MapUtils.getString(map, "cardStatus"))) {
                log.info(aesPan + "撤销成功！");
                if (!notifyOut(new OutNotifyVo(outPan, "0"), oilCouponLog)) {
                    log.error("输出平台通知撤销失败！！");
                }
                result.setCode("SUCCESS");
            } else {
                log.info(aesPan + "核销失败，状态其他，待易通核实！");
                result.setCode("FAIL");
                result.setFailMessage("cardStatus状态不是已使用！");
            }
        } else {
            log.info(aesPan + "核销失败，接口不成功！");
            result.setCode("FAIL");
            result.setFailMessage(MapUtils.getString(map, "rcDetail"));
        }
        outOilCouponLogRepository.save(oilCouponLog);
        return ResponseEntity.ok("");
    }


    private boolean notifyOut(OutNotifyVo notifyVo, OutOilCouponLog oilCouponLog) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("appId", appId);
        headers.set("authToken", authToken);
        HttpEntity<OutNotifyVo> entity = new HttpEntity<>(notifyVo, headers);
        try {
            ResponseEntity<Map> resp = restTemplate.exchange(outNotifyUrl, HttpMethod.POST, entity, Map.class);
            log.info("resp is {}", resp);
            Object obj = resp.getBody().get("data");
            oilCouponLog.setCode(obj.toString());
            log.info("响应结果：" + obj.toString());
            return (boolean) obj;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("无数据！");
        }
        oilCouponLog.setCode("fail");
        return false;
    }


}
