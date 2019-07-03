package com.winstar.carActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

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
     * 活动类型 1，建行交安卡 2，移动用户
     */
    @Column(columnDefinition = "varchar(10) COMMENT '活动类型'")
    private String type;

}
