package com.winstar.carLifeMall.repository;

import com.winstar.carLifeMall.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名称： ItemRepository
 * 作者： dpw
 * 日期： 2018-05-04 10:45
 * 描述： 商品
 **/
public interface SellerRepository extends JpaRepository<Seller, String>{

}
