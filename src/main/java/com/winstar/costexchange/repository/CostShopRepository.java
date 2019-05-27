package com.winstar.costexchange.repository;

import com.winstar.costexchange.entity.CostShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zl on 2019/5/21
 */
public interface CostShopRepository extends JpaRepository<CostShop, String> {

    List<CostShop> findByRemarkOrderByAmountAsc(String remark);

}
