package com.winstar.order.repository;

import com.winstar.cbcactivity.order.entity.OilOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2017/7/7 13:52.
 *         --  --
 */
public interface OilOrderRepository extends JpaRepository<OilOrder,String> {

}
