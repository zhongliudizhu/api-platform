package com.winstar.weekendBrand.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.vo.Result;
import com.winstar.weekendBrand.param.ReceiveRedPackageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.winstar.user.utils.ServiceManager.accountService;

/**
 * 名称： AuthController
 * 作者： dpw
 * 日期： 2018-01-26 15:08
 * 描述： 信息卡绑定
 **/
@RestController
@RequestMapping("/api/v1/cbc/weekendBrand")
@Slf4j
public class WeekendBarndController {

    /**
     * 获取活动状态
     *
     * @return Result
     */
    @GetMapping("/getWeekendBrandStatus")
    public Map getWeekendBrandStatus() {
        int activityWeekDay = 6;

        int leftDays = ServiceManager.weekEndBrandService.calculateWeek(activityWeekDay, Integer.valueOf(DateUtil.getWeekOfDate(new Date())));

        Map<String, Object> map = new HashMap();
        map.put("time", new Date().getTime());
        map.put("leftDays", leftDays);
        map.put("state", leftDays == 0);
        map.put("saturday", DateUtil.addDay(DateUtil.getWeekBegin(), leftDays == 6 ? 12 : 5).getTime());

        return map;
    }

    /**
     * 领取优惠券红包
     *
     * @param receiveRedPackageParam receiveRedPackageParam
     * @return MyCoupon
     */
    @PostMapping("/receiveRedPackage")
    public Result updateMobile(@RequestBody ReceiveRedPackageParam receiveRedPackageParam, HttpServletRequest request) throws NotRuleException {

        String accountId = ServiceManager.accountService.getAccountId(request);
        if (null == receiveRedPackageParam) {
            log.info("参数不合法！！！");
            throw new NotRuleException("paramNotAllowNull");
        }
        //todo 测试时关闭
       /* if (!ServiceManager.smsService.verifySms(receiveRedPackageParam.getUpdateAccountParam())) {
            log.info("验证码错误！！");
            throw new NotRuleException("verifyCodeIsError");
        }*/
        Account account = ServiceManager.accountRepository.findOne(accountId);

        if (accountService.checkBindMobileUnique(receiveRedPackageParam.getMobile())) {
            account.setMobile(receiveRedPackageParam.getMobile());
            account.setUpdateTime(new Date());

            ServiceManager.redisTools.remove(ServiceManager.REDIS_KEY_FIND_ACCOUNT_BY_ID + account.getId());
            ServiceManager.redisTools.remove(ServiceManager.REDIS_KEY_FIND_ACCOUNT_BY_OPENID + account.getOpenid());
            account = ServiceManager.accountRepository.save(account);
        }
        return ServiceManager.orderRedPackageInfoService.receiveOrderRedPackage(account.getId(), receiveRedPackageParam.getOrderId());
    }

}
