package com.winstar.costexchange.controller;

import com.winstar.costexchange.service.MoveBusinessService;
import com.winstar.exception.NotRuleException;
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

import javax.servlet.http.HttpServletRequest;
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
    public Result phoneNumberCheck(HttpServletRequest request, String phone) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        int remainingTimes = moveBusinessService.check(accountId);
        if (remainingTimes <= 0) {
            return Result.fail("user_no_times", "用户无参与资格");
        }
        System.out.println("用户是" + accountId);
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
