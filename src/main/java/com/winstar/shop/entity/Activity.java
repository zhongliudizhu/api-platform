package com.winstar.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 名称： Activity
 * 作者： sky
 * 日期： 2017-12-12 9:30
 * 描述： 活动实体
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_activity")
public class Activity {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 活动名称
     */
    private String name;
    /**
     * 创建时间
     */
   private Date createTime;
    /**
     * 活动开始时间
     */
   private Date beginTime;
    /**
     * 活动结束时间
     */
   private Date endTime;
    /**
     * 启用状态 0 未启用 1 已启用
     */
   private Integer status;
    /**
     * 商品 [1,2,3] JSONArray
     */
   private String goods;

}
