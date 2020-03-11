package com.winstar.oilOutPlatform.Service;


import com.alibaba.fastjson.JSON;
import com.winstar.SearchOilCoupon;
import com.winstar.oil.entity.OilStation;
import com.winstar.oil.service.OilStationService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import com.winstar.oilOutPlatform.repository.OutOilCouponLogRepository;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.oilOutPlatform.vo.OutNotifyVo;
import com.winstar.oilOutPlatform.vo.OutOilCouponVo;
import com.winstar.redis.OilRedisTools;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import ws.result.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class OutOilCouponService {

    private final RestTemplate restTemplate;

    private final OilStationService oilStationService;

    private final OutOilCouponRepository outOilCouponRepository;

    private final OutOilCouponLogRepository outOilCouponLogRepository;

    private final OilRedisTools oilRedisTools;

    @Value("${info.outNotifyUrl}")
    private String outNotifyUrl;

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    private static final String appId = "b31fd00d564f4efc808934610cd9076d";

    private static final String authToken = "4e6fbbcc4093406cbcc5c53fc0c4f2ef";

    @Autowired
    public OutOilCouponService(RestTemplate restTemplate, OilStationService oilStationService, OutOilCouponLogRepository outOilCouponLogRepository, OutOilCouponRepository outOilCouponRepository, OilRedisTools oilRedisTools) {
        this.restTemplate = restTemplate;
        this.oilStationService = oilStationService;
        this.outOilCouponLogRepository = outOilCouponLogRepository;
        this.outOilCouponRepository = outOilCouponRepository;
        this.oilRedisTools = oilRedisTools;
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
        oilCouponLog.setType("verify");
        oilCouponLog.setNumber("1");
        oilCouponLog.setOilId(outOilCoupon.getId());
        log.info(aesPan + "核销结果:" + JSON.toJSONString(map));
        if (MapUtils.getString(map, "rc").equals("00") || MapUtils.getString(map, "rc").equals("43")) { //00代表成功，43代表已核销
            if ("1".equals(MapUtils.getString(map, "cardStatus"))) { //卡状态 0代表正常，1代表已使用，2代表其他
                log.info(aesPan + "核销成功！");
                String useDate;
                if (WsdUtils.isNotEmpty(MapUtils.getString(map, "txnDate")) && WsdUtils.isNotEmpty(MapUtils.getString(map, "txnTime"))) {
                    useDate = WsdUtils.formatDate(MapUtils.getString(map, "txnDate") + MapUtils.getString(map, "txnTime"), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
                    log.info("使用时间为：{}", useDate);
                } else {
                    useDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                }
                String tId = MapUtils.getString(map, "tid");
                outOilCoupon.setUseState("1");
                outOilCoupon.setUseDate(useDate);
                outOilCoupon.setTId(tId);
                outOilCoupon.setMemo(MapUtils.getString(map, "memo"));
                result.setCode("SUCCESS");
                OilStation oilStation = oilStationService.getOilStation(tId);
                String tName = "陕西省";
                if (!ObjectUtils.isEmpty(oilStation)) {
                    tName = oilStationService.getOilStation(tId).getName();
                }
                if (notifyOut(new OutNotifyVo(outPan, tId, tName, "1", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(useDate).getTime()), oilCouponLog)) {
                    log.error("输出平台通知核销成功！！");
                } else {
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

    /**
     * 通知输出平台
     *
     * @param notifyVo     vo
     * @param oilCouponLog 日志
     * @return 布尔
     */
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

    /**
     * 获取油券VO
     *
     * @param oilCoupon 油券
     * @return 油券Vo
     */
    public OutOilCouponVo getOutOilCouponVo(OutOilCoupon oilCoupon) throws Exception {
        String useState = oilCoupon.getUseState();
        if (!StringUtils.isEmpty(useState) && useState.equals("0")) {
            String panText = AESUtil.decrypt(oilCoupon.getPan(), AESUtil.dekey);
            Map map = SearchOilCoupon.verification(panText.length() == 20 ? oilSendNewUrl : oilSendUrl, panText);
            if (MapUtils.getString(map, "rc").equals("00") && MapUtils.getString(map, "cardStatus").equals("1")) {
                oilCoupon.setUseState(MapUtils.getString(map, "cardStatus"));
                oilCoupon.setTId(MapUtils.getString(map, "tid"));
                String txnDate = MapUtils.getString(map, "txnDate");
                String txnTime = MapUtils.getString(map, "txnTime");
                oilCoupon.setUseDate(formatTxnDateAndTime(txnDate, txnTime));
                outOilCouponRepository.save(oilCoupon);
            }
        }
        OutOilCouponVo oilCouponVo = new OutOilCouponVo();
        BeanUtils.copyProperties(oilCoupon, oilCouponVo);
        if (!StringUtils.isEmpty(oilCoupon.getTId())) {
            String otlName = oilStationService.getOilStation(oilCoupon.getTId()).getName();
            oilCouponVo.setTName(otlName);
        }
        return oilCouponVo;
    }

    /**
     * 格式化日期
     *
     * @param txnDate 年月日
     * @param txnTime 时分秒
     * @return yyyy-MM-dd HH:mm:ss
     */
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

    /**
     * 更新油券并保存日志
     *
     * @param oilCoupon       油券
     * @param outId           输出平台ID
     * @param merchant        商户
     * @param outOilCouponLog 油券日志
     */
    @Transactional
    public void saveOilAndLog(OutOilCoupon oilCoupon, String outId, String merchant, OutOilCouponLog outOilCouponLog) {
        oilCoupon.setOutId(outId);
        oilCoupon.setSaleTime(new Date());
        oilCoupon.setUseState("0");
        oilCoupon.setOilState("1");
        oilCoupon.setMerchant(merchant);
        outOilCouponRepository.save(oilCoupon);
        outOilCouponLog.setNumber("1");
        outOilCouponLog.setOilId(oilCoupon.getId());
        outOilCouponLog.setType("sale");
        outOilCouponLog.setCreateTime(new Date());
        outOilCouponLogRepository.save(outOilCouponLog);
    }

    /**
     * 保存油券查看日志
     *
     * @param outOilCoupon 油券
     * @param result       result
     */
    @Async
    public void saveOutOilCouponLog(OutOilCoupon outOilCoupon, ws.result.Result result) {
        OutOilCouponLog log = new OutOilCouponLog();
        log.setOilId(outOilCoupon.getId());
        log.setOrderId(outOilCoupon.getOrderId());
        log.setCreateTime(new Date());
        log.setType("active");
        if (!ObjectUtils.isEmpty(result) && "SUCCESS".equals(result.getCode())) {
            log.setCode("success");
        } else {
            log.setCode("failed");
        }
        outOilCouponLogRepository.save(log);
    }

    /**
     * 查看油券查看开关是否开启
     *
     * @return boolean
     */
    public boolean getCouponSwitch() {
        String coupon_switch = (String) oilRedisTools.get("out_coupon_61045811001_switch");
        return "on".equals(coupon_switch);
    }



}
