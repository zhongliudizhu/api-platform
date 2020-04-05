package com.winstar.communalCoupon.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.winstar.cashier.wx.entity.card.request.ConsumeCardRequest;
import com.winstar.cashier.wx.service.WxMartetTemplate;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.CouponSendRecord;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.CouponSendRecordRepository;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.costexchange.entity.FailSendRecord;
import com.winstar.costexchange.repository.FailSendRecordRepository;
import com.winstar.costexchange.utils.RequestUtil;
import com.winstar.redis.CouponRedisTools;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author UU
 */
@Service
@Slf4j
@SuppressWarnings("unchecked")
public class AccountCouponService {

    private AccountCouponRepository accountCouponRepository;

    private final CouponSendRecordRepository couponSendRecordRepository;

    private final CouponRedisTools couponRedisTools;

    private final FailSendRecordRepository failSendRecordRepository;

    @Autowired
    public AccountCouponService(AccountCouponRepository accountCouponRepository, CouponSendRecordRepository couponSendRecordRepository, CouponRedisTools couponRedisTools, FailSendRecordRepository failSendRecordRepository) {
        this.accountCouponRepository = accountCouponRepository;
        this.couponSendRecordRepository = couponSendRecordRepository;
        this.couponRedisTools = couponRedisTools;
        this.failSendRecordRepository = failSendRecordRepository;
    }

    private static final String T_KEYS = "template_set";
    private static final String PREFIX = "coupon_account_";
    public static final String LOCKED = "locked";
    public static final String NORMAL = "normal";
    public static final String USED = "used";
    public static final String EXPIRED = "expired";
    public static final String SENDING = "sending";

    public static final String COUPON_LIST_PREFIX = "coupon_list_";

