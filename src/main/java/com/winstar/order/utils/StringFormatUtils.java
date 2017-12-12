package com.winstar.order.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 数字自增格式化
 * Created by dpw on 2016/7/14.
 */
public class StringFormatUtils {
    private static final String STR_FORMAT = "00000";

    /**
     * 删除末尾的0
     * @param src
     * @return
     */
    public static String delZero(String src) {
        if(!StringUtils.isEmpty(src))
            return src.replaceAll("0*$", "");
        else
            return src;
    }


    /**
     * 自动加 1
     * @param serialNumber
     * @return
     */
    public static String AutoSub(String serialNumber){
        Integer serial = Integer.parseInt(serialNumber);
        serial++;
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        return df.format(serial);
    }

   /* public static void main(String[] args) {
            System.out.println(StringFormatUtils.AutoSub("000001"));
    }*/

    /**
     * @Description:Json工具类
     * 有序集合以'['开始,以']'结束;其他以'{'开始，以'}'结束.
     * java对象转字符串可以使用JSONArray或JSONObject.
     * @author:bao
     * @date:Dec 24, 2011
     * @see JSONArray
     */

        /**
         * @Description:POJO转Json字符串
         * @param obj
         * @return String
         */
        public static String bean2JsonStr(Object obj) {
            JSONObject jsonObj = JSONObject.fromObject(obj);

            return StringFormatUtils.toString(jsonObj);
        }

        /**
         * @Description:Json字符串转POJO对象
         * @param jsonStr
         * @return Object
         */
        public static Object jsonStr2Bean(String jsonStr, Class<?> clazz) {
            Object obj;

            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            obj = JSONObject.toBean(jsonObject, clazz);

            return obj;
        }

    /**
     * @Description:Json字符串转POJO对象
     * @param jsonStr
     * @return Object
     */
    public static Object jsonStr2Bean2(String jsonStr, Class<?> clazz,Map<String,Class> map) {
        Object obj;

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        obj = JSONObject.toBean(jsonObject, clazz,map);

        return obj;
    }

        /**
         * @Description:Json字符串转Map<String, Object>
         * @param jsonStr
         * @return Map<String,Object>
         */
        public static Map<String, Object> jsonStr2Map(String jsonStr) {

            Map<String, Object> result = new HashMap<String, Object>();

            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            Iterator<?> keys = jsonObj.keys();
            String key;
            Object val;

            while (keys.hasNext()) {
                key = (String) keys.next();
                val = jsonObj.get(key);
                result.put(key, val);
            }

            return result;
        }

        /**
         * @Description:Json字符串转List,内部对象为Object，需要手动转换为制定的对象类型
         * @param jsonStr
         * @param clazz
         * @return List
         */
        public static List jsonStr2List(String jsonStr, Class clazz) {
            List result = new ArrayList();

            JSONArray jsonArray = JSONArray.fromObject(jsonStr);
            JSONObject jsonObj;
            Object pojoVal;

            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                JSONUtils.getMorpherRegistry().registerMorpher(new TimestampToDateMorpher());
                pojoVal = JSONObject.toBean(jsonObj, clazz);
                result.add(pojoVal);
            }

            return result;
        }


    /**
     * @description:json字符串转map集合
     *
     * @param jsonStr json字符串
     * @return result
     */
    public static List<Map<String,Object>> jsonStrToList(String jsonStr){
            List result = new ArrayList();
            JSONArray jsonArray = JSONArray.fromObject(jsonStr);
            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> map = new HashMap();
                JSONObject jsonObj = JSONObject.fromObject(jsonArray.get(i));
                Iterator<?> keys = jsonObj.keys();
                String key;
                Object val;

                while (keys.hasNext()) {
                    key = (String) keys.next();
                    val = jsonObj.get(key);
                    map.put(key, val);
                }
                result.add(map);
            }
            return  result;

        }

        public static Object[] jsonStr2ObjectArray(String jsonStr) {

            JSONArray jsonArray = JSONArray.fromObject(jsonStr);
            Object[] result = new Object[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                result[i] = jsonArray.get(i);

            }
            return result;
        }

        /**
         * @Description:将Object转化为String
         * @param obj 指定对象，默认值为""
         * @return String
         */
        public static String toString(Object obj) {
            return toString(obj, "");
        }

        /**
         * @Description:将Object转化为String
         * @param obj 指定对象
         * @param defaultStr 为空的默认字符串
         * @return String
         */
        public static String toString(Object obj, String defaultStr) {
            String result = defaultStr;
            if (obj != null) {
                result = obj.toString();
            }
            return result;
        }

        /**
         *将日期字符串转为纯数字，去掉 :  -  空格   yyyy-MM-dd HH:mm:ss → yyyyMMddHHmmss
         *
          * @param dateStr
         * @return
         */
        public static String dateToString(String dateStr){
            dateStr = dateStr.replace(" ","");
            dateStr = dateStr.replace(":","");
            dateStr = dateStr.replace("-","");
            return dateStr;
        }

}
