package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by qyc on 2018/10/25.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_eight_white_list")
public class EightWhiteList {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String driverLicense;

    private String phoneNumber;

    private String time;

    private Integer type;

    private String coupon;

    private String sendTime;

    private Integer sign;

    private String accountId;

    private Integer isGet;//0:未领取 1：已领取

    private String createTime;
    //交安卡卡号
    private String cardNumber;
}
