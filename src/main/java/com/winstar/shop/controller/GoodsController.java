package com.winstar.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.DateUtil;
import com.winstar.redis.RedisTools;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
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

/**
 * 名称： GoodsController
 * 作者： sky
 * 日期： 2017-12-12 11:36
 * 描述： 商品控制器
 **/
@RestController
@RequestMapping("/api/v1/cbc/goods")
public class GoodsController {

    public static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    static final Integer Status = 1;
    static final Integer HOUR_BEGIN = 7;
    static final Integer HOUR_END = 23;
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    OilOrderService oilOrderService;

    @Autowired
    RedisTools redisTools;

    /**
     * 根据活动Id查询商品
     *
     * @param request    HttpServletRequest
     * @param activityId 活动ID
     * @return
     * @throws MissingParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Goods> query(
            HttpServletRequest request,
            String activityId
    ) throws MissingParameterException, NotRuleException, NotFoundException {
        logger.info("查询商品列表，activityId=" + activityId);
        if (StringUtils.isEmpty(activityId)) throw new MissingParameterException("activityId");
        String accountId = accountService.getAccountId(request);
        logger.info("accountId=" + accountId);
        if(!redisTools.exists("activity_goods_" + activityId)){
            logger.info("redis中没有活动，从数据库中查询");
            Activity activity = activityRepository.findOne(activityId);
            if(!ObjectUtils.isEmpty(activity)){
                redisTools.set("activity_goods_" + activityId, JSON.toJSONString(activity));
            }
        }
        Activity activity = JSON.parseObject(redisTools.get("activity_goods_" + activityId).toString(), Activity.class);
        if(activity==null) {
            logger.info("活动不存在！");
            throw new NotFoundException("this activity is NotFound");
        }
        if (activity.getStatus() == 0) {
            logger.info("活动已关闭！");
            throw new NotFoundException("this activity is closed");
        }
        if (StringUtils.isEmpty(activity.getGoods()))  {
            logger.info("活动商品不存在！");
            throw new NotFoundException("this activity has no goods");
        }
        JSONArray array= JSONArray.parseArray(activity.getGoods());
        if(!redisTools.exists("activity_goods_list_" + activityId)){
            logger.info("redis中没有该活动的商品，从数据库中");
            List<Goods> list = goodsRepository.findByStatusAndIdInOrderByPriceAsc(Status,array);
            if(!ObjectUtils.isEmpty(list)){
                redisTools.set("activity_goods_list_" + activityId, JSON.toJSONString(list));
            }
        }
        List<Goods> list = JSON.parseArray(redisTools.get("activity_goods_list_" + activityId).toString(), Goods.class);
        logger.info("商品列表：" + list.size());
        if (list.size() == 0)  throw new NotFoundException("goods");
        return list;

    }

    /**
     * 每日7:00-24:00可抢购
     * @return
     */
    public static boolean checkTime(){
        int hour = DateUtil.getHour(new Date());
        return HOUR_BEGIN <= hour && hour <= HOUR_END;
    }

}
