package com.winstar.costexchange.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.entity.MoveBusinessRecord;
import com.winstar.costexchange.repository.MoveBusinessRecordRepository;
import com.winstar.costexchange.service.MoveBusinessService;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/cbc/move/")
public class MoveBusinessController {

    private final
    HttpServletRequest request;
    private final
    AccountService accountService;
    private final
    RestTemplate restTemplate;
    private final
    AccountCouponService accountCouponService;
    private final
    AccountCouponRepository accountCouponRepository;
    private final
    MoveBusinessService moveBusinessService;
    private final
    MoveBusinessRecordRepository moveBusinessRecordRepository;
    @Value("${info.handleBusinessUrl}")
    private String handleBusinessUrl;
    private static final String templateId = "000000006b96f5ca016bb166e6f60001";

    @Autowired
    public MoveBusinessController(HttpServletRequest request, AccountService accountService, RestTemplate restTemplate, AccountCouponService accountCouponService, AccountCouponRepository accountCouponRepository, MoveBusinessService moveBusinessService, MoveBusinessRecordRepository moveBusinessRecordRepository) {
        this.request = request;
        this.accountService = accountService;
        this.restTemplate = restTemplate;
        this.accountCouponService = accountCouponService;
        this.accountCouponRepository = accountCouponRepository;
        this.moveBusinessService = moveBusinessService;
        this.moveBusinessRecordRepository = moveBusinessRecordRepository;
    }

    @RequestMapping(value = "exchange")
    public Result send(@RequestParam String phone, @RequestParam String code) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        log.info("开始办理话费业务：accountId is :{}，phone is：{},code is :{}", accountId, phone, code);
        int remainingTimes = moveBusinessService.check(accountId);
        if (remainingTimes <= 0) {
            return Result.fail("user_no_times", "用户无参与资格");
        }
        Map map = handleBusiness(phone, code);
        if (!"0000".equals(MapUtils.getString(map, "retCode"))) {
            return Result.fail(MapUtils.getString(map, "retCode"), MapUtils.getString(map, "retMsg"));
        }
        boolean success = sendCoupon(accountId, phone);
        if (!success) {
            throw new NotRuleException("发券失败");
        }
        return Result.success(null);
    }

    @RequestMapping("check")
    public Result check() throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        int remainingTimes = moveBusinessService.check(accountId);
        if (remainingTimes < 0) {
            return Result.fail("user_not_bought", "用户未购买");
        } else if (remainingTimes == 0) {
            return Result.fail("user_not_times", "用户无参与资格");
        }
        return Result.success(null);

    }


    /**
     * 调取话费平台办理业务
     */
    private Map handleBusiness(String phone, String code) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("phone", phone);
        reqMap.put("code", code);
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(handleBusinessUrl, reqMap, Map.class);
        log.info("请求接口结果：" + mapResponseEntity.getBody().toString());
        return mapResponseEntity.getBody();
    }

    /**
     * 发放优惠券
     */
    private boolean sendCoupon(String accountId, String phone) {
        log.info("给用户发放优惠券：accountId is {} and templateId is {}", accountId, templateId);
        ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, "1");
        Map map = responseEntity.getBody();
        if (MapUtils.getString(map, "code").equals("SUCCESS")) {
            log.info("获取优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "yjx", accountId, null, null);
            accountCouponRepository.save(accountCoupons);
            log.info("发放优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            MoveBusinessRecord moveBusinessRecord = new MoveBusinessRecord();
            moveBusinessRecord.setAccountId(accountId);
            moveBusinessRecord.setPhone(phone);
            moveBusinessRecord.setCreatedAt(new Date());
            moveBusinessRecordRepository.save(moveBusinessRecord);
        }
        return false;
    }


}
