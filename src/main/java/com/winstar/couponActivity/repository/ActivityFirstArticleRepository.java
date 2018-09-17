package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.ActivityFirstArticle;
import com.winstar.couponActivity.entity.CouponActivity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * ActivityFirstArticleRepository
 *
 * @author: Big BB
 * @create 2018-03-16 10:02
 * @DESCRIPTION:
 **/
public interface ActivityFirstArticleRepository extends PagingAndSortingRepository<ActivityFirstArticle,String> {

    /**
     * 根据活动和状态获取
     * @param activityId
     */
    ActivityFirstArticle findByActivityIdAndStatus(String activityId,Integer status);
}
