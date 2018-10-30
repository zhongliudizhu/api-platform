package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * WhiteList
 *
 * @author: Big BB
 * @create 2018-03-16 14:10
 * @DESCRIPTION:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_white_list")
public class WhiteList {
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

}
