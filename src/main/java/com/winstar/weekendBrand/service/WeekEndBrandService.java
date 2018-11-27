package com.winstar.weekendBrand.service;

import org.springframework.stereotype.Service;

/**
 * 名称 WeekEndBrandService
 * 作者 dpw
 * 日期 2018/11/21 10:27
 * 项目 winstar-cbc-platform-api
 * 描述
 */
@Service
public class WeekEndBrandService {
    /**
     * 计算距离周几还有几天
     *
     * @param activityWeekDayDay 目标星期几
     * @param curWeekDay         当前星期几
     * @return 距离天数  ，如果当前，返回0
     */
    public int calculateWeek(int activityWeekDayDay, int curWeekDay) {
        //todox 上线删除
       /* if (true) return 0;*/
        if (activityWeekDayDay > curWeekDay)
            return activityWeekDayDay - curWeekDay;
        else if (activityWeekDayDay < curWeekDay)
            return activityWeekDayDay;
        else
            return 0;
    }
}
