package com.winstar.weekendBrand.service;

import com.alibaba.fastjson.JSON;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.ServiceManager;
import com.winstar.vo.Result;
import com.winstar.weekendBrand.entity.OrdersRedPackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 名称 OrderRedPackageInfoService
 * 作者 dpw
 * 日期 2018/11/20 16:38
 * 项目 winstar-cbc-platform-api
 * 描述
 */
@Service
@Slf4j
public class OrderRedPackageInfoService {


    /**
     * 检验重复购买
     *
     * @param accountId  accountId
     * @param activityId activityId
     * @return true
     */
    public void canBuy(String accountId, String activityId) throws NotRuleException {
        if (OrdersRedPackageInfo.ACTIVITY_ID_WEEKEND_BRAND.equals(activityId) && ServiceManager.oilOrderRepository.countByAccountIdAndActivityId(accountId, activityId, DateUtil.getWeekBegin(), DateUtil.getWeekEnd()) > 0) {
            throw new NotRuleException("justOnce.weekendBrand");
        }
    }

    /**
     * 校验活动状态
     *
     * @throws NotRuleException
     */
    public void checkActivityStatus(String activityId) throws NotRuleException {
        //todox
        int leftDays = ServiceManager.weekEndBrandService.calculateWeek(6, Integer.valueOf(DateUtil.getWeekOfDate(new Date())));
        if (activityId.equals(OrdersRedPackageInfo.ACTIVITY_ID_WEEKEND_BRAND)
                && 0 != leftDays
                && !StringUtils.isEmpty(activityId))
            throw new NotRuleException("weekendBrandActivityIsOver");
    }


    /**
     * 支付成功生成红包领券资格
     *
     * @param orderId     订单ID
     * @param couponPrice 优惠券价钱
     */
    public void generateOrderRedPackageInfoByOrderId(String orderId, BigDecimal couponPrice) {
        log.info("进入生成红包->" + orderId);
        //
       /* long count = ServiceManager.oilOrderRepository.countByIdAndActivityIdAndIsAvailableAndStatus(orderId, OrdersRedPackageInfo.ACTIVITY_ID_WEEKEND_BRAND, "0", 2);
        if (count == 0) return;*/
        log.info("开始生成红包开始" + orderId);
        List<OrdersRedPackageInfo> ordersRedPackageInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ordersRedPackageInfoList.add(new OrdersRedPackageInfo().init(orderId, couponPrice));
        }
        ServiceManager.ordersRedPackageInfoRepository.save(ordersRedPackageInfoList);
        log.info("生成红包结束" + orderId);
    }

    @Async
    public void generateOrdersRedPackageInfoByOrder(OilOrder oilOrder) {
        if (oilOrder.getActivityId().equals(OrdersRedPackageInfo.ACTIVITY_ID_WEEKEND_BRAND)) {
            ServiceManager.orderRedPackageInfoService.generateOrderRedPackageInfoByOrderId(oilOrder.getId(), new BigDecimal(5));
        }
    }

    /**
     * 领取红包，成功发券，失败返回错误信息
     *
     * @param accountId 账号ID
     * @param orderId   订单ID
     * @return 优惠券信息
     * @throws NotRuleException 异常信息
     */
    public Result receiveOrderRedPackage(String accountId, String orderId) throws NotRuleException {
        MyCoupon myCoupon = checkRepeatReceive(accountId, orderId);
        Map map = new HashMap<>();
        if (null != myCoupon) {
            map.put("receiveStatus", "alreadyReceived");
            map.put("myCoupon", myCoupon);
            return Result.success(map);
        }

        List<OrdersRedPackageInfo> validOrderRedPackageList = checkIsValid(orderId);

        for (OrdersRedPackageInfo ordersRedPackageInfo : validOrderRedPackageList) {
            if (ServiceManager.redisTools.setIfAbsent(ordersRedPackageInfo.getId(), 120)) {
                //发送油券
                myCoupon = ServiceManager.couponService.sendCoupon_freedom(accountId,
                        OrdersRedPackageInfo.ACTIVITY_ID_WEEKEND_BRAND,
                        ordersRedPackageInfo.getCouponPrice().doubleValue(),
                        DateUtil.addMonth(new Date(), 3),
                        300.0,
                        "延长壳牌加油优惠券",
                        "延长壳牌加油优惠券,周末分享");
                //更新机会状态
                setOrderRedPackageInfoReceived(accountId, ordersRedPackageInfo, myCoupon);
                break;
            }
        }
        log.info("领取成功" + JSON.toJSONString(myCoupon));

        map.put("receiveStatus", "newReceived");
        map.put("myCoupon", myCoupon);
        return Result.success(map);
    }

    /**
     * 更新领券资格数据的状态
     *
     * @param accountId            账号ID
     * @param ordersRedPackageInfo 领券资格
     */
    private void setOrderRedPackageInfoReceived(String accountId, OrdersRedPackageInfo ordersRedPackageInfo, MyCoupon myCoupon) {
        ordersRedPackageInfo.setReceiveStatus(OrdersRedPackageInfo.RECEIVE_STATUS_YES);
        ordersRedPackageInfo.setReceiveTime(new Date());
        ordersRedPackageInfo.setReceiveAccountId(accountId);
        ordersRedPackageInfo.setCouponDetail(JSON.toJSONString(myCoupon));
        ServiceManager.ordersRedPackageInfoRepository.save(ordersRedPackageInfo);
    }

    /**
     * 检验红包是否可用，可用返回红包机会列表
     *
     * @return List<OrdersRedPackageInfo>
     * @throws NotRuleException 红包已被领完
     */
    private List<OrdersRedPackageInfo> checkIsValid(String orderId) throws NotRuleException {
        List<OrdersRedPackageInfo> validOrderRedPackageList = ServiceManager.ordersRedPackageInfoRepository.findByOrderIdAndReceiveStatusAndExpiredTimeAfter(orderId, OrdersRedPackageInfo.RECEIVE_STATUS_NO, new Date());
        if (validOrderRedPackageList.size() == 0) {
            throw new NotRuleException("noRedPackageLeft.ordersRedPackageInfo");
        }
        return validOrderRedPackageList;
    }

    /**
     * 检测重复领取
     *
     * @param accountId accountId
     * @param orderId   orderId
     * @throws NotRuleException 重复领取
     */
    private MyCoupon checkRepeatReceive(String accountId, String orderId) {
        List<OrdersRedPackageInfo> ordersRedPackageInfoList = ServiceManager.ordersRedPackageInfoRepository.findByReceiveAccountIdAndOrderId(accountId, orderId);
        if (ordersRedPackageInfoList.size() > 0) {
            try {
                return JSON.parseObject(ordersRedPackageInfoList.get(0).getCouponDetail(), MyCoupon.class);
            } catch (Exception e) {
                log.error("领取优惠券成功后，再次领取时出错", e);
            }
        }
        return null;
    }

}
