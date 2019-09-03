package com.winstar.communalCoupon.controller;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by zl on 2019/9/3
 * 禁止往外暴露此接口，非常人不能调用
 */
@RequestMapping("/api/v1/noAuth/")
@RestController
@Slf4j
public class AccountCouponModifyController {

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @RequestMapping(value = "modifyCoupon", method = RequestMethod.POST)
    public Result modifyCoupon(@RequestBody Map map, HttpServletRequest request) throws ParseException {
        String authority = request.getHeader("authority");
        if(!"Auth_5VMAN74584AAA".equals(authority)){
            log.info("你是谁？");
            return Result.fail("error", "你是谁？");
        }
        String id = MapUtils.getString(map, "id");
        if(StringUtils.isEmpty(id)){
            log.info("id错误");
            return Result.fail("error", "id错误");
        }
        AccountCoupon accountCoupon = accountCouponRepository.findOne(id);
        if(ObjectUtils.isEmpty(accountCoupon)){
            log.info("coupon不存在");
            return Result.fail("error", "coupon不存在");
        }
        String state = MapUtils.getString(map, "state");
        if(!StringUtils.isEmpty(state)){
            accountCoupon.setState(state);
        }
        String orderId = MapUtils.getString(map, "orderId");
        accountCoupon.setOrderId(!StringUtils.isEmpty(orderId) ? orderId : null);
        String useDate = MapUtils.getString(map, "useDate");
        accountCoupon.setUseDate(!StringUtils.isEmpty(useDate) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(useDate) : null);
        accountCoupon = accountCouponRepository.save(accountCoupon);
        return Result.success(accountCoupon);
    }

}
