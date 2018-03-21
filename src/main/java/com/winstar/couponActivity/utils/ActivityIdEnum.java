package com.winstar.couponActivity.utils;

/**
 * ActivityIdEnum
 *
 * @author: Big BB
 * @create 2018-03-20 17:45
 * @DESCRIPTION:
 **/
public enum  ActivityIdEnum {
    ACTIVITY_ID_101("消费满笔/额获赠加油优惠券-50元", 101),
    ACTIVITY_ID_102("消费满笔/额获赠加油优惠券-100元", 102),
    ACTIVITY_ID_103("购车分期获赠加油优惠券-100元", 103),
    ACTIVITY_ID_104("购车分期获赠加油优惠券-200元", 104),
    ACTIVITY_ID_105("购车分期获赠加油优惠券-300元", 105),
    ACTIVITY_ID_106("龙途乐享卡用户获赠加油优惠券-100元", 106);

    private final String name ;
    private final int activity ;

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
