package com.winstar.oil.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * author: uu
 * Classname: OilStation
 * Description: TODO
 * Date: 2019/4/22 16:32
 */
@Entity
@Data
@Table(name = "cbc_oil_station")
public class OilStation implements Serializable {

    //序列化ID
    @Transient
    private static final long serialVersionUID = -5809782578272943999L;
    /**
     * 油站code
     */
    @Id
    @Column(columnDefinition = "varchar(11) comment '油站id'")
    private String id;

    /**
     * 油站名称
     */
    @Column(columnDefinition = "varchar(50) comment '油站名称'")
    private String name;


}
