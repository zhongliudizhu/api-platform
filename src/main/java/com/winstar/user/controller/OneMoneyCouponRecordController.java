package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.entity.OneMoneyCouponRecord;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author laohu
 **/
@RestController
@RequestMapping("/api/v1/cbc/OneMoneyCouponRecord")
public class OneMoneyCouponRecordController {

    /**
     * 添加购买资格
     *
     * @param orderId
     * @param openid  优驾行openid
     * @param request
     * @return
     * @throws NotRuleException
     */
    @PostMapping(value = "/save", produces = "application/json")
    public OneMoneyCouponRecord saveOneMoneyCouponRecord(String orderId, String openid, HttpServletRequest request) throws NotRuleException {
        if (StringUtils.isEmpty(orderId)) {
            throw new NotRuleException("orderId");
        } else if (StringUtils.isEmpty(openid))
            throw new NotRuleException("openid");

        String accountId = ServiceManager.accountService.findAccountIdByOpenid(openid);
        Integer count = ServiceManager.oneMoneyCouponRecordRepository.countByAccountId(accountId);
        if (count > 0)
            throw new NotRuleException("justOnce.oneMoneyCoupon");

        return ServiceManager.oneMoneyCouponRecordService.insertRecord(accountId, orderId);
    }

    /**
     * 是否有购买资格
     *
     * @param request
     * @return
     * @throws NotRuleException
     */
    @GetMapping(value = "/checkBuyAuth", produces = "application/json")
    public SimpleResult checkBuyAuth(HttpServletRequest request) throws NotRuleException {

        return new SimpleResult(String.valueOf(ServiceManager.oneMoneyCouponRecordService.checkBuyAuth(ServiceManager.accountService.getAccountId(request))));
    }
}
