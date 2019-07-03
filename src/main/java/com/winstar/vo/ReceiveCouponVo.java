package com.winstar.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by zl on 2019/7/3
 */
@Getter
@Setter
public class ReceiveCouponVo {

    @NotBlank(message = "活动id不能为空！")
    private String activityId;

}
