package com.winstar.coupon.util;

/**
 * ActivityIdEnum
 *
 * @author: Big BB
 * @create 2018-03-20 17:45
 * @DESCRIPTION:
 **/
public enum MyCouponEnum {
    COUPON_IS_USE_0("未使用", 0),
    COUPON_NOT_USE_1("已使用", 1),
    COUPON_NOT_USE_2("已失效", 1);


    private final String name;
    private final int status;



    MyCouponEnum(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}
