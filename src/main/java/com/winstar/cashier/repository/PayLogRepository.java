package com.winstar.cashier.repository;

import com.winstar.cashier.entity.PayLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 支付日志仓库
 */
public interface PayLogRepository extends JpaRepository<PayLog,String> {

    List<PayLog> findByOrderNumberAndCode(String orderNumber, String code);

}

