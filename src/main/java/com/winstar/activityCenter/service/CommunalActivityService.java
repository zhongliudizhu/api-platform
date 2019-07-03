package com.winstar.activityCenter.service;

import com.alibaba.fastjson.JSON;
import com.winstar.activityCenter.entity.CommunalActivity;
import com.winstar.activityCenter.repository.CommunalActivityRepository;
import com.winstar.activityCenter.vo.ActivityVo;
import com.winstar.activityCenter.vo.CouponTemplateVo;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.redis.RedisTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author UU
 * @Classname CommunalActivityService
 * @Description TODO
 * @Date 2019/7/2 11:35
 */
@Service
@Slf4j
public class CommunalActivityService {
    private final CommunalActivityRepository communalActivityRepository;
    private final RedisTools redisTools;
    private final AccountCouponRepository accountCouponRepository;

    @Autowired
    public CommunalActivityService(CommunalActivityRepository communalActivityRepository, RedisTools redisTools, AccountCouponRepository accountCouponRepository) {
        this.communalActivityRepository = communalActivityRepository;
        this.redisTools = redisTools;
        this.accountCouponRepository = accountCouponRepository;
    }

    @Value("${info.getTemplateInfoUrl}")
    private String getTemplateInfoUrl;


    /**
     * @param accountId 用户ID
     * @return list
     */
    public List<ActivityVo> findAvailableActivities(String accountId) {
        List<ActivityVo> activityVos = new ArrayList<>();
        Date now = new Date();
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountId(accountId);
        Map<String, List<AccountCoupon>> groupAccountCoupons = accountCoupons.stream().collect(Collectors.groupingBy(AccountCoupon::getActivityId));
        //已上架未删除已到展示时间
        List<CommunalActivity> list = communalActivityRepository.findAllByStatusAndDelAndShowDateBefore("yes", "no", now);
        StringBuilder sb = new StringBuilder();
        for (CommunalActivity communalActivity : list) {
            ActivityVo activityVo = new ActivityVo();
            BeanUtils.copyProperties(communalActivity, activityVo);
            activityVo.setTemplateId(communalActivity.getCouponTemplateId());
            sb.append(communalActivity.getCouponTemplateId()).append(",");
            activityVo.setStatus("领取");
            boolean available = true;
            //未开始领取直接返回
            if (communalActivity.getStartDate().getTime() > now.getTime()) {
                activityVo.setStatus("即将开抢");
                activityVos.add(activityVo);
                continue;
            }
            //限时限量优惠券判断
            if ("2".equals(communalActivity.getType())) {
                Integer activityReceivedNum = getActivityReceivedNum("activity" + communalActivity.getId());
                activityVo.setReceivedNum(activityReceivedNum);
                if (activityReceivedNum >= communalActivity.getTotalNum()) {
                    activityVo.setStatus("已结束");
                }
            }
            List<AccountCoupon> activityCoupons = groupAccountCoupons.get(communalActivity.getId());
            //正常活动已领取当天显示
            if (!ObjectUtils.isEmpty(activityCoupons)) {
                if (getDayEnd(activityCoupons.get(0).getCreatedAt()).getTime() <= now.getTime()) {
                    available = false;
                } else {
                    activityVo.setStatus("已领取");
                }
            }
            if (available) {
                activityVos.add(activityVo);
            }
        }
        String templateIds = "";
        if (sb.toString().length() != 0) {
            templateIds = sb.toString().substring(0, sb.toString().length() - 1);
        }
        setTemplateInfo(activityVos, templateIds);
        return activityVos;
    }

    /**
     * 模板信息设置
     *
     * @param activityVos 活动VOList
     * @param templateIds 模板ID
     */
    private void setTemplateInfo(List<ActivityVo> activityVos, String templateIds) {
        if (!ObjectUtils.isEmpty(activityVos)) {
            List<CouponTemplateVo> list = getTemplateInfo(templateIds);
            Map<String, CouponTemplateVo> map = list.stream().collect(Collectors.toMap(CouponTemplateVo::getId, CouponTemplateVo::getThis));
            for (ActivityVo activityVo : activityVos) {
                BeanUtils.copyProperties(map.get(activityVo.getTemplateId()), activityVo);
            }
        }
    }

    /**
     * 获取该日结束时间
     *
     * @param date 时间
     * @return date
     */
    private static Date getDayEnd(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.MINUTE, 59);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * 获取已领取数量
     *
     * @param key key
     * @return int
     */
    private Integer getActivityReceivedNum(String key) {
        Integer activityReceivedNum = (Integer) redisTools.get(key);
        return ObjectUtils.isEmpty(activityReceivedNum) ? 0 : activityReceivedNum;
    }


    /**
     * 获取优惠券模板信息
     *
     * @param templateIds 优惠券模板id
     * @return ResponseEntity
     */
    private List<CouponTemplateVo> getTemplateInfo(String templateIds) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("templateIds", templateIds);
        reqMap.put("merchant", SignUtil.merchant);
//        reqMap.put("sign", SignUtil.sign(reqMap));
        ResponseEntity<Map> mapResponseEntity = new RestTemplate().getForEntity(getTemplateInfoUrl + SignUtil.getParameters(reqMap), Map.class);
        log.info("请求优惠券模板信息接口结果：{}", mapResponseEntity.getBody().get("data").toString());
        List<CouponTemplateVo> list =
//                (List<CouponTemplateVo>) mapResponseEntity.getBody().get("data");
                JSON.parseArray(mapResponseEntity.getBody().get("data").toString(), CouponTemplateVo.class);
        log.info("请求优惠券模板信息接口结果：{}", list);
        return list;
    }

}
