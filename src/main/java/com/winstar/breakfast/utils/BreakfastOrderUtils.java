package com.winstar.breakfast.utils;

import com.winstar.breakfast.entity.Account;
import com.winstar.breakfast.entity.Order;
import com.winstar.order.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;

@Slf4j
public class BreakfastOrderUtils {


    /**
     * 随机生成6位数
     */
    public static int getRandomNum(int dig) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 9; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < dig; i++) {
            result = result * 10 + array[i];
        }
        return result;
    }

    /**
     * 生成订单序列号
     *
     * @return 订单序列号
     */
    public static String getSerialNumber() {
        int r1 = getRandomNum(5);
        int r2 = getRandomNum(5);
        String serialNumber = DateUtil.DateToString(new Date(), "yyyyMMddHHmmss");
        serialNumber = serialNumber + "wxzc" + String.valueOf(r1) + String.valueOf(r2);
        return serialNumber;
    }

    /**
     * 处理优惠券逻辑
     */
    public static Order checkCouponAndSetOrder(String couponId, Account account, Order order) {
        //TODO:metrics
        return null;
    }


}
