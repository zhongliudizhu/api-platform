package com.winstar.activityCenter.vo;

import lombok.Data;

/**
 * @author UU
 * @Classname CouponTemplateVo
 * @Description TODO
 * @Date 2019/5/25 11:46
 */
@Data
public class CouponTemplateVo {

    private String id;

    private String amount;

    private Integer doorSkill;

    private String couponName;

    private String subTitle;

    public CouponTemplateVo getThis() {
        return this;
    }
}