    public static String merchant = "61045834521";
    public static String secret = "6abab93252ea1a93c6c36a4585075da1b7116c6cdc0dccc8c8fe14e633289305";

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
        reqMap.put("merchant", merchant);
        ResponseEntity<Map> stringResponseEntity = new RestTemplate().getForEntity(takeCouponUrl + SignUtil.getParameters(reqMap, secret), Map.class);
        log.info("请求获取优惠券接口结果：" + stringResponseEntity.getBody());
        return stringResponseEntity;
    }


    /**
     * 获取可用优惠券
     *
     * @return List<AccountCoupon>
     */
    public List<AccountCoupon> getAvailableCoupons(List<AccountCoupon> accountCoupons, Double amount, String tags) {
        String couponIds = accountCoupons.stream().map(AccountCoupon::getCouponId).collect(Collectors.joining(","));
        log.info("couponIds:" + couponIds);
        ResponseEntity<Map> resp = checkCoupon(couponIds, amount.toString(), tags);
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
        reqMap.put("merchant", merchant);
        ResponseEntity<Map> mapResponseEntity = new RestTemplate().getForEntity(verifyCouponUrl + SignUtil.getParameters(reqMap, secret), Map.class);
        log.info("请求校验优惠券接口结果：" + mapResponseEntity.getBody().toString());
        return mapResponseEntity;
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
        reqMap.put("merchant", merchant);
        reqMap.put("tags", tags);
        reqMap.put("sign", SignUtil.sign(reqMap, secret));
        ResponseEntity<Map> mapResponseEntity = new RestTemplate().postForEntity(writeOffCouponUrl, reqMap, Map.class);
        log.info("请求核销优惠券接口结果：" + mapResponseEntity.getBody().toString());
        return mapResponseEntity;
    }

    @Async
    public void modifyCouponState(String accountId, String couponIds, String state, String serialNumber) {
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndCouponIdIn(accountId, couponIds.split(","));
        for (AccountCoupon accountCoupon : accountCoupons) {
            if (accountCoupon.getState().equals(AccountCouponService.USED)) {
                continue;
            }
            if (state.equals(AccountCouponService.NORMAL) && !accountCoupon.getState().equals(AccountCouponService.LOCKED) && !accountCoupon.getState().equals(AccountCouponService.SENDING)) {
                continue;
            }
            accountCoupon.setState(state);
            accountCoupon.setOrderId(serialNumber);
            if (state.equals(AccountCouponService.USED)) {
                accountCoupon.setUseDate(new Date());
            }
            accountCouponRepository.save(accountCoupon);
        }
        couponRedisTools.hmPutAll(AccountCouponService.COUPON_LIST_PREFIX + accountId, accountCoupons.stream().collect(Collectors.toMap(AccountCoupon::getCouponId, Function.identity())));
    }

    @Async
    public void consumeWxCard(String accountId, String couponIds, WxMartetTemplate wxMartetTemplate, AccountService accountService) {
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndCouponIdIn(accountId, couponIds.split(","));
        Account account = accountService.findOne(accountId);
        for (AccountCoupon accountCoupon : accountCoupons) {
            log.info("调用微信卡包核销优惠券，cardPackageId is " + accountCoupon.getCardPackageId());
            if (StringUtils.isEmpty(accountCoupon.getCardPackageId())) {
                continue;
            }
            try {
                ConsumeCardRequest consumeCardRequest = new ConsumeCardRequest();
                consumeCardRequest.setOpenid(account.getOpenid());
                consumeCardRequest.setCardId(accountCoupon.getCardPackageId());
                log.info("请求参数：" + JSON.toJSONString(consumeCardRequest));
                wxMartetTemplate.consumeCard(consumeCardRequest);
            } catch (Exception e) {
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
        couponRedisTools.hmRemove(COUPON_LIST_PREFIX + couponSendRecord.getSendAccountId(), accountCoupon.getCouponId());
        couponRedisTools.hmPut(COUPON_LIST_PREFIX + accountCoupon.getAccountId(), accountCoupon.getCouponId(), accountCoupon, null);
        return record;
    }

    /**
     * 检测优惠券是否有赠送超时未领取的券，有则回库
     */
    public void backSendingTimeOutCoupon(List<AccountCoupon> accountCoupons) {
        accountCoupons.stream().filter(accountCoupon -> accountCoupon.getState().equals(AccountCouponService.SENDING) && (new Date().getTime() - accountCoupon.getSendTime().getTime()) >= 24 * 60 * 60 * 1000).forEach(accountCoupon -> {
            accountCoupon.setState(AccountCouponService.NORMAL);
            accountCouponRepository.save(accountCoupon);
        });
    }

    /**
     * 发券
     */
    public List<AccountCoupon> sendCoupon(SendCouponDomain domain, RedisTools redisTools) {
        log.info("给用户发放优惠券：accountId is {} and templateId is {}", domain.getAccountId(), domain.getTemplateId());
        ResponseEntity<Map> responseEntity = getCoupon(domain.getTemplateId(), domain.getNum());
        Map map = responseEntity.getBody();
        if (MapUtils.getString(map, "code").equals("SUCCESS")) {
            log.info("获取优惠券成功！accountId is {} and templateId is {}", domain.getAccountId(), domain.getTemplateId());
            List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), domain, redisTools);
            accountCouponRepository.save(accountCoupons);
            String key = COUPON_LIST_PREFIX + domain.getAccountId();
            log.info("发放优惠券成功！accountId is {} and templateId is {}", domain.getAccountId(), domain.getTemplateId());
            couponRedisTools.hmPutAll(key, accountCoupons.stream().collect(Collectors.toMap(AccountCoupon::getCouponId, Function.identity())));
            accountCoupons.forEach(e -> {
                if (ObjectUtils.isEmpty(couponRedisTools.hmGet(key, e.getCouponId()))) {
                    log.info("redis写入失败 --" + e.getCouponId());
                    FailSendRecord failSendRecord = new FailSendRecord();
                    failSendRecord.setAccountId(domain.getAccountId());
                    failSendRecord.setTemplateId(domain.getTemplateId());
                    failSendRecord.setFailMsg("redis写入失败 --" + e.getCouponId());
                    failSendRecord.setCreatedAt(new Date());
                    failSendRecordRepository.save(failSendRecord);
                }
            });
            return accountCoupons;
        }
        return null;
    }

    /**
     * 获取用户redis中的优惠券入库
     *
     * @param accountId 用户Id
     */
    public void getRedisCoupon(String accountId) {
        log.info("获取用户redis中的优惠券入库  {}", accountId);
        Set<Object> tIds = couponRedisTools.setMembers(T_KEYS);
        for (Object tId : tIds) {
            if (couponRedisTools.exists(tId.toString())) {
                if (couponRedisTools.hmContains(tId.toString(), accountId) && couponRedisTools.setIfAbsent("getting_coupon_" + tId + "_" + accountId, 10)) {
                    SendCouponDomain domain = new SendCouponDomain();
                    domain.setAccountId(accountId);
                    domain.setNum("1");
                    domain.setTemplateId(tId.toString());
                    domain.setType("yjx");
                    sendCoupon(domain, null);
                    couponRedisTools.hmRemove(tId.toString(), accountId);
                }
            } else {
                couponRedisTools.removeSetMembers(T_KEYS, tId.toString());
            }
        }
    }

    /**
     * 将从hash里面取出的map转化为list
     */
    public List<AccountCoupon> getAccountCouponFromRedisHash(String accountId) {
        List<AccountCoupon> accountCoupons = new ArrayList<>();
        Map<Object, Object> map = couponRedisTools.hmGetAll(COUPON_LIST_PREFIX + accountId);
        Gson gson = new Gson();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            AccountCoupon accountCoupon = gson.fromJson(gson.toJson(entry.getValue()), AccountCoupon.class);
            accountCoupons.add(accountCoupon);
        }
        return accountCoupons;
    }

}



