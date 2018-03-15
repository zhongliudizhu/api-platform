package com.winstar.oil.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2018/3/12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_looking_used_coupon")
public class LookingUsedCoupon {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    @Column(length = 35)
    private String pan;

    @Column(length = 35)
    private String panText;

    @Column(length = 10)
    private String tId;

    private Date useDate;

    private Date lookDate;

    @Column(length = 1)
    private String activateState;

    private long beginTime;

    private long endTime;

    private long timeConsuming;

    @Column(length = 30)
    private String rc;

    @Column(length = 255)
    private String rcDetail;

}
