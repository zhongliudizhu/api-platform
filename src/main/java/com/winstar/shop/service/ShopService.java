package com.winstar.shop.service;

import com.alibaba.fastjson.JSONArray;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 名称： ShopService
 * 作者： sky
 * 日期： 2017-12-13 9:46
 * 描述：
 **/
@Service
public class ShopService {
    private Logger logger= LoggerFactory.getLogger(ShopService.class);
    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    GoodsRepository goodsRepository;
    /**
     * 查询活动
     *
     * @param id 活动Id
     * @return Activity
     */
    public Activity findByActivityId(String id){
        logger.info("查询活动详情 ："+id);
        return activityRepository.findOne(id);
    }

    /**
     * 查询商品
     *
     * @param id 商品ID
     * @return Goods
     */
    public Goods findByGoodsId(String id){
        return goodsRepository.findOne(id);
    }

    /**
     * 查询活动详情 包含商品
     *
     * @param id 活动Id
     * @return Activity
     */
    public Activity findActivityDetailById(String id){
        Activity activity=activityRepository.findOne(id);
        if(activity.getGoods()!=null){
            JSONArray array=JSONArray.parseArray(activity.getGoods());
            List<Goods> goodsList=goodsRepository.findByIdIn(array);
            activity.setGoodsList(goodsList);
        }
        logger.info("查询活动详情 [商品]："+id);
        return activity;
    }

}
