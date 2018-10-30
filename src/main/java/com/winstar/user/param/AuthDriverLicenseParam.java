package com.winstar.user.param;

import com.winstar.exception.NotRuleException;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 名称： AuthDriverLicenseParam
 * 作者： dpw
 * 日期： 2018-07-10 15:05
 * 描述： 优驾行绑定驾驶证参数校验
 **/
@Data
public class AuthDriverLicenseParam {
    private String serialNum;
    private String infoCard;
    private String verify;
    private String phone;

    public void checkParam() throws NotRuleException {
        if (StringUtils.isEmpty(getSerialNum())) {
            throw new NotRuleException("serialNum");
        } else if (StringUtils.isEmpty(getInfoCard()))
            throw new NotRuleException("infoCard");
        else if (StringUtils.isEmpty(getVerify()))
            throw new NotRuleException("verify");
        else if (StringUtils.isEmpty(getPhone()))
            throw new NotRuleException("phone");
    }
}
