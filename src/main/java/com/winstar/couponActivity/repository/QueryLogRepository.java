package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zl on 2016/10/13
 */
public interface QueryLogRepository extends JpaRepository<QueryLog,Long> {

}
