package com.winstar.shop.service;

import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import org.springframework.stereotype.Service;

/**
 * 名称： ShopService
 * 作者： sky
 * 日期： 2017-12-13 9:46
 * 描述：
 **/
@Service
public interface ShopService {

    /**
     * 查询活动
     * @param id
     * @return
     */
    Activity findByActivityId(String id);

    /**
     * 查询商品
     * @param id
     * @return
     */
    Goods findByGoodsId(String id);

    /**
     * 查询活动详情
     * @param id
     * @return
     */
    Activity findByDetailId(String id);

}
