package com.winstar.costexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zl on 2019/5/21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "communal_coupon_cost_shop")
public class CostShop {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    private String id;

    /**
     * 优惠券模板id
     */
    @Column(columnDefinition = "varchar(50) comment '优惠券模板id'")
    private String templateId;

    /**
     * 话费金额
     */
    @Column(columnDefinition = "double(10,2) comment '话费金额'")
    private Double costAmount;

    /**
     * 备注
     */
    @Column(columnDefinition = "varchar(100) comment '备注'")
    private String remark;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private Date createdAt;

}
