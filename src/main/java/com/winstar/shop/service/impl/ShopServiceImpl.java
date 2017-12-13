package com.winstar.shop.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.shop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 名称： ShopServiceImpl
 * 作者： sky
 * 日期： 2017-12-13 9:50
 * 描述：
 **/
@Service
public class ShopServiceImpl implements ShopService {
    private Logger logger= LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    GoodsRepository goodsRepository;

    @Override
    public Activity findByActivityId(String id) {
        logger.info("查询活动详情 ："+id);
        return activityRepository.findOne(id);
    }

    @Override
    public Goods findByGoodsId(String id) {
        return goodsRepository.findOne(id);
    }

    @Override
    public Activity findActivityDetailById(String id) {
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
