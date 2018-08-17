package com.winstar.couponActivity.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 新用户活动参与记录表
 **/
@Entity
@Data
@Table(name = "cbc_new_user_activity")
public class NewUserActivity {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 参加活动的用户id
     */
    private  String accountId;
    /**
     * 有效期结束时间
     */
    private Date validEndAt;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 活动状态 0 正常 1过期 2已经参与了活动
     */
    private Integer acStatus;
}
