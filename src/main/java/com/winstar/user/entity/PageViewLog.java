package com.winstar.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户访问日志
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CBC_USER_PAGE_VIEW_LOG")
public class PageViewLog {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;
    /**
     * 活动ID
     */
    private String activityId;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 用户ID
     */
    private String accountId;
    /**
     * url
     */
    private String url;

    /**
     * 日志记录时间
     */
    @Column(length = 50)
    private Date createTime;
}
