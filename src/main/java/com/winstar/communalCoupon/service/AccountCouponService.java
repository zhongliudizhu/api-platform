package com.winstar.communalCoupon.service;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.util.SignUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author UU
 * @Classname AccountCouponService
 * @Description TODO
 * @Date 2019/5/28 17:57
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountCouponService {

    AccountCouponRepository accountCouponRepository;


    /**
     * 获取优惠券
     *
     * @return ResponseEntity
     */
    public ResponseEntity<Map> getCoupon(String templateId, String num) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("templateId", templateId);
        reqMap.put("num", num);
        reqMap.put("merchant", SignUtil.merchant);
        return new RestTemplate().getForEntity("http://localhost:12002/api/v1/coupon/takeCoupons?" + SignUtil.getParameters(reqMap), Map.class);
    }

    /**
     * 发放优惠券
     *
     * @param accountId  用户id
     * @param templateId 优惠券模板Id
     * @param type       优惠券种类
     * @param showStatus 是否显示
     * @return ResponseEntity
     */
    public AccountCoupon sendCoupon(String accountId, String templateId, String type, String showStatus) {
        AccountCoupon accountCoupon = new AccountCoupon();
        Map map = getCoupon(templateId, "1").getBody();
        accountCoupon.setAccountId(accountId);
        accountCoupon.setCouponId(MapUtils.getString(map, "id"));
        accountCoupon.setAmount(MapUtils.getDouble(map, "amount"));
        accountCoupon.setFullMoney(MapUtils.getDouble(map, "doorSkill"));
        accountCoupon.setTitle(MapUtils.getString(map, "name"));
        accountCoupon.setSubTitle(MapUtils.getString(map, "subTitle"));
        accountCoupon.setBeginTime((Date) map.get("startTime"));
        accountCoupon.setEndTime((Date) map.get("endTime"));
        accountCoupon.setType(type);
        accountCoupon.setTags(MapUtils.getString(map, "suitItems"));
        accountCoupon.setState("normal");
        accountCoupon.setShowStatus(showStatus);
        accountCoupon.setCreatedAt(new Date());
        AccountCoupon coupon = accountCouponRepository.save(accountCoupon);
        log.info("发券结束 优惠券信息：{}", coupon);
        return coupon;
    }


    /**
     * 获取可用优惠券
     *
     * @return List<AccountCoupon>
     */
    @SuppressWarnings("unchecked")
    public List<AccountCoupon> getAvailableCoupons(List<AccountCoupon> accountCoupons, Double amount) {
        String couponIds = accountCoupons.stream().map(AccountCoupon::getCouponId).collect(Collectors.joining(","));
        log.info("couponIds:" + couponIds);
        ResponseEntity<Map> resp = checkCoupon(couponIds, amount.toString());
        log.info("map:" + resp.getBody().toString());
        Map map = resp.getBody();
        if (!"SUCCESS".equals(map.get("code"))) {
            List<String> list = (List<String>) map.get("data");
            List<AccountCoupon> couponList = accountCoupons.stream().filter(e -> !list.contains(e.getCouponId())).collect(Collectors.toList());
            log.info("couponList: {}", couponList);
            return couponList;
        }
        return accountCoupons;
    }

    /**
     * 检验优惠券
     *
     * @param couponIds  优惠券id
     * @param itemAmount 商品金额
     * @param tags       商品标签
     * @return ResponseEntity
     */
    public static ResponseEntity<Map> checkCoupon(String couponIds, String itemAmount, String tags) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ids", couponIds);
        reqMap.put("itemAmount", itemAmount);
        reqMap.put("tags", tags);
        reqMap.put("merchant", SignUtil.merchant);
        return new RestTemplate().getForEntity("http://localhost:12002/api/v1/coupon/verify/verify?" + SignUtil.getParameters(reqMap), Map.class);
    }

    /**
     * 检验优惠券
     *
     * @param couponIds  优惠券id
     * @param itemAmount 商品金额
     * @return ResponseEntity
     */
    public static ResponseEntity<Map> checkCoupon(String couponIds, String itemAmount) {
        return checkCoupon(couponIds, itemAmount, "");
    }

    /**
     * 核销优惠券
     *
     * @param couponIds  优惠券id
     * @param itemAmount 商品金额
     * @return ResponseEntity
     */
    public ResponseEntity<Map> writeOffCoupon(String couponIds, String itemAmount) {
        return writeOffCoupon(couponIds, itemAmount, "");
    }

    /**
     * 核销优惠券
     *
     * @param couponIds  优惠券id
     * @param itemAmount 商品金额
     * @param tags       商品金额
     * @return ResponseEntity
     */
    public ResponseEntity<Map> writeOffCoupon(String couponIds, String itemAmount, String tags) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ids", couponIds);
        reqMap.put("itemAmount", itemAmount);
        reqMap.put("merchant", SignUtil.merchant);
        reqMap.put("tags", tags);
        reqMap.put("sign", SignUtil.sign(reqMap));
        return new RestTemplate().postForEntity("http://localhost:12002/api/v1/coupon/verify/cancel", reqMap, Map.class);
    }

}



