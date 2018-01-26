package com.winstar.user.vo;


import java.util.List;

/**
 * Created by 5pines on 2016/10/14.
 */
public class AuthVerifyCodeMsgResult {
    private String status;
    private String errorMessage;

    /**
     * 返回结果数据集合
     */
    private List<AuthVerifyCodeEntity> results;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<AuthVerifyCodeEntity> getResults() {
        return results;
    }
    public void setResults(List<AuthVerifyCodeEntity> results) {
        this.results = results;
    }
}
