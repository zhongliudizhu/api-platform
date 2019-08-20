package com.winstar.costexchange.controller;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.costexchange.entity.FailSendRecord;
import com.winstar.costexchange.entity.MoveBusinessRecord;
import com.winstar.costexchange.repository.FailSendRecordRepository;
import com.winstar.costexchange.repository.MoveBusinessRecordRepository;
import com.winstar.costexchange.service.MoveBusinessService;
import com.winstar.exception.NotRuleException;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.winstar.utils.ValidatorParameterUtils.isMobile;

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
    MoveBusinessService moveBusinessService;
    private final
    MoveBusinessRecordRepository moveBusinessRecordRepository;
    private final
    FailSendRecordRepository failSendRecordRepository;
    @Value("${info.handleBusinessUrl}")
    private String handleBusinessUrl;
    private static final String[] templateIds = {"000000006b96f5ca016cacf4ad11003c", "000000006b96f5ca016cacf63e46003d", "000000006b96f5ca016cacfbc124003e"};

    @Autowired
    public MoveBusinessController(HttpServletRequest request, AccountService accountService, RestTemplate restTemplate, AccountCouponService accountCouponService, MoveBusinessService moveBusinessService, MoveBusinessRecordRepository moveBusinessRecordRepository, FailSendRecordRepository failSendRecordRepository) {
        this.request = request;
        this.accountService = accountService;
        this.restTemplate = restTemplate;
        this.accountCouponService = accountCouponService;
        this.moveBusinessService = moveBusinessService;
        this.moveBusinessRecordRepository = moveBusinessRecordRepository;
        this.failSendRecordRepository = failSendRecordRepository;
    }

    @RequestMapping(value = "exchange")
    public Result send(@RequestParam String phone, @RequestParam String code) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        log.info("开始办理话费业务：accountId is :{}，phone is：{},code is :{}", accountId, phone, code);
        if (!isMobile(phone)) {
            return Result.fail("param_error", "手机号错误");
        }
        int remainingTimes = moveBusinessService.check(accountId);
        if (remainingTimes <= 0) {
            return Result.fail("user_no_times", "用户无参与资格");
        }
        Map map = handleBusiness(phone, code);
        if (!"0000".equals(MapUtils.getString(map, "retCode"))) {
            return Result.fail(MapUtils.getString(map, "retCode"), MapUtils.getString(map, "retMsg"));
        }
        //记录
        MoveBusinessRecord moveBusinessRecord = new MoveBusinessRecord();
        moveBusinessRecord.setAccountId(accountId);
        moveBusinessRecord.setPhone(phone);
        moveBusinessRecord.setCreatedAt(new Date());
        moveBusinessRecordRepository.save(moveBusinessRecord);
        boolean success = true;
        for (String templateId : templateIds) {
            if (!sendCoupon(accountId, templateId)) {
                success = false;
            }
        }
        if (!success) {
            return Result.fail("send_coupon_fail", "发券失败");
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
    private boolean sendCoupon(String accountId, String templateId) {
        SendCouponDomain domain = new SendCouponDomain(templateId, accountId, AccountCoupon.TYPE_YJX, "1", null, null);
        List<AccountCoupon> accountCoupons = accountCouponService.sendCoupon(domain, null);
        if (!ObjectUtils.isEmpty(accountCoupons)) {
            return true;
        }
        FailSendRecord failSendRecord = new FailSendRecord();
        failSendRecord.setAccountId(accountId);
        failSendRecord.setTemplateId(templateId);
        failSendRecord.setCreatedAt(new Date());
        failSendRecord.setFailMsg("模板id获取不到优惠券");
        failSendRecordRepository.save(failSendRecord);
        log.error("用户 {} 发放优惠券失败 ！！", accountId);
        return false;
    }


}
