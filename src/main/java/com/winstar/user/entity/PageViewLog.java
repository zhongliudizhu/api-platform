package com.winstar.user.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 用户访问日志
 *
 * @author laohu
 * @date 2017/12/11 17:33
 * @desc
 **/
@Entity
@Table(name = "CBC_PLATFORM_USER_PAGE_VIEW")
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
    @Column(columnDefinition = "timestamp default current_timestamp")
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
