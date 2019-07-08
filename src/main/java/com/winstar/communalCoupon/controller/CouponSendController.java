package com.winstar.communalCoupon.controller;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.CouponSendRecord;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.CouponSendRecordRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.exception.NotRuleException;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import com.winstar.vo.SendCouponVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by zl on 2019/7/8
 */
@RestController
@RequestMapping("/api/v1/cbc/coupon")
public class CouponSendController {

    private static final Logger logger = LoggerFactory.getLogger(CouponSendController.class);

    @Autowired
    AccountService accountService;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Autowired
    CouponSendRecordRepository couponSendRecordRepository;

    /**
     * 赠送优惠券
     */
    @RequestMapping(value = "send/receive", method = RequestMethod.POST)
    public Result receiveSendCoupon(@RequestBody @Valid SendCouponVo sendCouponVo, HttpServletRequest request) throws NotRuleException {
        logger.info("领取别人赠送的优惠券》》》》》领取优惠券id{}和对应的模板id{}", sendCouponVo.getCouponId(), sendCouponVo.getTemplateId());
        String accountId = accountService.getAccountId(request);
        String openId = accountService.getOpenId(request);
        AccountCoupon accountCoupon = accountCouponRepository.findByCouponId(sendCouponVo.getCouponId());
        if(ObjectUtils.isEmpty(accountCoupon)){
            logger.info("优惠券不存在！");
            return Result.fail("coupon_not_found", "优惠券不存在！");
        }
        if(!accountCoupon.getState().equals(AccountCouponService.SENDING)){
            logger.info("优惠券未赠送，不能领取！");
            return Result.fail("coupon_not_sending", "优惠券未赠送，不能领取！");
        }
        if((new Date().getTime() - accountCoupon.getEndTime().getTime()) >= 0){
            logger.info("优惠券已过期，不能领取！");
            return Result.fail("coupon_expired", "优惠券已过期，不能领取！");
        }
        CouponSendRecord couponSendRecord = couponSendRecordRepository.findByCouponIdAndIsTimeOut(sendCouponVo.getCouponId(), "no");
        if(ObjectUtils.isEmpty(couponSendRecord)){
            logger.info("优惠券赠送记录不存在！");
            return Result.fail("coupon_send_record_not_found", "优惠券赠送记录不存在！");
        }
        accountCoupon.setAccountId(accountId);
        accountCoupon.setState(AccountCouponService.NORMAL);
        couponSendRecord.setReceiveAccountId(accountId);
        couponSendRecord.setReceiverAccountOpenid(openId);
        couponSendRecord.setReceiveTime(new Date());
        return null;
    }

}
