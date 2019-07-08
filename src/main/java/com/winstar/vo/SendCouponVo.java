package com.winstar.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by zl on 2019/7/8
 */
@Getter
@Setter
public class SendCouponVo {

    /**
     * 优惠券id
     */
    @NotBlank(message = "优惠券id不能为空！")
    private String couponId;

    /**
     * 模板id
     */
    @NotBlank(message = "模板id不能为空！")
    private String templateId;

}
