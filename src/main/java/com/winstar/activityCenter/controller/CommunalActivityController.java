package com.winstar.activityCenter.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.CommunalActivity;
import com.winstar.activityCenter.repository.CommunalActivityRepository;
import com.winstar.activityCenter.service.CommunalActivityService;
import com.winstar.activityCenter.vo.ActivityVo;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author UU
 * Classname CommunalActivityController
 * Description TODO
 * Date 2019/7/2 11:31
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cbc/communalActivity")
public class CommunalActivityController {

    CommunalActivityRepository communalActivityRepository;
    CommunalActivityService communalActivityService;
    HttpServletRequest request;
    AccountCouponRepository accountCouponRepository;
    AccountCouponService accountCouponService;

    @GetMapping(value = "/availableActivities")
    public Result getActivities(@RequestParam(required = false, defaultValue = "center") String target) throws NotRuleException {
        String accountId = (String) request.getAttribute("accountId");
        List<ActivityVo> activityVos = communalActivityService.findAvailableActivities(accountId, target);
        return Result.success(activityVos);
    }

    @GetMapping(value = "/newUser")
    public Result newUserActivity(@RequestParam String openId, String nickName, String headImgUrl) {
        log.info("openId is {},nickName is {}, headImgUrl is {}", openId, nickName, headImgUrl);
        String accountId;
        Account account = ServiceManager.accountRepository.findByOpenid(openId);
        if (ObjectUtils.isEmpty(account)) {
            log.info("用户{}不存在，正在创建...", openId);
            Account accountSaved = ServiceManager.accountService.createAccount(new AccountParam(openId, nickName, headImgUrl));
            ServiceManager.accountService.createAccessToken(accountSaved);
            accountId = accountSaved.getId();
        } else {
            accountId = account.getId();
        }
        AccessToken accessToken = ServiceManager.accessTokenService.findByAccountId(accountId);
        if (null == accessToken) {
            ServiceManager.accountService.createAccessToken(account);
        }
        CommunalActivity activity = communalActivityRepository.findByStatusAndDelAndShowDateBeforeAndOnlyNew("yes", "no", new Date(), "yes");
        if (ObjectUtils.isEmpty(activity)) {
            log.error("新用户活动不存在");
            return Result.fail("activity_not_here", "活动不存在");
        }
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndStatus(accountId, 3);
        if (!ObjectUtils.isEmpty(oilOrders)) {
            return Result.fail("user_not_new", "仅限新用户领取");
        } else {
            List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndActivityId(accountId, activity.getId());
            if (!ObjectUtils.isEmpty(accountCoupons)) {
                return Result.fail("user_has_received", "用户已领取");
            }
        }
        boolean success = sendCoupon(activity.getCouponTemplateId(), accountId, activity.getId());
        if (success) {
            return Result.success(null);
        } else {
            return Result.fail("send_coupon_fail", "发券失败");
        }
    }

    private boolean sendCoupon(String templateId, String accountId, String activityId) {
        log.info("给抢券成功的用户发放优惠券：accountId is {} and templateId is {}", accountId, templateId);
        ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, "1");
        Map map = responseEntity.getBody();
        if (MapUtils.getString(map, "code").equals("SUCCESS")) {
            log.info("获取优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "yjx", accountId, activityId, null);
            accountCouponRepository.save(accountCoupons);
            log.info("发放优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            return true;
        } else {
            return false;
        }
    }
}
