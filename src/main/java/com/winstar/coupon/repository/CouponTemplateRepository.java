package com.winstar.coupon.repository;

import com.winstar.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 名称： CouponTemplateRepository
 * 作者： sky
 * 日期： 2017-12-12 9:48
 * 描述：
 **/
public interface CouponTemplateRepository extends JpaSpecificationExecutor<CouponTemplate>,JpaRepository<CouponTemplate,String> {


}
