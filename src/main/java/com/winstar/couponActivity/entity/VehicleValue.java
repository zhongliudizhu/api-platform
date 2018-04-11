package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * VehicleValue
 *
 * @author: Big BB
 * @create 2018-04-11 18:32
 * @DESCRIPTION:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jh_id1_che")
public class VehicleValue {
    /**
     * 唯一标识
     */
    @Id
    private String id;

    private String name;

    private String phone;

    private String hphm;

    private String hpzl;

    private String fdjh;

    private String clsbdh;

    private String cdrq;

    private String clpp1;

    private String clxh;

    private String xh;

    private String cl;

    private Integer value;

}
