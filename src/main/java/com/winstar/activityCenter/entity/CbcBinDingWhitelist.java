package com.winstar.activityCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cbc_binding_white")
@Data
@Entity
public class CbcBinDingWhitelist {
    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;
    /**
     * 手机号
     */
    @Column(columnDefinition = "varchar(20) COMMENT '手机号'")
    private String phone;
    /**
     * 有效时间
     */
    @Column(columnDefinition = "datetime COMMENT '有效时间'")
    private Date validTime;
    /**
     * 优惠券模板id
     */
    @Column(columnDefinition = "varchar(100) COMMENT '优惠券模板id'")
    private String couponId;
    /**
     * 认证状态 0 未认证 1 已认证
     */
    @Column(columnDefinition = "varchar(2) COMMENT '认证状态'")
    private String state;
    /**
     * 面值
     */
    @Column(columnDefinition = "varchar(2) COMMENT '面值'")
    private String faceValue;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    private Date createTime;
}
