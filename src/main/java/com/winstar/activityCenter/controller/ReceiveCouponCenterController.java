package com.winstar.activityCenter.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.CommunalActivity;
import com.winstar.activityCenter.repository.CommunalActivityRepository;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.ReceiveCouponVo;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/setNumber", method = RequestMethod.GET)
    public void setNumber(String activityId, Integer number){
        String listKey = "awards:" + activityId;
        redisTools.remove(listKey);
        List<Object> list = new ArrayList<>();
        for(int i=0;i<number;i++){
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
        if(!redisTools.setIfAbsent(activityId + "<->" + accountId, 5)){
            log.info("5秒之内禁止重复抢购！");
            return Result.fail("click_fast", "请勿频繁点击！");
        }
        CommunalActivity activity = communalActivityRepository.findOne(activityId);
        if(ObjectUtils.isEmpty(activity)){
            log.info("活动不存在！");
            return Result.fail("Not_found_activity", "活动不存在！");
        }
        long nowTime = System.currentTimeMillis();
        if(nowTime < activity.getStartDate().getTime()){
            log.info("活动尚未开始！");
            return Result.fail("activity_not_begin", "活动尚未开始！");
        }
        if(nowTime > activity.getEndDate().getTime()){
            log.info("活动已结束！");
            return Result.fail("activity_end", "活动尚已结束！");
        }
        if(activity.getStatus().equals("no")){
            log.info("活动未上架！");
            return Result.fail("activity_is_down", "活动未上架！");
        }
        if(!activity.getType().equals("2")){
            log.info("此活动非限量抢购活动！");
            return Result.fail("activity_not_purchase", "此活动非限量抢购活动！");
        }
        String listKey = "awards:" + activityId;
        Object popValue = redisTools.leftPop(listKey);
        if(ObjectUtils.isEmpty(popValue)){
            log.info("券已经被抢完了，没有了，下次再来吧！");
            return Result.fail("coupon_over", "券已经被抢完了，没有了，下次再来吧！");
        }
        Long result = redisTools.add("accountId", activityId + "-is-purchase-" + accountId);
        if(result > 0){
            log.info("抢券成功！");
            List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndActivityId(accountId, activityId);
            if(!ObjectUtils.isEmpty(accountCoupons)){
                log.info("该活动用户已经领过券，不能再领取了：accountId is {} and activityId is {}" , accountId, activityId);
                return Result.fail("activity_coupon_receive", "您已经领过券了，不能重复领取！");
            }
            sendCoupon(activity.getCouponTemplateId(), accountId, activityId);
            String numberKey = "activity" + activityId;
            if(redisTools.exists(numberKey)){
                redisTools.set(numberKey, (Integer)redisTools.get(numberKey) + 1);
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
        if(!redisTools.setIfAbsent(activityId + "<->" + accountId, 5)){
            log.info("5秒之内禁止重复抢购！");
            return Result.fail("click_fast", "请勿频繁点击！");
        }
        CommunalActivity activity = communalActivityRepository.findOne(activityId);
        if(ObjectUtils.isEmpty(activity)){
            log.info("活动不存在！");
            return Result.fail("Not_found_activity", "活动不存在！");
        }
        long nowTime = System.currentTimeMillis();
        if(nowTime < activity.getStartDate().getTime()){
            log.info("活动尚未开始！");
            return Result.fail("activity_not_begin", "活动尚未开始！");
        }
        if(activity.getStatus().equals("no")){
            log.info("活动未上架！");
            return Result.fail("activity_is_down", "活动未上架！");
        }
        if(!activity.getType().equals("1")){
            log.info("此活动非长期领券活动！");
            return Result.fail("activity_not_normal", "此活动非长期领券活动！");
        }
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndActivityId(accountId, activityId);
        if(!ObjectUtils.isEmpty(accountCoupons)){
            log.info("该活动用户已经领过券，不能再领取了：accountId is {} and activityId is {}" , accountId, activityId);
            return Result.fail("activity_coupon_receive", "您已经领过券了，不能重复领取！");
        }
        log.info("该活动用户未领过券，可以领取：accountId is {} and activityId is {}" , accountId, activityId);
        sendCoupon(activity.getCouponTemplateId(), accountId, activityId);
        return Result.success(new HashMap<>());
    }

    /**
     * 通用发放优惠券
     */
    private void sendCoupon(String templateId, String accountId, String activityId){
        log.info("给抢券成功的用户发放优惠券：accountId is {} and templateId is {}" , accountId, templateId);
        ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, "1");
        Map map = responseEntity.getBody();
        if(MapUtils.getString(map, "code").equals("SUCCESS")){
            log.info("获取优惠券成功！accountId is {} and templateId is {}" , accountId, templateId);
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "yjx", accountId, activityId);
            accountCouponRepository.save(accountCoupons);
            log.info("发放优惠券成功！accountId is {} and templateId is {}" , accountId, templateId);
        }
    }

}
