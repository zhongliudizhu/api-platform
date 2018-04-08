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
@Table(name = "cbc_care_juan_list")
public class CareJuanList {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String accountId;

    private String name;

    private String phoneNumber;

    private String plateNumber;

    private Date creatTime;
}
