package com.winstar.oil.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2020/1/16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_oil_black_list")
public class OilBlackList {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 用户id
     */
    @Column(columnDefinition = "varchar(50) comment '用户id'")
    private String accountId;

    /**
     * openId
     */
    @Column(columnDefinition = "varchar(50) comment 'openid'")
    private String openId;

    /**
     * 是否是白名单用户  yes/no
     */
    @Column(columnDefinition = "varchar(5) comment 'vip'")
    private String vip;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '用户id'")
    private Date createTime;

    /**
     * 加入原因
     */
    @Column(columnDefinition = "varchar(255) comment '加入黑名单原因'")
    private String mark;

}
