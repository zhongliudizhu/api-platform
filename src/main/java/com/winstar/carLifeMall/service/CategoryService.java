package com.winstar.carLifeMall.service;

import com.winstar.carLifeMall.entity.Category;
import com.winstar.carLifeMall.entity.Item;
import com.winstar.carLifeMall.entity.ItemSellerRelation;
import com.winstar.carLifeMall.entity.Seller;
import com.winstar.redis.RedisTools;
import com.winstar.user.utils.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 名称： CategoryService
 * 作者： dpw
 * 日期： 2018-05-07 9:24
 * 描述： 类别
 **/
@Service
public class CategoryService {
    @Autowired
    RedisTools redisTools;

    /**
     * 商家
     *   cbc_car_life_seller
     *   status 0启用 1停用 2删除：0显示 1 2 不显示
     *   商品类别
     *   cbc_car_life_category
     *   status 0正常 1锁定 2删除 ：0 1 显示 2不显示
     *   商品管理
     *   cbc_car_life_item
     *   status 0上架 1 下架：0显示 1不显示
     *
     * @return
     */
    @Cacheable(value = "getCarLifeCategory", keyGenerator = "tkKeyGenerator")
    public List<Category> findByStatus() {

        List<Category> categoryList = ServiceManager.categoryRepository.findByStatusNot(Category.STATUS_DELETE);
        categoryList.forEach(categoryTemp -> {
            List<Item> item = ServiceManager.itemRepository.findByCategoryIdAndStatus(categoryTemp.getId(), Item.STATUS_NORMAL);
            item.forEach(itemTemp -> {
                {
                    List<ItemSellerRelation> listISR = ServiceManager.itemSellerRelationRepository.findByItemId(itemTemp.getId());
                    List<Seller> result = listISR.stream().map(itemSellerRelationTemp -> {
                        return ServiceManager.sellerRepository.findOne(itemSellerRelationTemp.getSellerId());
                    }).filter(t->t.getStatus()==Seller.STATUS_NORMAL).collect(toList());

                    itemTemp.setSeller(result);
                }
            });

            categoryTemp.setItemsList(item);
        });

        return categoryList;
    }

}
