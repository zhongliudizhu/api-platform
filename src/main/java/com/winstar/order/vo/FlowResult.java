package com.winstar.order.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shoo on 2017/12/27 18:22.
 *         -- 充值 提交结果
 */
@NoArgsConstructor
@Getter
@Setter
public class FlowResult {

    private String clientOrderId;
    private String phoneNo;
    private String prdInfo;
    private String orderStatus;
    private String desc;
}
