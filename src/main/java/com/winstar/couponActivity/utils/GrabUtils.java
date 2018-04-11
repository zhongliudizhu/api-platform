package com.winstar.couponActivity.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * grab utils
 *
 * @author gradle
 */
public class GrabUtils {

    private static String token = "27e73d6efa5df6544ab4e3fe714e957a";
    private static String valuationUrl = "http://api.che300.com/service/getUsedCarPrice?token={token}&modelId={modelId}&regDate={regDate}&mile={mile}&zone={zone}";

    /**
     * 车辆估值
     */
    public static Map valuation(String modelId, String regDate, String mile, String zone) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("token", token);
        urlVariables.put("modelId", modelId);
        urlVariables.put("regDate", regDate);
        urlVariables.put("mile", mile);
        urlVariables.put("zone", zone);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(valuationUrl, Map.class, urlVariables);
        Map map = responseEntity.getBody();
        System.out.println("车300返回数据：" + JSON.toJSONString(map));
        return map;
    }
}
