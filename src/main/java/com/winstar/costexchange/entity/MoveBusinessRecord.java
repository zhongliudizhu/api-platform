package com.winstar.costexchange.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cbc_move_business_record")
public class MoveBusinessRecord {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    private String id;

    /**
     * 用户id
     */
    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String accountId;

    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(50) comment '手机号'")
    private String phone;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private Date createdAt;


}
