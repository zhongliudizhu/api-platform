package com.winstar.mobile.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ReceiveDomain {

    @NotEmpty(message = "模板id不能为空")
    private String templateId;
    @NotEmpty(message = "手机号不能为空")
    private String phone;
    @NotEmpty(message = "openId不能为空")
    private String openId;

}
