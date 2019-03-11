package com.winstar.drawActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zl on 2019/3/11
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrawRecord {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 中奖奖项类型 1:99元优惠券  2:999元优惠券  空未中奖
     */
    @Column(length = 1)
    private String prizeType;

    /**
     * 用户Id
     */
    @Column(length = 50)
    private String accountId;

    /**
     * 交安卡号
     */
    @Column(length = 50)
    private String cardNumber;

    /**
     * 是否中奖  YES/NO
     */
    private String isPrized;

    /**
     * 中奖时间
     */
    private Date createdAt;

}
