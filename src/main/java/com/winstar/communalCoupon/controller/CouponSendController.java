package com.winstar.communalCoupon.controller;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.CouponSendRecord;
import com.winstar.communalCoupon.entity.TemplateRule;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.CouponSendRecordRepository;
import com.winstar.communalCoupon.repository.TemplateRuleRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import com.winstar.vo.SendCouponVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    AccountCouponService accountCouponService;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Autowired
    CouponSendRecordRepository couponSendRecordRepository;

    @Autowired
    RedisTools redisTools;

    @Autowired
    TemplateRuleRepository templateRuleRepository;

    /**
     * 领取优惠券
     */
    @RequestMapping(value = "send/receive", method = RequestMethod.POST)
    public Result receiveSendCoupon(@RequestBody Map map, HttpServletRequest request) throws NotRuleException {
        String recordId = MapUtils.getString(map, "recordId");
        if (StringUtils.isEmpty(recordId)) {
            logger.info("赠送记录id不能为空！");
            return Result.fail("recordId_not_found", "赠送记录id不能为空！");
        }
        String listKey = "sendCoupons:" + recordId;
        Object popValue = redisTools.leftPop(listKey);
        if (ObjectUtils.isEmpty(popValue)) {
            logger.info("你手也太慢了吧，机会已经被别人抢走喽！");
            redisTools.remove(listKey);
            return Result.fail("coupon_get_other", "你手也太慢了吧，机会已经被别人抢走喽！");
        }
        logger.info("领取别人赠送的优惠券，对应的赠送记录id is {}", recordId);
        CouponSendRecord couponSendRecord = couponSendRecordRepository.findOne(recordId);
        if (ObjectUtils.isEmpty(couponSendRecord)) {
            logger.info("优惠券赠送记录不存在！");
            return Result.fail("coupon_send_record_not_found", "优惠券赠送记录不存在！");
        }
        if(!StringUtils.isEmpty(couponSendRecord.getReceiveAccountId())){
            logger.info("优惠券已领取！");
            return Result.fail("coupon_received", "优惠券已领取！");
        }
        if(StringUtils.isEmpty(couponSendRecord.getReceiveAccountId()) && (new Date().getTime() - couponSendRecord.getSendTime().getTime()) >= 24 * 60 * 60 * 1000){
            logger.info("优惠券超时未领取已退回！");
            return Result.fail("coupon_back", "优惠券超时未领取已退回！");
        }
        Object switchValue = redisTools.get(couponSendRecord.getTemplateId() + "_switch");
        if (ObjectUtils.isEmpty(switchValue)) {
            logger.info("模板赠送开关值不存在，需要查询数据库！");
            TemplateRule templateRule = templateRuleRepository.findByTemplateId(couponSendRecord.getTemplateId());
            switchValue = !ObjectUtils.isEmpty(templateRule) ? templateRule.getGiftable() : "no";
            redisTools.set(couponSendRecord.getTemplateId() + "_switch", switchValue);
        }
        if (switchValue.toString().equals("no")) {
            logger.info("优惠券不支持赠送！");
            return Result.fail("coupon_not_support", "优惠券不支持赠送！");
        }
        AccountCoupon accountCoupon = accountCouponRepository.findByCouponId(couponSendRecord.getCouponId());
        if (ObjectUtils.isEmpty(accountCoupon)) {
            logger.info("优惠券不存在！");
            return Result.fail("coupon_not_found", "优惠券不存在！");
        }
        if (!accountCoupon.getState().equals(AccountCouponService.SENDING)) {
            logger.info("优惠券未赠送，不能领取！");
            return Result.fail("coupon_not_sending", "优惠券未赠送，不能领取！");
        }
        if ((new Date().getTime() - accountCoupon.getEndTime().getTime()) >= 0) {
            logger.info("优惠券已过期，不能领取！");
            return Result.fail("coupon_expired", "优惠券已过期，不能领取！");
        }
        String accountId = accountService.getAccountId(request);
        String openId = accountService.getOpenId(request);
        if (accountCoupon.getAccountId().equals(accountId)) {
            logger.info("领取人不能是自己！");
            redisTools.rightPush(listKey, "1");
            return Result.fail("coupon_receiver_not_sender", "领取人不能是自己！");
        }
        accountCoupon.setAccountId(accountId);
        accountCoupon.setState(AccountCouponService.NORMAL);
        couponSendRecord.setReceiveAccountId(accountId);
        couponSendRecord.setReceiverAccountOpenid(openId);
        couponSendRecord.setReceiveTime(new Date());
        accountCouponService.saveCouponAndRecord(accountCoupon, couponSendRecord);
        logger.info("赠送的优惠券领取成功！优惠券id{}" + couponSendRecord.getCouponId());
        return Result.success(couponSendRecord);
    }

    /**
     * 赠送优惠券
     */
    @PostMapping(value = "send")
    public Result sendCoupon(@RequestBody @Valid SendCouponVo sendCouponVo, HttpServletRequest request) throws NotRuleException {
        logger.info("领取别人赠送的优惠券id={}和对应的模板id={}", sendCouponVo.getCouponId(), sendCouponVo.getTemplateId());
        Object switchValue = redisTools.get(sendCouponVo.getTemplateId() + "_switch");
        if (ObjectUtils.isEmpty(switchValue)) {
            logger.info("模板赠送开关值不存在，需要查询数据库！");
            TemplateRule templateRule = templateRuleRepository.findByTemplateId(sendCouponVo.getTemplateId());
            if (!ObjectUtils.isEmpty(templateRule)) {
                switchValue = templateRule.getGiftable();
            } else {
                switchValue = "no";
            }
            redisTools.set(sendCouponVo.getTemplateId() + "_switch", switchValue);
        }
        if (!"yes".equals(switchValue.toString())) {
            logger.info("优惠券不支持赠送！");
            return Result.fail("coupon_not_support", "优惠券不支持赠送！");
        }
        AccountCoupon accountCoupon = accountCouponRepository.findByCouponId(sendCouponVo.getCouponId());
        if (ObjectUtils.isEmpty(accountCoupon)) {
            logger.info("优惠券不存在！");
            return Result.fail("coupon_not_found", "优惠券不存在！");
        }
        if (AccountCouponService.SENDING.equals(accountCoupon.getState())) {
            logger.info("优惠券已是赠送状态！");
            return Result.fail("coupon_state_sending", "优惠券已是赠送状态！");
        }
        if ((System.currentTimeMillis() - accountCoupon.getEndTime().getTime()) >= 0) {
            logger.info("优惠券已过期，不能赠送！");
            return Result.fail("coupon_expired", "优惠券已过期，不能赠送！");
        }
        String accountId = accountService.getAccountId(request);
        String openId = accountService.getOpenId(request);
        CouponSendRecord couponSendRecord = new CouponSendRecord();
        couponSendRecord.setSendAccountId(accountId);
        couponSendRecord.setSendAccountOpenid(openId);
        couponSendRecord.setCouponId(sendCouponVo.getCouponId());
        couponSendRecord.setTemplateId(sendCouponVo.getTemplateId());
        CouponSendRecord record = couponSendRecordRepository.save(couponSendRecord);
        String listKey = "sendCoupons:" + record.getId();
        redisTools.remove(listKey);
        redisTools.rightPush(listKey, "1");
        logger.info("赠送优惠券成功！优惠券id{}" + sendCouponVo.getCouponId());
        return Result.success(record);
    }

    /**
     * 领取优惠券
     */
    @RequestMapping(value = "nowSuccess", method = RequestMethod.POST)
    public Result nowBackCoupon(@RequestBody Map map, HttpServletRequest request) throws NotRuleException {
        String recordId = MapUtils.getString(map, "recordId");
        logger.info("未分享出去立即回退优惠券，不能让优惠券不翼而飞！recordId is {}", recordId);
        if (StringUtils.isEmpty(recordId)) {
            logger.info("赠送记录id不能为空！");
            return Result.fail("recordId_not_found", "赠送记录id不能为空！");
        }
        String accountId = accountService.getAccountId(request);
        CouponSendRecord couponSendRecord = couponSendRecordRepository.findOne(recordId);
        if (ObjectUtils.isEmpty(couponSendRecord)) {
            logger.info("优惠券赠送记录不存在！");
            return Result.fail("coupon_send_record_not_found", "优惠券赠送记录不存在！");
        }
        if(!couponSendRecord.getSendAccountId().equals(accountId)){
            logger.info("不是你赠送出去的优惠你干嘛调接口退回！");
            return Result.fail("coupon_not_your_send", "优惠券非你赠送！");
        }
        AccountCoupon accountCoupon = accountCouponRepository.findByCouponId(couponSendRecord.getCouponId());
        if (ObjectUtils.isEmpty(accountCoupon)) {
            logger.info("优惠券不存在！");
            return Result.fail("coupon_not_found", "优惠券不存在！");
        }
        if(!accountCoupon.getAccountId().equals(accountId)){
            logger.info("优惠券已经被领取了，不在你名下了！");
            return Result.fail("coupon_not_your_send", "优惠券已经被领取了，不在你名下了！");
        }
        if(!accountCoupon.getState().equals(AccountCouponService.NORMAL)){
            logger.info("优惠券非赠送状态，不能退回！");
            return Result.fail("coupon_not_send_state", "优惠券非赠送状态，不能退回！");
        }
        couponSendRecord.setSendTime(new Date());
        accountCoupon.setSendTime(new Date());
        accountCoupon.setState(AccountCouponService.SENDING);
        accountCouponService.saveCouponAndRecord(accountCoupon, couponSendRecord);
        return Result.success(new HashMap<>());
    }

}
