package com.winstar.oil.utils;

/**
 * ActivityIdEnum
 *
 * @author: Big BB
 * @create 2018-03-20 17:45
 * @DESCRIPTION:
 **/
public enum  ActivityIdEnum {
    ACTIVITY_ID_1("购加油券折上折优惠", 1),
    ACTIVITY_ID_2("购加油券返利", 2),
    ACTIVITY_ID_666("新手活动", 666);

    private final String name;
    private final int activity;

    ActivityIdEnum(String name, int activity) {
        this.name = name;
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public int getActivity() {
        return activity;
    }
}
