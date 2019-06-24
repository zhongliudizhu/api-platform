package com.winstar.couponActivity.utils;

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
    ACTIVITY_ID_102("龙途乐享卡月度日均存款达标-50元", 102),
    ACTIVITY_ID_666("新手活动", 666),
    ACTIVITY_ID_667("裂变活动", 667),
    ACTIVITY_ID_105("百万加油补贴", 105),
    ACTIVITY_ID_106("新办交安卡", 106),
    ACTIVITY_ID_109("领取50元优惠包", 109),

    ACTIVITY_ID_201("交安卡周四秒杀", 201),
    ACTIVITY_ID_202("优驾行周四秒杀", 202),

    ACTIVITY_ID_204("锦鲤活动", 204),


    ACTIVITY_STATUS_0("未领取", 0),
    ACTIVITY_STATUS_1("领取", 1),
    ACTIVITY_STATUS_2("重复领取", 2),
    ACTIVITY_STATUS_3("已售罄", 3),
    ACTIVITY_STATUS_4("已使用", 4),
    ACTIVITY_STATUS_5("已过期", 5),

    ACTIVITY_VERIFY_0("未领取千万加油补贴", 0),
    ACTIVITY_VERIFY_1("已领取", 1),

    ACTIVITY_sign_1("纯信", 1),
    ACTIVITY_sign_2("纯储", 2),
    ACTIVITY_sign_3("交叉", 3);

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
