package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 类别
 **/
public interface CategoryRepository extends JpaRepository<Category, String>{
    List<Category> findByStatusNot(Integer status);
}
