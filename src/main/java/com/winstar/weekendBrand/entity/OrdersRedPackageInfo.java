package com.winstar.weekendBrand.entity;

import com.winstar.order.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CBC_ORDERS_RED_PACKAGE_INFO")
public class OrdersRedPackageInfo {
    public static final int RECEIVE_STATUS_YES = 2;
    public static final int RECEIVE_STATUS_NO = 1;
    public static final String ACTIVITY_ID_WEEKEND_BRAND = "203";

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 活动ID
     */
    @Column(length = 50)
    private String activityId;

    /**
     * 订单ID
     */
    @Column(length = 60)
    private String orderId;

    /**
     * 领取人
     */
    @Column(length = 50)
    private String receiveAccountId;

    /**
     * 领取状态
     */
    private Integer receiveStatus;

    /**
     * 优惠券面额
     */
    private BigDecimal couponPrice;

    /**
     * 领取时间
     */
    private Date receiveTime;

    /**
     * 创建时间
     */
    @Column(length = 50)
    private Date createTime;

    /**
     * 过期时间
     */
    private Date expiredTime;

    public OrdersRedPackageInfo init(String orderId, BigDecimal couponPrice) {
        this.setActivityId(ACTIVITY_ID_WEEKEND_BRAND);
        this.setCreateTime(new Date());
        this.setOrderId(orderId);
        this.setReceiveStatus(OrdersRedPackageInfo.RECEIVE_STATUS_NO);
        this.setCouponPrice(couponPrice);
        this.setExpiredTime(DateUtil.addMonth(this.getCreateTime(), 1));
        return this;
    }
}
