package com.winstar.carLifeMall.param;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * 名称： CarLifeOrdersParam
 * 作者： dpw
 * 日期： 2018-05-07 10:08
 * 描述： 汽车生活订单参数
 **/
@Data
public class CarLifeOrdersParam {
    /**
     * 商家id
     */
    private String sellerId;
    /**
     * 商家名称
     */
    private String sellerName;
    /**
     * 商品名称
     */
    private String itemId;
    /**
     * 预约事项详情
     */
    private String itemName;
    /**
     * 预约时间
     */
    private Date reserveTime;
    /**
     * 预约手机号码
     */
    private String reserveMobile;
}
