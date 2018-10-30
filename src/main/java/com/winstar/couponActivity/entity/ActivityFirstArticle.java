package com.winstar.couponActivity.entity;

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
 * CouponActivity
 *
 * @author Big BB
 * @create 2017-12-19 11:23
 * 活动微信公众号首推文章
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_activity_first_article")
public class ActivityFirstArticle {
    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    /**
     * 活动id
     */
    private String activityId;
    /**
     * 公众号id
     */
    private String publicId;

    /**
     * 文章id
     */
    private String articleId;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 状态
     */
    private Integer status;
}
