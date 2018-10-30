package com.winstar.carLifeMall.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.winstar.user.utils.ServiceManager;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 名称： Seller
 * 作者： dpw
 * 日期： 2018-05-03 16:05
 * 描述： 类别
 **/
@Entity
@Data
@Table(name = "CBC_CAR_LIFE_CATEGORY")
public class Category {
    @Transient
    public static final Integer STATUS_NORMAL = 0;
    @Transient
    public static final Integer STATUS_DELETE = 2;
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 名称
     */
    @Column(length = 50)
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 状态 0正常 1锁定 2删除
     */
    private Integer status;

    @Transient
    public List<Item> itemsList;
}
