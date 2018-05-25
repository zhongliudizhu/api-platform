package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * CareJuanList
 *
 * @author: Big BB
 * @create 2018-04-03 10:06
 * @DESCRIPTION:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_join_list")
public class JoinList {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String accountId;


    private String plateNumber;

    private Integer isVerify;

    private Date createTime;

    @Transient
    private long number;
}
