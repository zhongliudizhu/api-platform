package com.PageViewLog;

import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.ServiceManager;
import com.winstar.utils.AESUtil;

import java.util.Date;

/**
 * 名称 test
 * 作者 dpw
 * 日期 2018/11/7 14:31
 * 项目 winstar-cbc-platform-api
 * 描述
 */
public class test {
    public static void main(String[] args) throws Exception {

       int activityWeekDay = 6;

        System.out.println(calculateWeek(activityWeekDay, 1));
        System.out.println(calculateWeek(activityWeekDay, 2));
        System.out.println(calculateWeek(activityWeekDay, 3));
        System.out.println(calculateWeek(activityWeekDay, 4));
        System.out.println(calculateWeek(activityWeekDay, 5));
        System.out.println(calculateWeek(activityWeekDay, 6));
        System.out.println(calculateWeek(activityWeekDay, 7));

        Date curTime = DateUtil.addDay(new Date(),6);

     /*   System.out.println(AESUtil.decrypt("A1DAC05B97DB91D5EC033723C2BC05DAE826DCA97071B71B46B24B8F09F7FFB0",AESUtil.dekey));
       System.out.println(AESUtil.decrypt("1AF29CA6C6254DD2DE97901AE8EAF57432A8588539E91F6480231646D50BB8D0",AESUtil.dekey));
        System.out.println(AESUtil.decrypt("2F3023571098DD08A565FDAC2D8B3E68EDD312239AB0A8027CF3D38962D3566B",AESUtil.dekey));*/
    }
    /**
     * 计算距离周几还有几天
     *
     * @param activityWeekDayDay 目标星期几
     * @param curWeekDay         当前星期几
     * @return 距离天数  ，如果当前，返回0
     */
    public static int calculateWeek(int activityWeekDayDay, int curWeekDay) {
        if (activityWeekDayDay > curWeekDay)
            return activityWeekDayDay - curWeekDay;
        else if (activityWeekDayDay < curWeekDay)
            return activityWeekDayDay;
        else
            return 0;
    }

}
