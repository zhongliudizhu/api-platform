package com.winstar.oilOutPlatform.repository;

import com.winstar.oilOutPlatform.entity.OutOilCouponLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zl on 2019/10/9
 */
public interface OutOilCouponLogRepository extends JpaRepository<OutOilCouponLog, String> {

    List<OutOilCouponLog> findByOilIdAndOrderId(String oilId,String orderId);
}
