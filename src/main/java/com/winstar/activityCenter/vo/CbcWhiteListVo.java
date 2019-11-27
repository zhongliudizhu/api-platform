package com.winstar.activityCenter.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by zl on 2019/11/26
 */
@Data
public class CbcWhiteListVo {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空！")
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空！")
    private String randCode;

    /**
     * 活动编号
     */
    @NotBlank(message = "活动编号不能为空！")
    private String activityCode;

}
