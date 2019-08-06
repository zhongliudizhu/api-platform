package com.winstar.costexchange.controller;

import com.winstar.costexchange.service.MoveBusinessService;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cbc/phoneNumberCheck")
public class PhoneNumberCheckController {
    @Value("${info.phoneNumberCheckUrl}")
    private String phoneNumberCheckUrl;
    @Autowired
    AccountService accountService;
    @Autowired
    MoveBusinessService moveBusinessService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Result phoneNumberCheck(String phone){
        ResponseEntity<Map> stringResponseEntity = new RestTemplate().getForEntity(phoneNumberCheckUrl + "/" + phone, Map.class);
        System.out.println(stringResponseEntity.getBody());
        if (stringResponseEntity.getBody().get("data") != null) {
//            return Result.success("请您输入正确的手机号");
            return Result.success(stringResponseEntity.getBody().get("data"));
        } else {
            return Result.fail(MapUtils.getString(stringResponseEntity.getBody(), "retCode"), MapUtils.getString(stringResponseEntity.getBody(), "retMsg"));

        }
    }
}
