package com.winstar.oil.repository;

import com.winstar.oil.entity.OilCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 名称： OilCouponRepository
 * 作者： sky
 * 日期： 2017-12-12 9:38
 * 描述：
 **/
public interface OilCouponRepository extends JpaSpecificationExecutor<OilCoupon>,JpaRepository<OilCoupon,String> {

    Page<OilCoupon> findByPanAmtAndOilState(Double panAmt, String oilState, Pageable pageable);

    @Query(value = "select * from cbc_oil_coupon where oil_state=0 order by RAND() limit 50", nativeQuery = true)
    List<OilCoupon> findRandomOilCoupons();

}
