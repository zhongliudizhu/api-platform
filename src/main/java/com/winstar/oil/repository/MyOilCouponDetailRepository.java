package com.winstar.oil.repository;

import com.winstar.oil.entity.MyOilCouponDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 名称： MyOilCouponDetailRepository
 * 作者： sky
 * 日期： 2017-12-12 9:38
 * 描述：
 **/
public interface MyOilCouponDetailRepository  extends JpaRepository<MyOilCouponDetail,String>,JpaSpecificationExecutor<MyOilCouponDetail>{


}
