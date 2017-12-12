package com.winstar.coupon.repository;

import com.winstar.coupon.entity.MyOilCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 名称： MyOilCouponRepository
 * 作者： sky
 * 日期： 2017-12-12 9:37
 * 描述：
 **/
public interface MyOilCouponRepository extends JpaSpecificationExecutor<MyOilCoupon>,JpaRepository<MyOilCoupon,String> {


}
