package com.winstar.mobile;

import com.alibaba.fastjson.JSON;
import com.winstar.mobile.domain.CmResult;
import com.winstar.mobile.domain.SendMessageRequestDomain;
import com.winstar.mobile.domain.VerifyRequestDomain;
import com.winstar.mobile.service.CmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zl on 2020/3/6
 */
@RestController
@RequestMapping("/api/v1/cbc/test/mobile")
@Slf4j
public class TestController {

    @Autowired
    CmService cmService;

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public CmResult getToken(String mobile){
        VerifyRequestDomain domain = new VerifyRequestDomain();
        domain.setPackageId("96044072");
        domain.setProductId("69900043");
        domain.setSerialNumber(mobile);
        log.info("参数：" + JSON.toJSONString(domain));
        return cmService.verify(domain);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public CmResult sendMessage(String mobile){
        SendMessageRequestDomain domain = new SendMessageRequestDomain();
        domain.setSerialNumber(mobile);
        domain.setBusinessCode("96044072");
        log.info("参数：" + JSON.toJSONString(domain));
        return cmService.sendMessage(domain);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public CmResult create(String mobile, String smsCode){
        VerifyRequestDomain domain = new VerifyRequestDomain();
        domain.setPackageId("96044072");
        domain.setProductId("69900043");
        domain.setSerialNumber(mobile);
        domain.setSmsCode(smsCode);
        log.info("参数：" + JSON.toJSONString(domain));
        return cmService.create(domain);
    }

}
