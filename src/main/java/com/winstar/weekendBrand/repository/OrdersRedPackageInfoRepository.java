package com.winstar.weekendBrand.repository;

import com.winstar.weekendBrand.entity.OrdersRedPackageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrdersRedPackageInfoRepository extends JpaRepository<OrdersRedPackageInfo, String> {
    List<OrdersRedPackageInfo> findByReceiveAccountIdAndOrderId(String accoutId, String orderId);

    List<OrdersRedPackageInfo> findByOrderIdAndReceiveStatusAndExpiredTimeAfter(String orderId, Integer status, Date expiredTime);
}

