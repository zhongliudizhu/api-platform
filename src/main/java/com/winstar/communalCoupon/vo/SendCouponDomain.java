package com.winstar.communalCoupon.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2019/8/19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendCouponDomain {

    private String templateId;

    private String accountId;

    private String type;

    private String num;

    private String activityId;

    private String phone;

    public SendCouponDomain(String accountId, String type){
        this.accountId = accountId;
        this.type = type;
    }

}
