package com.winstar.couponActivity.utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by zl on 2017/9/12
 */
@Component
public class RequestServerUtil {


    public static ResponseEntity<String> getRequest(RestTemplate restTemplate, String url,Map uriVariables){
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(null,RequestServerUtil.getJsonHeaders());
        ResponseEntity<String> responseEntity =null;
        try {
          responseEntity = restTemplate.exchange(url, HttpMethod.GET,entity, String.class,uriVariables);
        } catch (Exception e){
           e.printStackTrace();
        }
        return responseEntity;
    }
    //
    public static ResponseEntity<String> getRequestToken(RestTemplate restTemplate, String orderUrl, Map<String, Object> reqOrderMap, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("token_id",token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = null;
        try {
         responseEntity = restTemplate.exchange(orderUrl, HttpMethod.GET,entity, String.class,reqOrderMap);
        } catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    public static ResponseEntity<String> getRequestFromToken(RestTemplate restTemplate, String orderUrl, Map<String, String> reqOrderMap, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("token_id",token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(orderUrl, HttpMethod.GET,entity,String.class,reqOrderMap);
        } catch (Exception e){
            e.printStackTrace();
        }
        return responseEntity;
    }

    /**
     * 设置json请求头
     * 普通
     * @return http header请求头
     */
    public static HttpHeaders getJsonHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
        headers.add("Connection", "close");
        return headers;
    }

}
