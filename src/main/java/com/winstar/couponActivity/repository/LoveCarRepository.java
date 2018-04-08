package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.LoveCarLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * LoveCarRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:
 **/
public interface LoveCarRepository extends JpaRepository<LoveCarLog,String>,JpaSpecificationExecutor<LoveCarLog> {
    List<LoveCarLog> findByAccountId(String accountId);
}
