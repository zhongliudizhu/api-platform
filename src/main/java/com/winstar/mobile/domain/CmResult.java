package com.winstar.mobile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2020/3/6
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CmResult {

    /**
     * 返回码
     */
    private String returnCode;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 业务对象
     */
    private Object dataInfo;

    public boolean isSuccess(){
        return returnCode.equals("1000");
    }

}
