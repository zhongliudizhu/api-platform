package com.winstar.oilOutPlatform.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

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
     * 输出平台用户Id
     */
    @NotBlank(message = "outUserId 不能为空！！")
    private String outUserId;

    /**
     * 数量
     */
    @NotNull(message = "number 不能为空！！")
    private String number;

    /**
     * 签名
     */
    @NotBlank(message = "sign 不能为空！！")
    private String sign;
}
