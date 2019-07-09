package com.winstar.communalCoupon.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author UU
 * @Classname TemplateRule
 * @Description TODO
 * @Date 2019/7/8 14:14
 */
@Entity
@Data
@Table(name = "cbc_template_rule")
public class TemplateRule {

    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;

    /**
     * 模板id
     */
    @Column(columnDefinition = "varchar(50) COMMENT '模板id'")
    private String templateId;

    /**
     * 是否可以转赠 yes可转增；no不可转增
     */
    @Column(columnDefinition = "varchar(10) COMMENT '是否可以转赠 yes可转增；no不可转增'")
    private String giftable;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private Date createdAt;
}
