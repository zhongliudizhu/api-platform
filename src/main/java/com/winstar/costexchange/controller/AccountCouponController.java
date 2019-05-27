package com.winstar.costexchange.controller;

import com.winstar.costexchange.entity.AccountCoupon;
import com.winstar.costexchange.repository.AccountCouponRepository;
import com.winstar.utils.WebUitl;
import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zl on 2019/5/27
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class AccountCouponController {

    private static final Logger logger = LoggerFactory.getLogger(AccountCouponController.class);

    @Autowired
    AccountCouponRepository accountCouponRepository;

    /**
     * 查询优惠券列表
     */
    @RequestMapping(value = "getCoupons", method = RequestMethod.GET)
    public Result getCoupons(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer nextPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "normal") String state){
        if(!state.equals("normal") && !state.equals("used") && !state.equals("expired")){
            logger.info("状态值错误！");
            return Result.fail("state_not_auth", "状态值错误！");
        }
        String accountId = (String) request.getAttribute("accountId");
        Pageable pageable = WebUitl.buildPageRequest(nextPage, pageSize, null);
        Page<AccountCoupon> accountCouponPage = accountCouponRepository.findByAccountIdAndShowStatusAndState(accountId, "yes", state, pageable);
        return Result.success(accountCouponPage);
    }

}
