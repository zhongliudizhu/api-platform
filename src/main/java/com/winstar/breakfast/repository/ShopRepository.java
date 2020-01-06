package com.winstar.breakfast.repository;

import com.winstar.breakfast.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zl on 2020/1/6
 */
public interface ShopRepository extends JpaRepository<Shop, String> {
}
