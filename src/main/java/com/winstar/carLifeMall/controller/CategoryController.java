package com.winstar.carLifeMall.controller;

import com.winstar.carLifeMall.entity.Category;
import com.winstar.carLifeMall.entity.Item;
import com.winstar.carLifeMall.entity.ItemSellerRelation;
import com.winstar.carLifeMall.entity.Seller;
import com.winstar.carLifeMall.repository.CategoryRepository;
import com.winstar.carLifeMall.service.CategoryService;
import com.winstar.exception.NotFoundException;
import com.winstar.user.utils.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 名称： CategoryController
 * 作者： dpw
 * 日期： 2018-05-04 10:56
 * 描述： 类别
 **/
@RequestMapping("/api/v1/cbc/carLife/category")
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 获取类别
     *
     * @return
     * @throws NotFoundException
     */
    @RequestMapping("/list")
    public List<Category> findList() throws NotFoundException {
        List<Category> list = categoryService.findByStatus();

        if (list.size() == 0) throw new NotFoundException("category");

        return list;
    }
}
