package com.winstar.order.service;

import com.winstar.coupon.service.CouponService;
import com.winstar.order.entity.CouponLog;
import com.winstar.order.repository.CouponLogRepository;
import com.winstar.utils.WsdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author shoo on 2018/2/2 17:48.
 *         --
 */
@Service
public class LoveActivityService {
    private static Logger logger = LoggerFactory.getLogger(CouponService.class);
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponLogRepository couponLogRepository;

    @Transactional
    public String giveCoupon(String accountId, String openId){
        try {
            String couponName1 = "S1-" + WsdUtils.getRandomNumber(8);
            String couponName2 = "S2-" + WsdUtils.getRandomNumber(8);
            couponService.sendCoupon_freedom(accountId,"2",5.0,new Date(1522425599000L),100.0, couponName1, "手心活动发5元优惠券");
            couponService.sendCoupon_freedom(accountId,"2",10.0,new Date(1522425599000L),200.0, couponName2, "手心活动发10元优惠券");
            CouponLog couponLog = new CouponLog();
            couponLog.setAccountId(accountId);
            couponLog.setOpenId(openId);
            couponLog.setCreateTime(new Date());
            couponLogRepository.save(couponLog);

        }catch (Exception ex){
            logger.error("爱心活动发券失败");
            return "1";
        }
        return "0";
    }

}
