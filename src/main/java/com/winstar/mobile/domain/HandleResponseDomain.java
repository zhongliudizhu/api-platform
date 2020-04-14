package com.winstar.mobile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2020/3/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HandleResponseDomain {

    /**
     * 交易流水
     */
    private String tradeId;

    /**
     * 号码归属地
     */
    private String dbSource;

    /**
     * 手机号
     */
    private String serialNumber;

    /**
     * 订单号
     */
    private String orderId;

}
