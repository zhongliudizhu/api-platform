package com.winstar.user.controller;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.OneMoneyCouponRecord;
import com.winstar.user.utils.ServiceManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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
     * @param request
     * @return
     * @throws NotRuleException
     */
    @PostMapping(value = "/save", produces = "application/json")
    public OneMoneyCouponRecord getToken(String orderId, HttpServletRequest request) throws NotRuleException {
        if (StringUtils.isEmpty(orderId)) {
            throw new NotRuleException("orderId");
        }
        Integer count = ServiceManager.oneMoneyCouponRecordRepository.countByAccountId(ServiceManager.accountService.getAccountId(request));
        if (count > 0)
            throw new NotRuleException("justOnce.oneMoneyCoupon");

        return ServiceManager.oneMoneyCouponRecordService.insertRecord(ServiceManager.accountService.getAccountId(request), orderId);
    }

    /**
     * 是否有购买资格
     *
     * @param request
     * @return
     * @throws NotRuleException
     */
    @PostMapping(value = "/checkBuyAuth", produces = "application/json")
    public boolean checkBuyAuth(HttpServletRequest request) throws NotRuleException {
        return ServiceManager.oneMoneyCouponRecordService.checkBuyAuth(ServiceManager.accountService.getAccountId(request));
    }
}
