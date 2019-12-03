package com.winstar.oil.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.SearchOilCoupon;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.entity.OilCouponVerificationLog;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.oil.repository.OilCouponVerificationLogRepository;
import com.winstar.oilOutPlatform.Service.OutOilCouponService;
import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import com.winstar.oilOutPlatform.repository.OutOilCouponRepository;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ws.result.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zl on 2017/6/22
 */
@RestController
@RequestMapping("api/v1/cbc/verification")
public class VerificationCardController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCardController.class);

    @Autowired
    OilCouponRepository oilCouponRepository;

    @Autowired
    MyOilCouponRepository myOilCouponRepository;

    @Autowired
    OilCouponVerificationLogRepository oilCouponVerificationLogRepository;

    @Autowired
    OutOilCouponService outOilCouponService;

    @Autowired
    OutOilCouponRepository outOilCouponRepository;

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity checkCard(@RequestParam String pan) throws Exception{
        logger.info("电子券使用进行核销。。。");
        Result result = new Result();
        String aesPan = AESUtil.encrypt(pan,AESUtil.dekey);
        logger.info("加密后的油券号码：" + aesPan);
        OilCouponVerificationLog oilCouponVerificationLog = new OilCouponVerificationLog();
        oilCouponVerificationLog.setCreateTime(new Date());
        oilCouponVerificationLog.setPan(aesPan);
        oilCouponVerificationLog.setRequestUrl(pan.length() == 20 ? oilSendNewUrl : oilSendUrl);
        oilCouponVerificationLog = oilCouponVerificationLogRepository.save(oilCouponVerificationLog);
        OutOilCoupon outOilCoupon= outOilCouponRepository.findByPan(aesPan);
        if (!ObjectUtils.isEmpty(outOilCoupon)) {
            return outOilCouponService.checkOutCard(outOilCoupon);
        }
        MyOilCoupon myOilCoupon = myOilCouponRepository.findByPan(aesPan);
        if(WsdUtils.isEmpty(myOilCoupon)){
            logger.info(pan + "油券不存在！");
            result.setCode("NOT_FOUND");
            result.setFailMessage(pan + "油券不存在！");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Map<String, String> map = SearchOilCoupon.verification(pan.length() == 20 ? oilSendNewUrl : oilSendUrl, pan);
        logger.info(aesPan + "核销结果:" + JSON.toJSONString(map));
        oilCouponVerificationLog.setBackData(JSON.toJSONString(map));
        if(MapUtils.getString(map, "rc").equals("00") || MapUtils.getString(map, "rc").equals("43")){ //00代表成功，43代表已核销
            if("1".equals(MapUtils.getString(map, "cardStatus"))){ //卡状态 0代表正常，1代表已使用，2代表其他
                logger.info(aesPan + "核销成功！");
                String useDate;
                if(WsdUtils.isNotEmpty(MapUtils.getString(map, "txnDate")) && WsdUtils.isNotEmpty(MapUtils.getString(map, "txnTime"))){
                    useDate = WsdUtils.formatDate(MapUtils.getString(map, "txnDate") + MapUtils.getString(map, "txnTime"),"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
                }else{
                    useDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                }
                updateOilCoupon(myOilCoupon,"1",useDate,MapUtils.getString(map, "tid"));
                result.setCode("SUCCESS");
                oilCouponVerificationLog.setType(1);
            }else if("0".equals(MapUtils.getString(map, "cardStatus"))){
                logger.info(aesPan + "撤销成功！");
                updateOilCoupon(myOilCoupon,"0",null,null);
                result.setCode("SUCCESS");
                oilCouponVerificationLog.setType(2);
            }else{
                logger.info(aesPan + "核销失败，状态其他，待易通核实！");
                result.setCode("FAIL");
                result.setFailMessage("cardStatus状态不是已使用！");
            }
        }else{
            logger.info(aesPan + "核销失败，接口不成功！");
            result.setCode("FAIL");
            result.setFailMessage(MapUtils.getString(map, "rcDetail"));
        }
        oilCouponVerificationLogRepository.save(oilCouponVerificationLog);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void updateOilCoupon(MyOilCoupon myOilCoupon,String useState,String useDate,String tId) {
        if(WsdUtils.isNotEmpty(myOilCoupon)){
            myOilCoupon.setUseState(useState);
            myOilCoupon.setUseDate(useDate);
            myOilCoupon.setTId(tId);
            myOilCouponRepository.save(myOilCoupon);
            logger.info("电子券状态修改成功！");
        }
    }

}
