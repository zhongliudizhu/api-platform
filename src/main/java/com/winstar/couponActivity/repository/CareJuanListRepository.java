package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.CareJuanList;
import com.winstar.couponActivity.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * CareJuanListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:
 **/
public interface CareJuanListRepository extends JpaRepository<CareJuanList,String>,JpaSpecificationExecutor<CareJuanList> {
    CareJuanList findByAccountId(String accountId);
}
