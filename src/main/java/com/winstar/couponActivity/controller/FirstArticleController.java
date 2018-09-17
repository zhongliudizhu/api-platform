package com.winstar.couponActivity.controller;

import com.google.common.collect.Maps;
import com.winstar.carLifeMall.repository.CarLifeOrdersRepository;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.*;
import com.winstar.couponActivity.repository.*;
import com.winstar.couponActivity.service.InviteUserService;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.couponActivity.utils.UtilConstants;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CouponActivityController
 *
 * @author: liuyw
 * @create： 2018-08-29 9:39
 * @DESCRIPTION: 裂变活动
 **/
@RestController
@RequestMapping("/api/v1/cbc/firstArticle")
public class FirstArticleController {

    @Autowired
    ActivityFirstArticleRepository activityFirstArticleRepository;
    /**
     * 获取该活动的首推文章
     * @param request
     * @param activityId
     * @return
     */
    @RequestMapping(value = "getFirstArticleByType",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ActivityFirstArticle getFirstArticleByType(HttpServletRequest request,String activityId)throws NotFoundException{
        if (StringUtils.isEmpty(activityId)){
            throw new NotFoundException("param.is.null");
        }
        ActivityFirstArticle activityFirstArticle=activityFirstArticleRepository.findByActivityIdAndStatus(activityId,1);
        if (StringUtils.isEmpty(activityFirstArticle)){
            throw new NotFoundException("query.is.null");
        }
        return activityFirstArticle;
    }
}
