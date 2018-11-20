package com.winstar.weekendBrand.service;

import com.alibaba.fastjson.JSON;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.ServiceManager;
import com.winstar.weekendBrand.entity.OrdersRedPackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * 支付成功生成红包领券资格
     *
     * @param orderId     订单ID
     * @param couponPrice 优惠券价钱
     */
    public void generateOrderRedPackageInfoByOrderId(String orderId, BigDecimal couponPrice) {
        log.info("进入生成红包->" + orderId);
        long count = ServiceManager.oilOrderRepository.countByIdAndActivityIdAndIsAvailableAndStatus(orderId, OrdersRedPackageInfo.ACTIVITY_ID_WEEKEND_BRAND, "0", 2);
        if (count == 0) return;
        log.info("开始生成红包开始" + orderId);
        List<OrdersRedPackageInfo> ordersRedPackageInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ordersRedPackageInfoList.add(new OrdersRedPackageInfo().init(orderId, couponPrice));
        }
        ServiceManager.ordersRedPackageInfoRepository.save(ordersRedPackageInfoList);

        log.info("生成红包结束" + orderId);
    }

    /**
     * 领取红包，成功发券，失败返回错误信息
     *
     * @param accountId 账号ID
     * @param orderId   订单ID
     * @return 优惠券信息
     * @throws NotRuleException 异常信息
     */
    public MyCoupon receiveOrderRedPackage(String accountId, String orderId) throws NotRuleException {
        checkRepeatReceive(accountId, orderId);
        List<OrdersRedPackageInfo> validOrderRedPackageList = checkIsValid();
        MyCoupon myCoupon = null;
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
                setOrderRedPackageInfoReceived(accountId, ordersRedPackageInfo);
                break;
            }
        }
        log.info("领取成功" + JSON.toJSONString(myCoupon));
        return myCoupon;
    }

    /**
     * 更新领券资格数据的状态
     *
     * @param accountId            账号ID
     * @param ordersRedPackageInfo 领券资格
     */
    private void setOrderRedPackageInfoReceived(String accountId, OrdersRedPackageInfo ordersRedPackageInfo) {
        ordersRedPackageInfo.setReceiveStatus(OrdersRedPackageInfo.RECEIVE_STATUS_YES);
        ordersRedPackageInfo.setReceiveTime(new Date());
        ordersRedPackageInfo.setReceiveAccountId(accountId);
        ServiceManager.ordersRedPackageInfoRepository.save(ordersRedPackageInfo);
    }

    /**
     * 检验红包是否可用，可用返回红包机会列表
     *
     * @return List<OrdersRedPackageInfo>
     * @throws NotRuleException 红包已被领完
     */
    private List<OrdersRedPackageInfo> checkIsValid() throws NotRuleException {
        List<OrdersRedPackageInfo> validOrderRedPackageList = ServiceManager.ordersRedPackageInfoRepository.findByReceiveStatusAndExpiredTimeAfter(OrdersRedPackageInfo.RECEIVE_STATUS_NO, new Date());
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
    private void checkRepeatReceive(String accountId, String orderId) throws NotRuleException {
        long receiveCount = ServiceManager.ordersRedPackageInfoRepository.countByReceiveAccountIdAndOrderId(accountId, orderId);
        if (receiveCount > 0) {
            throw new NotRuleException("repeatReceived.ordersRedPackageInfo");
        }
    }

}
