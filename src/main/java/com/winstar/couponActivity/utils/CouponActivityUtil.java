package com.winstar.couponActivity.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winstar.couponActivity.entity.WhiteList;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * CouponActivityUtil
 *
 * @author: Big BB
 * @create 2018-04-12 13:54
 * @DESCRIPTION:
 **/
public class CouponActivityUtil {

    /**
     * 识别纯信、纯储、交叉
     * @param whiteLists101
     * @param whiteLists102
     * @return
     */
    public static Integer getSign(List<WhiteList> whiteLists101, List<WhiteList> whiteLists102){
        Integer sign = 0;

        if (!ObjectUtils.isEmpty(whiteLists101)&&ObjectUtils.isEmpty(whiteLists102)){
            sign = ActivityIdEnum.ACTIVITY_sign_1.getActivity(); //纯信
        }
        if(ObjectUtils.isEmpty(whiteLists101)&&!ObjectUtils.isEmpty(whiteLists102)){
            sign = ActivityIdEnum.ACTIVITY_sign_2.getActivity(); //纯储
        }
        if(!ObjectUtils.isEmpty(whiteLists101)&&!ObjectUtils.isEmpty(whiteLists102)){
            sign = ActivityIdEnum.ACTIVITY_sign_3.getActivity(); //交叉
        }
        return sign;
    }

    /**
     * 获取优驾行token
     */
    public static String reqAccount(String openid, String type,RestTemplate restTemplate,String getTokenInfoUrl,ObjectMapper objectMapper) {
        Map<String ,String> urlVariables = new HashMap<String ,String>();
        urlVariables.put("userId", openid);
        urlVariables.put("type", type);
        ResponseEntity<String> tokenBody = RequestServerUtil.getRequest(restTemplate,getTokenInfoUrl, urlVariables);
        if(tokenBody.getStatusCode().value()==200){
            String accountContent = tokenBody.getBody().toString();
            JsonNode tokenNodeContentNode = null;
            try {
                tokenNodeContentNode = objectMapper.readTree(accountContent);
            } catch (IOException e) {
                return null;
            }
            JsonNode tokenNode = tokenNodeContentNode.path("token");

            return tokenNode.textValue();
        }
        return null;
    }

    /**
     * 获取添加车辆
     */
    public static  List<String> reqCars(String token,RestTemplate restTemplate,String getLocalCarsUrl,ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Map<String, Object> reqOrderMap = new HashMap<>();

        List<String> carVos = new LinkedList<>();
        ResponseEntity<String> responseCars = RequestServerUtil.getRequestToken(restTemplate,getLocalCarsUrl,reqOrderMap,token);
        try {
            if(responseCars.getStatusCode().value()==200){
                String carsContent = responseCars.getBody().toString();
                JsonNode carsContentNode = objectMapper.readTree(carsContent);
                JsonNode carNode = carsContentNode.path("objData");
                Iterator<JsonNode> iterator = carNode.elements();
                while (iterator.hasNext()) {
                    JsonNode result = iterator.next();
                    JsonNode resultNode = objectMapper.readTree(result.toString());
                    carVos.add(resultNode.path("plateNumber").textValue().toString());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return carVos;
    }

    /**
     * 获取用户名下的车辆
     * @param driverLicense
     * @return
     */
    public static  List<String> reqSixInOneCars(String driverLicense,RestTemplate restTemplate,String getSixInOneCarsUrl,ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(driverLicense)) {
            return null;
        }
        Map<String, String> reqOrderMap = new HashMap<>();
        reqOrderMap.put("certificateNumber",driverLicense);
        reqOrderMap.put("certificateType","A");
        String token_id = "2b254bec-dd48-11e6-81f7-9457a5545c84";
        List<String> carVos = new LinkedList<>();

        try {
            ResponseEntity<String> responseCars = RequestServerUtil.getRequestFromToken(restTemplate,getSixInOneCarsUrl,reqOrderMap,token_id);
            if(responseCars.getStatusCode().value()==200){
                String carsContent = responseCars.getBody().toString();
                JsonNode carsContentNode  = objectMapper.readTree(carsContent);
                Iterator<JsonNode> iterator = carsContentNode.elements();
                while (iterator.hasNext()) {
                    JsonNode result = iterator.next();
                    JsonNode resultNode = objectMapper.readTree(result.toString());
                    carVos.add(resultNode.path("number").textValue().toString());
                }
            }
        } catch (IOException e) {
            return null;
        }
        return carVos;
    }

    /**
     * 优驾行添加车辆比对六合一车辆
     *
     * @param driverLicense
     * @return
     */
    public static boolean compareCars(String plateNumber, String driverLicense,RestTemplate restTemplate,String getSixInOneCarsUrl,ObjectMapper objectMapper){
        boolean flag = true;

        List<String> sixInOneCars  = CouponActivityUtil.reqSixInOneCars(driverLicense,restTemplate,getSixInOneCarsUrl,objectMapper);//获取本人名下车辆
        if(StringUtils.isEmpty(sixInOneCars)){
            return true;
        }
        if(sixInOneCars.contains(plateNumber)){
            flag = false;
        }
        return flag;
    }
}
