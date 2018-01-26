package com.winstar.user.param;

/**
 * Created by 5pines on 2016/10/12.
 */
public class MsgContent {
    /**
     * 卡号
     */
    private String kh;
    /**
     * 手机号后4位
     */
    private String sjh;

    /**
     * 序号（请求id）
     */
    private String xh;
    /**
     * 验证码
     */
    private String yzm;

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public String getSjh() {
        return sjh;
    }

    public void setSjh(String sjh) {
        this.sjh = sjh;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getYzm() {
        return yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }
}
