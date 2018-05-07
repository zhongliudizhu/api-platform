package com.winstar.carLifeMall.service;

import com.winstar.carLifeMall.entity.Category;
import com.winstar.user.utils.ServiceManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 名称： CategoryService
 * 作者： dpw
 * 日期： 2018-05-07 9:24
 * 描述： 类别
 **/
@Service
public class CategoryService {
    @Cacheable(value = "getCarLifeCategory", keyGenerator = "tkKeyGenerator")
    public List<Category> findByStatus() {
        return ServiceManager.categoryRepository.findByStatus(Category.STATUS_NORMAL);
    }

}
