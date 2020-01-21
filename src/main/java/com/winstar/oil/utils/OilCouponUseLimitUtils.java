package com.winstar.oil.utils;

import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.order.utils.DateUtil;
import com.winstar.redis.OilRedisTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zl on 2020/1/19
 */
@Component
public class OilCouponUseLimitUtils {

    private static final Logger logger = LoggerFactory.getLogger(OilCouponUseLimitUtils.class);

    static OilRedisTools oilRedisTools;

    static MyOilCouponRepository myOilCouponRepository;

    @Autowired
    public void setOilRedisTools(OilRedisTools oilRedisTools){
        OilCouponUseLimitUtils.oilRedisTools = oilRedisTools;
    }

    @Autowired
    public void setMyOilCouponRepository(MyOilCouponRepository myOilCouponRepository){
        OilCouponUseLimitUtils.myOilCouponRepository = myOilCouponRepository;
    }

    /**
     * 是否黑名单
     */
    public static boolean isBlack(String accountId) {
        String blackList = "cbc_oil_black_list";
        if(oilRedisTools.setExists(blackList, accountId)){
            logger.info("您是黑名单用户，请联系客服！accountId：" + accountId);
            return true;
        }
        return false;
    }

    /**
     * 是否vip
     */
    public static boolean isVip(String accountId){
        String vipList = "cbc_oil_vip_list";
        if(oilRedisTools.setExists(vipList, accountId)){
            logger.info("您是VIP用户，请通行！accountId：" + accountId);
            return true;
        }
        return false;
    }

    /**
     * 油券总开关
     */
    public static boolean getCouponSwitch() {
        String coupon_switch = (String) oilRedisTools.get("cbc_coupon_switch");
        return "on".equals(coupon_switch);
    }

    /**
     * 是否受限用户
     */
    public static String isLimitUser(String id, String accountId){
        String limitKey = "limit_oil_keys";
        Set<Object> keys = oilRedisTools.setMembers(limitKey);
        String key = null;
        for (Object k : keys) {
            if(oilRedisTools.exists(k.toString()) && oilRedisTools.setExists(k.toString(), accountId)){
                key = k.toString();
                break;
            }
        }
        if(!StringUtils.isEmpty(key)){
            logger.info(accountId + "受限制code：" + key);
            List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByAccountId(accountId);
            if(!ObjectUtils.isEmpty(myOilCoupons)){
                Integer days = Integer.valueOf(key.split("_")[1].replaceAll("day-", ""));
                Integer number = Integer.valueOf(key.split("_")[2].replaceAll("number-", ""));
                logger.info("受限天数：" + days + "，受限数量：" + number);
                Date date = DateUtil.addDay(new Date(), -days);
                logger.info("比对时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                List<MyOilCoupon> notUsedList = myOilCoupons.stream().filter(e -> (e.getUseState().equals("0") && !StringUtils.isEmpty(e.getPan()))).collect(Collectors.toList());
                List<MyOilCoupon> usedList = myOilCoupons.stream().filter(entity -> {
                    try {
                        return entity.getUseState().equals("1") && new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entity.getUseDate()).after(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                }).collect(Collectors.toList());
                logger.info("点开未使用张数：" + notUsedList.size() + "，" + days + "天之内使用的张数：" + usedList.size());
                if(usedList.size() >= number){
                    logger.info("使用超过限制张数，返回限制code");
                    return key;
                }
                if((notUsedList.size() + usedList.size()) >= number && !notUsedList.stream().map(MyOilCoupon::getId).collect(Collectors.toList()).contains(id)){
                    logger.info("使用和点开未使用之和超过限制，返回限制code");
                    return key;
                }
            }
        }
        return null;
    }

}
