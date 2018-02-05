package com.winstar.order.repository;

import com.winstar.order.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2018/2/2 17:00.
 *         --
 */
public interface InsuranceRepository extends JpaRepository<Insurance, String>{
    List<Insurance> findByOpenId(String openId);

    List<Insurance> findByCreateTimeBetween( Date begin, Date end );
}
