package com.winstar.couponActivity.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * RandomUtil
 *
 * @author: Big BB
 * @create 2018-03-21 13:56
 * @DESCRIPTION:
 **/
public class RandomUtil {
    /**
     * 生成一个n位的随机数字符串
     *
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();


        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(9);// [1,9)
            sb.append(str.charAt(number+1));
        }
        return sb.toString();
    }


    /**
     * 生成一个n位的随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();


        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(36);// [0,36)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 获取当前时间，年月日时
     *
     * @return
     */
    public static String getDate() {
        String str = "";
        DateFormat df = new SimpleDateFormat("yyyyMMddHH");
        Date date = new Date();
        str = df.format(date);
        return str;
    }


    /**
     * 生成用户id，10位,纯数字
     *
     * @return
     */
    public static String userId() {
        String userId = "";
        userId = getRandomNum(10);
        return userId;
    }


    /**
     * 生成订单号，13位，纯数字
     *
     * @return
     */
    public static String orderId() {
        String orderId = "";
        String date = getDate();
        String rand = getRandomNum(13);
        orderId = date + rand;
        return orderId;
    }


    /**
     * 生成商品id，10位,纯数字
     *
     * @return
     */
    public static String proId() {
        String userId = "";
        userId = getRandomNum(10);
        return userId;
    }

    /**
     * 随机取list数据
     * @param list
     * @param n  取几条
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    public static List getRandomList(List list, int n) {
        Map map = new HashMap();
        List listNew = new ArrayList();
        if(list.size()<=n){
            return list;
        }else{
            while(map.size()<n){
                int random = (int) (Math.random() * list.size());
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    listNew.add(list.get(random));
                }
            }
            return listNew;
        }
    }


    /**
     * 取数组随机数
     * @param arr
     * @param n
     * @return
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public static Long[] createRandomArray(Long[] arr, int n) {
        // TODO Auto-generated method stub
        @SuppressWarnings("rawtypes")
        Map map = new HashMap();
        Long[] arrNew = new Long[n];
        if(arr.length<=n){
            return arr;
        }else{
            int count = 0;//新数组下标计数
            while(map.size()<n){
                int random = (int) (Math.random() * arr.length);
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    arrNew[count++] = arr[random];
                }
            }
            return arrNew;
        }
    }
}
