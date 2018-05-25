package com.winstar.carLifeMall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 商品
 **/
@Entity
@Data
@Table(name = "CBC_EARLY_AND_EVENING_MARKET_CONFIG", indexes = {
        @Index(name = "idx_type", columnList = "type")})
public class EarlyAndEveningMarketConfig {

    @Transient
    public static final Integer TYPE_EARLY_MARKET = 1;
    @Transient
    public static final Integer TYPE_EVENING_MARKET = 2;

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 1 早市 2 晚市
     */
    private Integer type;
    /**
     * 开始时间
     */
    private int marketStartTime;

    /**
     * 结束时间
     */
    private int marketEndTime;

    @Column(length = 50)
    private String weekDay;


    @Transient
    private long currentTime;
}
