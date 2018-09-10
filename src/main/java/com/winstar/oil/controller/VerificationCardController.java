package com.winstar.oil.controller;

import com.winstar.SearchOilCoupon;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity checkCard(@RequestParam String pan) throws Exception{
        logger.info("电子券使用进行核销。。。");
        logger.info("加密后的油券号码：" + AESUtil.encrypt(pan,AESUtil.dekey));
        Result result = new Result();
        MyOilCoupon myOilCoupon = myOilCouponRepository.findByPan(AESUtil.encrypt(pan,AESUtil.dekey));
        if(WsdUtils.isEmpty(myOilCoupon)){
            logger.info(pan + "油券不存在！");
            result.setCode("NOT_FOUND");
            result.setFailMessage(pan + "油券不存在！");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        if(pan.length() == 20){
            oilSendUrl = oilSendNewUrl;
        }
        Map<String, String> map = SearchOilCoupon.verification(oilSendUrl, pan);
        logger.info("rc:" + MapUtils.getString(map, "rc"));
        if(MapUtils.getString(map, "rc").equals("00") || MapUtils.getString(map, "rc").equals("43")){ //00代表成功，43代表已核销
            if("1".equals(MapUtils.getString(map, "cardStatus"))){ //卡状态 0代表正常，1代表已使用，2代表其他
                String useDate;
                if(WsdUtils.isNotEmpty(MapUtils.getString(map, "txnDate")) && WsdUtils.isNotEmpty(MapUtils.getString(map, "txnTime"))){
                    useDate = WsdUtils.formatDate(MapUtils.getString(map, "txnDate") + MapUtils.getString(map, "txnTime"),"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
                }else{
                    useDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                }
                updateOilCoupon(myOilCoupon,"1",useDate,MapUtils.getString(map, "tid"));
                result.setCode("SUCCESS");
            }else if("0".equals(MapUtils.getString(map, "cardStatus"))){
                updateOilCoupon(myOilCoupon,"0",null,null);
                result.setCode("SUCCESS");
            }else{
                result.setCode("FAIL");
                result.setFailMessage("cardStatus状态不是已使用！");
            }
        }else{
            result.setCode("FAIL");
            result.setFailMessage(MapUtils.getString(map, "rcDetail"));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void updateOilCoupon(MyOilCoupon myOilCoupon,String useState,String useDate,String tId) throws Exception{
        if(WsdUtils.isNotEmpty(myOilCoupon)){
            myOilCoupon.setUseState(useState);
            myOilCoupon.setUseDate(useDate);
            myOilCoupon.setTId(tId);
            myOilCouponRepository.save(myOilCoupon);
            logger.info("电子券状态修改成功！");
        }
    }

}
