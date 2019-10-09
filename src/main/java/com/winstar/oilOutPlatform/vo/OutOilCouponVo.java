package com.winstar.oilOutPlatform.vo;


import lombok.Data;

import java.util.Date;



@Data
public class OutOilCouponVo {


    private String id;


    /**
     * 电子券金额
     */
    private Double panAmt;

    /**
     * 加油券名称
     */
    private String panName;


    /**
     * 加油券状态 0：未售、1：已售
     */
    private String oilState;


    /**
     * 销售时间
     */
    private Date saleTime;

    /**
     * 使用状态 0：未使用、1：已使用
     */
    private String useState;

    /**
     * 使用时间
     */
    private String useDate;

    /**
     * 使用的加油站Id
     */
    private String tId;

    /**
     * 使用的加油站名称
     */
    private String tName;

}
