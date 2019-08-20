package com.winstar.activityCenter.controller;

import com.winstar.activityCenter.entity.CommunalActivity;
import com.winstar.activityCenter.repository.CommunalActivityRepository;
import com.winstar.activityCenter.service.CommunalActivityService;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
import com.winstar.vo.ReceiveCouponVo;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zl on 2019/7/3
 * 领券中心
 */
@RestController
@RequestMapping("/api/v1/cbc/receive")
@Slf4j
public class ReceiveCouponCenterController {

    @Autowired
    RedisTools redisTools;

    @Autowired
    CommunalActivityRepository communalActivityRepository;

    @Autowired
    AccountCouponService accountCouponService;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    CommunalActivityService activityService;

    @RequestMapping(value = "/setNumber", method = RequestMethod.GET)
    public void setNumber(String activityId, Integer number) {
        String listKey = "sendCoupons:" + activityId;
        redisTools.remove(listKey);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add("1");
        }
        redisTools.rightPushAll(listKey, list);
        log.info(listKey + "的集合长度：" + redisTools.size(listKey));
    }

    /**
     * 抢购领券
     */
    @RequestMapping(value = "/coupon/purchase", method = RequestMethod.POST)
    public Result purchaseCoupon(@RequestBody @Valid ReceiveCouponVo receiveCouponVo, HttpServletRequest request) throws NotRuleException {
        log.info("优惠券抢购开始==========");
        String accountId = accountService.getAccountId(request);
        String activityId = receiveCouponVo.getActivityId();
        if (!redisTools.setIfAbsent(activityId + "<->" + accountId, 5)) {
            log.info("5秒之内禁止重复抢购！");
            return Result.fail("click_fast", "请勿频繁点击！");
        }
        CommunalActivity activity = communalActivityRepository.findOne(activityId);
        Result validatorResult = activityService.validatorActivity(activity);
        if (!ObjectUtils.isEmpty(validatorResult)) {
            return validatorResult;
        }
        if (!activity.getType().equals("2")) {
            log.info("此活动非限量抢购活动！");
            return Result.fail("activity_not_purchase", "此活动非限量抢购活动！");
        }
        String listKey = "awards:" + activityId;
        Object popValue = redisTools.leftPop(listKey);
        if (ObjectUtils.isEmpty(popValue)) {
            log.info("券已经被抢完了，没有了，下次再来吧！");
            redisTools.remove(listKey);
            return Result.fail("coupon_over", "券已经被抢完了，没有了，下次再来吧！");
        }
        Long result = redisTools.add("accountId", activityId + "-is-purchase-" + accountId);
        if (result > 0) {
            log.info("抢券成功！");
            List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndActivityId(accountId, activityId);
            if (!ObjectUtils.isEmpty(accountCoupons)) {
                log.info("该活动用户已经领过券，不能再领取了：accountId is {} and activityId is {}", accountId, activityId);
                return Result.fail("activity_coupon_receive", "您已经领过券了，不能重复领取！");
            }
            SendCouponDomain domain = new SendCouponDomain(activity.getCouponTemplateId(), accountId, AccountCoupon.TYPE_YJX, "1", activityId, null);
            accountCouponService.sendCoupon(domain, redisTools);
            String numberKey = "activity" + activityId;
            if (redisTools.exists(numberKey)) {
                redisTools.set(numberKey, (Integer) redisTools.get(numberKey) + 1);
            }
            return Result.success(new HashMap<>());
        }
        log.info("已经抢过一次了，把占用的名额恢复到待抢列表中！");
        redisTools.rightPush(listKey, 1);
        return Result.fail("activity_coupon_receive", "您已经抢过了！");
    }

    /**
     * 普通领券
     */
    @RequestMapping(value = "/coupon/normal", method = RequestMethod.POST)
    public Result receiveCoupon(@RequestBody @Valid ReceiveCouponVo receiveCouponVo, HttpServletRequest request) throws NotRuleException {
        log.info("优惠券领取开始==========");
        String accountId = accountService.getAccountId(request);
        String activityId = receiveCouponVo.getActivityId();
        if (!redisTools.setIfAbsent(activityId + "<->" + accountId, 5)) {
            log.info("5秒之内禁止重复抢购！");
            return Result.fail("click_fast", "请勿频繁点击！");
        }
        CommunalActivity activity = communalActivityRepository.findOne(activityId);
        Result validatorResult = activityService.validatorActivity(activity);
        if (!ObjectUtils.isEmpty(validatorResult)) {
            return validatorResult;
        }
        if (!activity.getType().equals("1")) {
            log.info("此活动非长期领券活动！");
            return Result.fail("activity_not_normal", "此活动非长期领券活动！");
        }
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndActivityId(accountId, activityId);
        if (!ObjectUtils.isEmpty(accountCoupons)) {
            log.info("该活动用户已经领过券，不能再领取了：accountId is {} and activityId is {}", accountId, activityId);
            return Result.fail("activity_coupon_receive", "您已经领过券了，不能重复领取！");
        }
        log.info("该活动用户未领过券，可以领取：accountId is {} and activityId is {}", accountId, activityId);
        SendCouponDomain domain = new SendCouponDomain(activity.getCouponTemplateId(), accountId, AccountCoupon.TYPE_YJX, "1", activityId, null);
        accountCouponService.sendCoupon(domain, redisTools);
        return Result.success(new HashMap<>());
    }

    @PostMapping("coupon/ccb")
    public Result ccbReceive(@RequestBody ReceiveCouponVo receiveCouponVo, HttpServletRequest request) throws NotRuleException {
        log.info("优惠券抢购开始==========");
        String accountId = accountService.getAccountId(request);
        Account account = ServiceManager.accountService.findOne(accountId);
        String activityId = receiveCouponVo.getActivityId();
        if (null == account || StringUtils.isEmpty(account.getAuthInfoCard())) {
            return Result.fail("Not_found_user", "用户未认证交安卡！");
        }
        if (!redisTools.setIfAbsent(activityId + "<->" + accountId, 5)) {
            log.info("5秒之内禁止重复抢购！");
            return Result.fail("click_fast", "请勿频繁点击！");
        }
        CommunalActivity activity = communalActivityRepository.findOne(activityId);
        Result validatorResult = activityService.validatorActivity(activity);
        if (!ObjectUtils.isEmpty(validatorResult)) {
            return validatorResult;
        }
        if (!activity.getTarget().equals("ccb")) {
            log.info("此活动非交安卡用户专享活动！");
            return Result.fail("activity_not_purchase", "此活动非交安卡用户专享活动！");
        }
        //所有交安卡专享活动
        List<CommunalActivity> ccbActivities = communalActivityRepository.findAllByStatusAndDelAndTargetAndShowDateBefore("yes", "no", "ccb", new Date());
        //交安卡专享活动Id
        List<String> activityIds = ccbActivities.stream().map(CommunalActivity::getId).collect(Collectors.toList());
        //交安卡活动领取的优惠券
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndActivityIdIn(accountId, activityIds);
        if (!ObjectUtils.isEmpty(accountCoupons)) {
            log.info("交安卡活动用户已经领过券，不能再领取了：accountId is {} and activityId is {}", accountId, accountCoupons.get(0).getActivityId());
            return Result.fail("activity_coupon_receive", "您已经领过交安卡活动专属券了，不能重复领取！");
        }
        log.info("该活动用户未领过券，可以领取：accountId is {} and activityId is {}", accountId, activityId);
        SendCouponDomain domain = new SendCouponDomain(activity.getCouponTemplateId(), accountId, AccountCoupon.TYPE_CCB, "1", activityId, null);
        List<AccountCoupon> sendCouponResult = accountCouponService.sendCoupon(domain, redisTools);
        if (!ObjectUtils.isEmpty(sendCouponResult)) {
            return Result.success(null);
        }
        return Result.fail("send_fail", "发券失败");
    }

}
