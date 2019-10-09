package com.winstar.oilOutPlatform.repository;

import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zl on 2019/10/9
 */
public interface OutOilCouponLogRepository extends JpaRepository<OutOilCouponLog, String> {

    OutOilCouponLog findByOrderId(String orderId);
}
