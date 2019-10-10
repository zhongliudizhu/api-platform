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
public class ActiveParams {

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空！！")
    private String merchant;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空！！")
    private String orderId;

    /**
     * 油券id
     */
    @NotBlank(message = "油券id不能为空！！")
    private String oilId;

    /**
     * 签名
     */
    @NotBlank(message = "签名不能为空！！")
    private String sign;


    /**
     * 输出平台用户Id
     */
    @NotBlank(message = "用户id不能为空！！")
    private String outUserId;

}
