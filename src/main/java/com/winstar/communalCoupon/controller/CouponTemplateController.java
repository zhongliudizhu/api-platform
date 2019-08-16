package com.winstar.communalCoupon.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winstar.communalCoupon.entity.TemplateRule;
import com.winstar.communalCoupon.repository.TemplateRuleRepository;
import com.winstar.communalCoupon.util.SignUtil;
import com.winstar.redis.RedisTools;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cbc/coupon")
@Slf4j
public class CouponTemplateController {

    private RedisTools redisTools;
    private TemplateRuleRepository templateRuleRepository;
    private RestTemplate restTemplate;
    @Value("${info.getTemplateInfoUrl}")
    private String couponTemplateUrl;

    @Autowired
    public CouponTemplateController(RedisTools redisTools, TemplateRuleRepository templateRuleRepository, RestTemplate restTemplate) {
        this.redisTools = redisTools;
        this.templateRuleRepository = templateRuleRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/showTemplateInfo")
    public Result showCouponTemplateInfo() {
        Map<String, Object> dataMap = new HashMap<>();
        double sum = 0;
        if (redisTools.exists("illegalTemplate")) {
            Set<Object> set = redisTools.setMembers("illegalTemplate");
            if (set.isEmpty()) {
                return Result.fail("illLegal_redis_not_found", "未发现违法模板信息");
            }
            Set<String> statusSet = set.stream().filter(o -> "yes".equals(redisTools.get(o + "_illegalStatus"))).map(Object::toString).collect(Collectors.toSet());
            if (statusSet.isEmpty()) {
                return Result.fail("illLegal_status_error", "违法模板状态未开启");
            }
            List<String> nonValueList = new ArrayList<>();
            for (String s : statusSet) {
                Object value = redisTools.get(s + "_amount");
                if (value == null) {
                    nonValueList.add(s);
                } else {
                    sum += Double.valueOf(value.toString());
                }
            }
            if (!CollectionUtils.isEmpty(nonValueList)) {
                List<Double> amount = getCouponAmount(nonValueList);
                if (CollectionUtils.isEmpty(amount)) {
                    return Result.fail("amount_code_error", "未找到优惠券模板的面值信息");
                }
                sum += amount.stream().mapToDouble(d -> d).sum();
            }
            dataMap.put("amount", sum);
            dataMap.put("number", statusSet.size());

        } else {
            List<TemplateRule> statusList = templateRuleRepository.findByIllegalStatus("yes");
            if (CollectionUtils.isEmpty(statusList)) {
                return Result.fail("illLegal_not_found", "未发现违法模板信息");
            }
            List<String> templateIds = getCouponTemplateIds(statusList);
            List<Double> amountList = getCouponAmount(templateIds);
            if (CollectionUtils.isEmpty(amountList)) {
                return Result.fail("code_error", "未找到优惠券面值信息");
            }
            sum = amountList.stream().mapToDouble(d -> d).sum();
            dataMap.put("amount", sum);
            dataMap.put("number", amountList.size());
        }
        dataMap.put("illegalStatus", "yes");
        return Result.success(dataMap);

    }

    private List<Double> getCouponAmount(List<String> templateIds) {
        Map<String, String> reqMap = new HashMap<>();
        StringBuilder str = new StringBuilder();
        List<Double> amountList = new ArrayList<>();
        for (String s : templateIds) {
            str.append(s);
            str.append(",");
        }
        String s = str.substring(0, str.length() - 1);
        reqMap.put("templateIds", s);
        reqMap.put("merchant", SignUtil.merchant);
        Map map = restTemplate.getForObject(couponTemplateUrl + SignUtil.getParameters(reqMap), Map.class);
        if (!"SUCCESS".equals(map.get("code"))) {
            return null;
        }
        String data = (String) map.get("data");
        JSONArray jsonArray = JSON.parseArray(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            amountList.add(Double.valueOf(jsonObject.get("amount").toString()));
        }
        return amountList;
    }

    private List<String> getCouponTemplateIds(List<TemplateRule> statusList) {
        List<String> list = new ArrayList<>();
        for (TemplateRule templateRule : statusList) {
            list.add(templateRule.getTemplateId());
        }
        return list;
    }

}
