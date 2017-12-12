package com.winstar.order.repository;

import com.winstar.order.entity.OilOrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author shoo on 2017/7/7 14:46.
 *         --  --
 */
public interface OilOrderItemsRepository extends JpaRepository<OilOrderItems,String> {

}
