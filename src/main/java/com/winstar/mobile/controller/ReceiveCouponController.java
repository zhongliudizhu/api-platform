package com.winstar.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.mobile.domain.ReceiveDomain;
import com.winstar.mobile.entity.ReceiveQualification;
import com.winstar.mobile.repository.ReceiveQualificationRepository;
import com.winstar.redis.RedisTools;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/cbc/mobile")
@AllArgsConstructor
public class ReceiveCouponController {

    private RedisTools redisTools;
    private AccountCouponService accountCouponService;
    private ReceiveQualificationRepository qualificationRepository;

    @PostMapping("/receiveCoupons")
    public Result receiveCoupons(@RequestBody @Valid ReceiveDomain receiveDomain, HttpServletRequest request) {
        log.info("入参信息为 {}", JSON.toJSONString(receiveDomain));
        String accountId = (String) request.getAttribute("accountId");
        if (!redisTools.setIfAbsent(accountId + "_receive", 3)) {
            log.info("点击频率太高了吧，请慢点！");
            return Result.fail("click_too_fast", "点击太快，请慢点");
        }
        String validateKey = receiveDomain.getPhone();
        Object data = redisTools.get(validateKey);
        log.info("redis中的 data is {}", data);
        if (ObjectUtils.isEmpty(data) || !("YES").equalsIgnoreCase(data.toString())) {
            log.info("验证资格失败");
            redisTools.remove(accountId + "_receive");
            return Result.fail("validate_failed", "您无资格领券，请先办理套餐");
        }
        String[] templates = receiveDomain.getTemplateId().split(",");
        for (String s : templates) {
            SendCouponDomain domain = new SendCouponDomain(s, accountId, "mobile_12.2", "1", null, null);
            log.info("发放优惠券 。。 templateId is{}", s);
            List<AccountCoupon> couponList = accountCouponService.sendCoupon(domain, null);
            if (CollectionUtils.isEmpty(couponList)) {
                redisTools.remove(accountId + "_receive");
                return Result.fail("give_coupon_failed", "发券失败");
            }
        }
        ReceiveQualification qualification = new ReceiveQualification();
        qualification.setPhone(receiveDomain.getPhone());
        qualification.setReceiveTime(new Date());
        qualification.setPlatType("mobile");
        qualificationRepository.save(qualification);
        redisTools.remove(validateKey);
        return Result.success(true);
    }


}
