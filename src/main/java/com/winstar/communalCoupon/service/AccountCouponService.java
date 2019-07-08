package com.winstar.communalCoupon.service;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
@Slf4j
public class AccountCouponService {

    private AccountCouponRepository accountCouponRepository;

    @Autowired
    public AccountCouponService(AccountCouponRepository accountCouponRepository) {
        this.accountCouponRepository = accountCouponRepository;
    }

    public static final String LOCKED = "locked";
    public static final String NORMAL = "normal";
    public static final String USED = "used";
    public static final String EXPIRED = "expired";
    public static final String SENDING = "sending";


    @Value("${info.takeCouponUrl}")
    private String takeCouponUrl;

    private static String verifyCouponUrl;

    @Value("${info.verifyCouponUrl}")
    public void setVerifyCouponUrl(String verifyCouponUrl){
        AccountCouponService.verifyCouponUrl = verifyCouponUrl;
    }

    @Value("${info.writeOffCouponUrl}")
    private String writeOffCouponUrl;

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
        ResponseEntity<Map> stringResponseEntity = new RestTemplate().getForEntity(takeCouponUrl + SignUtil.getParameters(reqMap), Map.class);
        log.info("请求获取优惠券接口结果：" + stringResponseEntity.getBody());
        return stringResponseEntity;
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
        log.info("verifyCouponUrl=" + verifyCouponUrl);
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ids", couponIds);
        reqMap.put("itemAmount", itemAmount);
        reqMap.put("tags", tags);
        reqMap.put("merchant", SignUtil.merchant);
        ResponseEntity<Map> mapResponseEntity = new RestTemplate().getForEntity(verifyCouponUrl + SignUtil.getParameters(reqMap), Map.class);
        log.info("请求校验优惠券接口结果：" + mapResponseEntity.getBody().toString());
        return mapResponseEntity;
    }

    /**
     * 检验优惠券
     *
     * @param couponIds  优惠券id
     * @param itemAmount 商品金额
     * @return ResponseEntity
     */
    public static ResponseEntity<Map> checkCoupon(String couponIds, String itemAmount) {
        return checkCoupon(couponIds, itemAmount, null);
    }

    /**
     * 核销优惠券
     *
     * @param couponIds  优惠券id
     * @param itemAmount 商品金额
     * @return ResponseEntity
     */
    @Async
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
    @Async
    public ResponseEntity<Map> writeOffCoupon(String couponIds, String itemAmount, String tags) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ids", couponIds);
        reqMap.put("itemAmount", itemAmount);
        reqMap.put("merchant", SignUtil.merchant);
        reqMap.put("tags", tags);
        reqMap.put("sign", SignUtil.sign(reqMap));
        ResponseEntity<Map> mapResponseEntity = new RestTemplate().postForEntity(writeOffCouponUrl, reqMap, Map.class);
        log.info("请求核销优惠券接口结果：" + mapResponseEntity.getBody().toString());
        return mapResponseEntity;
    }

    @Async
    public void modifyCouponState(String accountId, String couponIds, String state, String serialNumber) {
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndCouponIdIn(accountId, couponIds.split(","));
        for (AccountCoupon accountCoupon : accountCoupons) {
            accountCoupon.setState(state);
            accountCoupon.setOrderId(serialNumber);
            accountCoupon.setUseDate(new Date());
        }
        accountCouponRepository.save(accountCoupons);
    }

}



