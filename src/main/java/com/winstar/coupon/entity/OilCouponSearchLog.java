package com.winstar.coupon.entity;

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
 * 名称： OilCouponSearchLog
 * 作者： sky
 * 日期： 2017private12private12 9:19
 * 描述： 油券查看记录
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oil_coupon_search_log")
public class OilCouponSearchLog {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 账号ID
     */
    private String accountId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 查看时间
     */
    private Date createTime;
    /**
     * 油卡编号
     */
    private String pan;
    /**
     * ip地址
     */
    private String ip;

}
