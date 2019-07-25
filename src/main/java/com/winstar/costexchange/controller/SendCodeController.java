package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.costexchange.service.MoveBusinessService;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class SendCodeController {

    private RestTemplate restTemplate;
    private MoveBusinessService moveBusinessService;
    @Value("${info.sendCodeUrl}")
    private String sendCodeUrl;

    @Autowired
    public SendCodeController(RestTemplate restTemplate, MoveBusinessService moveBusinessService) {
        this.restTemplate = restTemplate;
        this.moveBusinessService = moveBusinessService;
    }

    @GetMapping("/sendMobileCode")
    public Result sendMobileCode(HttpServletRequest request, @RequestParam("mobile") String mobile) {
        String accountId = (String) request.getAttribute("accountId");
        if (StringUtils.isEmpty(mobile)) {
            return Result.fail("mobile_empty", "手机号不合法");
        }
        if (mobile.length() != 11) {
            return Result.fail("mobile_error", "手机号错误");
        }
        if (moveBusinessService.check(accountId) <= 0) {
            return Result.fail("account_non_chance", "您无资格享受活动，请先下单");
        }
        Map codeInfo;
        try {
            codeInfo = restTemplate.getForEntity(sendCodeUrl + "/" + mobile, Map.class).getBody();
            log.info("{codeInfo}===" + JSON.toJSONString(codeInfo));
            if (!codeInfo.get("retCode").equals("0000")) {
                return Result.fail("code_failed", String.valueOf(codeInfo.get("retMsg")));
            }
        } catch (Exception e) {
            return Result.fail("code_failed", "请求验证码失败");
        }
        return new Result(String.valueOf(codeInfo.get("retCode")), String.valueOf(codeInfo.get("retMsg")), null);

    }


}
