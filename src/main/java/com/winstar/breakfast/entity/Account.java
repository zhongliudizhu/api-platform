package com.winstar.breakfast.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2020/1/6
 */
@Data
@Entity
@Table(name = "breakfast_account")
public class Account {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(20) comment '手机号'")
    private String phone;

    /**
     * 是否删除 yes/no
     */
    @Column(columnDefinition = "varchar(10) comment '是否删除 yes/no'")
    private String del;

    /**
     * 创建日期
     */
    @Column(columnDefinition = "datetime comment '创建日期'")
    private Date createdAt;

}
