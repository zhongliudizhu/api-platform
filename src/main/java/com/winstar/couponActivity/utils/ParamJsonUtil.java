package com.winstar.couponActivity.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BianJiang on 2017/3/9.
 */
public class ParamJsonUtil {
    public static Map<String,String> AnalysisParamJsonUtil(String json){
        Map<String,String> paramJsonMap=new HashMap<>();
        ObjectMapper jsonObject = new ObjectMapper();
        try {
            JsonNode rootNode = jsonObject.readTree(json);

            JsonNode paramtersNode = rootNode.path("paramters");

            /***********************发动机***********************/
            JsonNode basicParametersNode = paramtersNode.path("基本参数");
           //厂商
            JsonNode brandNode=basicParametersNode.path("厂商");
            paramJsonMap.put("brand",brandNode.textValue());

            /***********************发动机***********************/
            JsonNode engineNode = paramtersNode.path("发动机");
            //环保标准
            JsonNode environmentalStandardsNode = engineNode.path("环保标准");
            paramJsonMap.put("environmental",environmentalStandardsNode.textValue());

            //发动机排量
            JsonNode displacementNode = engineNode.path("排量(L)");
            paramJsonMap.put("displacement",displacementNode.textValue());
            //发动机排量
            JsonNode powerNode = engineNode.path("最大功率(kW)");
            paramJsonMap.put("power",powerNode.textValue());

        } catch (IOException e) {
            e.printStackTrace();
            return paramJsonMap;
        }
        return paramJsonMap;
    }
    public  static  String SortJson(Map map){
//        Map<String,String> sortMap=new LinkedHashMap<String, String>();
//        sortMap.put("电动天窗",map.get("电动天窗").toString());
//        sortMap.put("ABS防抱死",map.get("ABS防抱死").toString());
//        sortMap.put("EPS系统",map.get("EPS系统").toString());
//        sortMap.put("发动机防盗",map.get("发动机防盗").toString());
//        sortMap.put("遥控钥匙",map.get("遥控钥匙").toString());
//        sortMap.put("驻车雷达",map.get("驻车雷达").toString());
//        sortMap.put("胎压监测",map.get("胎压监测").toString());
//        sortMap.put("无钥匙启动",map.get("无钥匙启动").toString());
//        sortMap.put("多功能方向盘",map.get("多功能方向盘").toString());
//        sortMap.put("儿童座椅",map.get("儿童座椅").toString());
//        sortMap.put("四轮驱动",map.get("四轮驱动").toString());
//        sortMap.put("氙气大灯",map.get("氙气大灯").toString());
//        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(sortMap);
//        System.out.println(jsonObject);

        StringBuilder stringBuilder=new StringBuilder("{");
        stringBuilder.append("\"电动天窗\""+":\""+map.get("电动天窗").toString()+"\",");
        stringBuilder.append("\"ABS防抱死\""+":\""+map.get("ABS防抱死").toString()+"\",");
        stringBuilder.append("\"EPS系统\""+":\""+map.get("EPS系统").toString()+"\",");
        stringBuilder.append("\"发动机防盗\""+":\""+map.get("发动机防盗").toString()+"\",");
        stringBuilder.append("\"遥控钥匙\""+":\""+map.get("遥控钥匙").toString()+"\",");
        stringBuilder.append("\"驻车雷达\""+":\""+map.get("驻车雷达").toString()+"\",");
        stringBuilder.append("\"胎压监测\""+":\""+map.get("胎压监测").toString()+"\",");
        stringBuilder.append("\"无钥匙启动\""+":\""+map.get("无钥匙启动").toString()+"\",");
        stringBuilder.append("\"多功能方向盘\""+":\""+map.get("多功能方向盘").toString()+"\",");
        stringBuilder.append("\"儿童座椅\""+":\""+map.get("儿童座椅").toString()+"\",");
        stringBuilder.append("\"四轮驱动\""+":\""+map.get("四轮驱动").toString()+"\",");
        stringBuilder.append("\"氙气大灯\""+":\""+map.get("氙气大灯").toString()+"\"}");
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
