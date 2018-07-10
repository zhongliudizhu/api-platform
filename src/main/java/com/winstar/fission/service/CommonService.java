package com.winstar.fission.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 名称： CommonService
 * 作者： dpw
 * 日期： 2018-07-10 9:57
 * 描述： commonService
 **/
@Service
public class CommonService {
    @Value("${activity_id_fission}")
    String activityIdFission;

    public String getActivityIdFission() {
        if (StringUtils.isEmpty(this.activityIdFission))
            return "999999999";
        return activityIdFission;
    }
}
