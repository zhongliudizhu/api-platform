package com.winstar.communalCoupon.service;

import com.alibaba.fastjson.JSON;
import com.winstar.cashier.wx.entity.card.request.ConsumeCardRequest;
import com.winstar.cashier.wx.service.WxMartetTemplate;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.CouponSendRecord;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.CouponSendRecordRepository;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author UU
 */
@Service
@Slf4j
public class AccountCouponService {

    private AccountCouponRepository accountCouponRepository;

    @Autowired
    private CouponSendRecordRepository couponSendRecordRepository;

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
    public void setVerifyCouponUrl(String verifyCouponUrl) {
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
    public List<AccountCoupon> getAvailableCoupons(List<AccountCoupon> accountCoupons, Double amount,String tags) {
        String couponIds = accountCoupons.stream().map(AccountCoupon::getCouponId).collect(Collectors.joining(","));
        log.info("couponIds:" + couponIds);
        ResponseEntity<Map> resp = checkCoupon(couponIds, amount.toString(),tags);
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
            if (state.equals(AccountCouponService.USED)) {
                accountCoupon.setUseDate(new Date());
            }
        }
        accountCouponRepository.save(accountCoupons);
    }

    @Async
    public void consumeWxCard(String accountId, String couponIds, WxMartetTemplate wxMartetTemplate, AccountService accountService){
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndCouponIdIn(accountId, couponIds.split(","));
        Account account = accountService.findOne(accountId);
        for (AccountCoupon accountCoupon : accountCoupons) {
            log.info("调用微信卡包核销优惠券，cardPackageId is " + accountCoupon.getCardPackageId());
            if(StringUtils.isEmpty(accountCoupon.getCardPackageId())){
                continue;
            }
            try {
                ConsumeCardRequest consumeCardRequest = new ConsumeCardRequest();
                consumeCardRequest.setOpenid(account.getOpenid());
                consumeCardRequest.setCardId(accountCoupon.getCardPackageId());
                log.info("请求参数：" + JSON.toJSONString(consumeCardRequest));
                wxMartetTemplate.consumeCard(consumeCardRequest);
            }catch (Exception e){
                log.error("调用微信卡包核销优惠券失败！cardPackageId is " + accountCoupon.getCardPackageId(), e);
            }
            log.info(accountCoupon.getCardPackageId() + "<->通知微信卡包核销完毕！");
        }
    }

    @Transactional
    public CouponSendRecord saveCouponAndRecord(AccountCoupon accountCoupon, CouponSendRecord couponSendRecord) {
        log.info("保存的用户优惠券：" + JSON.toJSONString(accountCoupon));
        accountCouponRepository.save(accountCoupon);
        log.info("保存优惠券成功！");
        log.info("保存优惠券赠送记录：" + JSON.toJSONString(couponSendRecord));
        CouponSendRecord record = couponSendRecordRepository.save(couponSendRecord);
        log.info("保存优惠券赠送记录成功！");
        return record;
    }

}



