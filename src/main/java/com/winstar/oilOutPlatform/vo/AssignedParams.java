package com.winstar.oilOutPlatform.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by zl on 2019/10/9
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedParams {

    /**
     * 商户号
     */
    @NotBlank(message = "merchant 不能为空！！")
    private String merchant;

    /**
     * 订单号
     */
    @NotBlank(message = "orderId 不能为空！！")
    private String orderId;

    /**
     * 数量
     */
    @NotBlank(message = "number 不能为空！！")
    private long number;

    /**
     * 签名
     */
    @NotBlank(message = "sign 不能为空！！")
    private String sign;
}
