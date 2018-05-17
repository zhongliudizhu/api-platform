package com.winstar.cashier.comm;

/**
 * Created by zl on 2017/2/22
 */
public enum EnumType {

    PAY_STATE_NO(0,"0","未支付"),
    PAY_STATE_SUCCESS(1,"1","支付成功"),
    PAY_STATE_FAIL(2,"2","支付失败"),

    SOURCE_WECHAT(1,"1","wechat"),
    SOURCE_ANDROID(2,"2","android"),
    SOURCE_IOS(3,"3","IOS"),
    SOURCE_XIAOCHENGXU(0,"0","小程序"),
    SOURCE_PC(4,"4","PC"),

    PAY_BANKCODE_CONDTRUCTION(105,"105","建行"),
    PAY_BANKCODE_CONDTRUCTION_CREDIT(106,"106","建行信用卡"),
    PAY_BANKCODE_CONDTRUCTION_DEBIT(107,"107","建行储蓄卡"),
    PAY_BANKCODE_ALIPAY(992,"992","支付宝"),
    PAY_BANKCODE_WECHAT(991,"991","微信"),
    PAY_BANKCODE_UNIONPAY(999,"999","银联"),

    PAY_WAY_CONSTRUCTION(0,"0","建行"),
    PAY_WAY_UNIONPAY(1,"1","银联"),
    PAY_WAY_WEIXIN(2,"2","微信"),
    PAY_WAY_WEIXIN_APP(200,"200","微信APP"),
    PAY_WAY_WEIXIN_PUBLIC_NUMBER(201,"201","微信公众号"),
    PAY_WAY_WEIXIN_H5(202,"202","微信H5"),
    PAY_WAY_WEIXIN_X(203,"203","微信小程序"),
    PAY_WAY_WEIXIN_SM(204,"204","微信扫码"),
    PAY_WAY_WEIXIN_CARD(205,"205","微信刷卡"),
    PAY_WAY_ALIPAY(3,"3","支付宝"),
    PAY_WAY_ALIPAY_APP(300,"300","支付宝APP"),
    PAY_WAY_ALIPAY_MOBILE(301,"301","支付宝手机网站"),
    PAY_WAY_ALIPAY_PC(302,"302","支付宝电脑网站"),
    PAY_WAY_ALIPAY_FACE(303,"303","支付宝当面付"),
    PAY_WAY_ALIPAY_HB(304,"304","支付宝花呗"),

    PAY_SHOPNAME_ILLEGAL(0,"违法缴费","违法缴费"),
    PAY_SHOPNAME_OILCARD(1,"油券订单","油券订单"),
    PAY_SHOPNAME_SAFE(2,"保险订单","保险订单"),
    PAY_SHOPNAME_CHARGE(3,"代办订单","代办订单"),
    PAY_SHOPNAME_VALIDATECAR(4,"审车订单","审车订单"),
    PAY_SHOPNAME_CARSERVICE(5,"汽车服务","汽车服务"),
    ;

    private final Integer value;
    private final String valueStr;
    private final String description;

    EnumType(Integer value, String valueStr, String description) {
        this.value = value;
        this.valueStr = valueStr;
        this.description = description;
    }

    public Integer value() {
        return this.value;
    }
    public String description() { return this.description; }
    public String valueStr() { return this.valueStr; }

}
