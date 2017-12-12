package com.winstar.oil.repository;

import com.winstar.oil.entity.OilCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 名称： OilCouponRepository
 * 作者： sky
 * 日期： 2017-12-12 9:38
 * 描述：
 **/
public interface OilCouponRepository extends JpaSpecificationExecutor<OilCoupon>,JpaRepository<OilCoupon,String> {


}
