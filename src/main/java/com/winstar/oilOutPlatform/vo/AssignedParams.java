package com.winstar.oilOutPlatform.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String merchant;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 数量
     */
    private long number;

}
