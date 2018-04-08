package com.winstar.couponActivity.vo;

/**
 * VerifyResult
 *
 * @author: Big BB
 * @create 2018-04-08 11:18
 * @DESCRIPTION:
 **/
public class VerifyResult {
    private String accountId;//账户

    private Integer type;//活动类型  101 102 103 104

    private Integer sign;// 1:纯信 2：纯储 3:交叉

    private Integer isGet;// 1:领取成功 2：已经领取 3:售罄

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public Integer getIsGet() {
        return isGet;
    }

    public void setIsGet(Integer isGet) {
        this.isGet = isGet;
    }
}
