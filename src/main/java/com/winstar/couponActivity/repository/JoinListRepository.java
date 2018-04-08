package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.CareJuanList;
import com.winstar.couponActivity.entity.JoinList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * CareJuanListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:
 **/
public interface JoinListRepository extends JpaRepository<JoinList,String>,JpaSpecificationExecutor<JoinList> {
    JoinList findByAccountId(String accountId);

    List<JoinList> findByCreateTimeLessThanEqual(Date time);
}
