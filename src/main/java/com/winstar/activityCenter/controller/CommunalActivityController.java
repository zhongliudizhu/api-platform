package com.winstar.activityCenter.controller;

import com.winstar.activityCenter.entity.CommunalActivity;
import com.winstar.activityCenter.repository.CommunalActivityRepository;
import com.winstar.activityCenter.service.CommunalActivityService;
import com.winstar.activityCenter.vo.ActivityVo;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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
    AccountService accountService;

    @GetMapping(value = "/availableActivities")
    public Result getActivities(@RequestParam(required = false, defaultValue = "center") String target) throws NotRuleException {
        String accountId = (String) request.getAttribute("accountId");
        List<ActivityVo> activityVos = communalActivityService.findAvailableActivities(accountId, target);
        return Result.success(activityVos);
    }

    @GetMapping(value = "/newUser")
    public Result newUserActivity(@RequestParam String openId, String nickName, String headImgUrl) {
        log.info("openId is {},nickName is {}, headImgUrl is {}", openId, nickName, headImgUrl);
        String accountId = accountService.getAccountOrCreateByOpenId(openId, nickName, headImgUrl).getId();
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
        SendCouponDomain domain = new SendCouponDomain(activity.getCouponTemplateId(), accountId, AccountCoupon.TYPE_YJX, "1", activity.getId(), null);
        List<AccountCoupon> sendCouponResult = accountCouponService.sendCoupon(domain, null);
        if (!ObjectUtils.isEmpty(sendCouponResult)) {
            return Result.success(null);
        } else {
            return Result.fail("send_coupon_fail", "发券失败");
        }
    }

}
