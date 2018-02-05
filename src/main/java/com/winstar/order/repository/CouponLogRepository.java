package com.winstar.order.repository;

import com.winstar.order.entity.CouponLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author shoo on 2018/2/2 17:00.
 *         --
 */
public interface CouponLogRepository extends JpaRepository<CouponLog, String> {

    List<CouponLog> findByOpenId(String openId);
}
