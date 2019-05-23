package com.winstar.costexchange.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by zl on 2019/5/23
 */
public class RequestUtil {

    public static Map post(String url, Map<String, String> reqMap){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(reqMap),headers);
        ResponseEntity<Map> resp = new RestTemplate().exchange(url, HttpMethod.POST, entity, Map.class);
        return resp.getBody();
    }

}
