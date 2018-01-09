package com.winstar.oil.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.oil.utils.RequestSvcInfoUtils;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.object.ObjectFactory;
import ws.object.SvcInfo;
import ws.result.Result;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private final static ObjectFactory objectFactory = new ObjectFactory();

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity checkCard(@RequestParam String pan) throws Exception{
        logger.info("电子券使用进行核销。。。");
        Result result = new Result();
        SvcInfo req = new SvcInfo();
        req.setTxnId(objectFactory.createSvcInfoTxnId("shell1"));
        req.setPan(objectFactory.createSvcInfoPan(pan));
        SvcInfo svcInfo = RequestSvcInfoUtils.getSvcInfo(req);
        logger.info("rc:" + svcInfo.getRc().getValue());
        if(svcInfo.getRc().getValue().equals("00") || svcInfo.getRc().getValue().equals("43")){ //00代表成功，43代表已核销
            if("1".equals(svcInfo.getCardStatus().getValue())){ //卡状态 0代表正常，1代表已使用，2代表其他
                String useDate;
                if(WsdUtils.isNotEmpty(svcInfo.getTxnDate().getValue()) && WsdUtils.isNotEmpty(svcInfo.getTxnTime().getValue())){
                    useDate = WsdUtils.formatDate(svcInfo.getTxnDate().getValue() + svcInfo.getTxnTime().getValue(),"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
                }else{
                    useDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                }
                updateOilCoupon(pan,"1",useDate,svcInfo.getTid().getValue());
                result.setCode("SUCCESS");
            }else if("0".equals(svcInfo.getCardStatus().getValue())){
                updateOilCoupon(pan,"0",null,null);
                result.setCode("SUCCESS");
            }else{
                result.setCode("FAIL");
                result.setFailMessage("cardStatus状态不是已使用！");
            }
        }else{
            result.setCode("FAIL");
            result.setFailMessage(svcInfo.getRcDetail().getValue());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "{pan}/handler",method = RequestMethod.GET)
    public ResponseEntity checkCardHandler(@PathVariable String pan) throws Exception{
        Result result = new Result();
        SvcInfo req = new SvcInfo();
        req.setTxnId(objectFactory.createSvcInfoTxnId("shell1"));
        req.setPan(objectFactory.createSvcInfoPan(pan));
        SvcInfo svcInfo = RequestSvcInfoUtils.getSvcInfo(req);
        logger.info("rc:" + svcInfo.getRc().getValue());
        if(svcInfo.getRc().getValue().equals("00") || svcInfo.getRc().getValue().equals("43")){ //00代表成功，43代表已核销
            if("1".equals(svcInfo.getCardStatus().getValue())){ //卡状态 0代表正常，1代表已使用，2代表其他
                String useDate;
                if(WsdUtils.isNotEmpty(svcInfo.getTxnDate().getValue()) && WsdUtils.isNotEmpty(svcInfo.getTxnTime().getValue())){
                    useDate = WsdUtils.formatDate(svcInfo.getTxnDate().getValue() + svcInfo.getTxnTime().getValue(),"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
                }else{
                    useDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                }
                updateOilCoupon(pan,"1",useDate,svcInfo.getTid().getValue());
                result.setCode("SUCCESS");
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/onlySearch/{pan}",method = RequestMethod.GET)
    public ResponseEntity onlySearch(@PathVariable String pan) throws Exception{
        logger.info("查询易通电子券状态码。。。");
        SvcInfo req = new SvcInfo();
        req.setTxnId(objectFactory.createSvcInfoTxnId("shell1"));
        req.setPan(objectFactory.createSvcInfoPan(pan));
        SvcInfo svcInfo = RequestSvcInfoUtils.getSvcInfo(req);
        return new ResponseEntity<>(JSON.toJSONString(svcInfo), HttpStatus.OK);
    }

    private void updateOilCoupon(String pan,String useState,String useDate,String tId) throws Exception{
        logger.info("加密后的油券号码：" + AESUtil.encrypt(pan, AESUtil.dekey));
        MyOilCoupon myOilCoupon = myOilCouponRepository.findByPan(AESUtil.encrypt(pan, AESUtil.dekey));
        if(WsdUtils.isNotEmpty(myOilCoupon)){
            myOilCoupon.setUseState(useState);
            myOilCoupon.setUseDate(useDate);
            myOilCoupon.setTId(tId);
            myOilCouponRepository.save(myOilCoupon);
            logger.info("电子券状态修改成功！");
        }
    }

}
