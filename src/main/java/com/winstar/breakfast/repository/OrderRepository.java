package com.winstar.breakfast.repository;

import com.winstar.breakfast.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zl on 2020/1/6
 */
public interface OrderRepository extends JpaRepository<Order, String> {
}
