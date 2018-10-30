package com.winstar.obu.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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
@Table(name = "cbc_obu_white_list")
public class ObuWhiteList {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String driverLicense;

    private String phoneNumber;

    private String name;

    private Date timeStart;

    private Date timeEnd;

    private String etc;

    private String coupon;

    private Date sendTime;

    private String accountId;

    private Integer isGet;//0:未领取 1：已领取

    private String createTime;

}
