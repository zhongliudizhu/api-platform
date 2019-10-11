package com.winstar.oilOutPlatform.repository;

import com.winstar.oilOutPlatform.entity.OutOilCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by zl on 2019/10/9
 */
public interface OutOilCouponRepository extends JpaRepository<OutOilCoupon, String> {

    List<OutOilCoupon> findByOrderId(String orderId);

    List<OutOilCoupon> findByOilState(String oilState);

    List<OutOilCoupon> findByOilStateAndPanIn(String state,Set<Object> pans);

    OutOilCoupon findByIdAndOrderId(String oilId, String orderId);
}
