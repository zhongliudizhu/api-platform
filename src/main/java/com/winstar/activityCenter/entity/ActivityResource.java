package com.winstar.activityCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cbc_activity_resource")
@Data
@Entity
public class ActivityResource implements Serializable {

    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;

    /**
     * 图片url
     */
    @Column(columnDefinition = "varchar(100) COMMENT '图片url'")
    private String photoUrl;

    /**
     * 资源url
     */
    @Column(columnDefinition = "varchar(100) COMMENT '资源url'")
    private String resourceUrl;

    /**
     * 活动名称
     */
    @Column(columnDefinition = "varchar(20) COMMENT '活动名称'")
    private String activityName;

    /**
     * 排序等级
     */
    @Column(columnDefinition = "varchar(10) COMMENT '排序等级'")
    private String type;

    /**
     * del 删除，normal 正常
     */
    @Column(columnDefinition = "varchar(20) COMMENT '活动状态'")
    private String status;


    /**
     * 活动类型
     */
    @Column(columnDefinition = "varchar(20) COMMENT '活动类型'")
    private String activityType;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    private Date createdAt;

}
