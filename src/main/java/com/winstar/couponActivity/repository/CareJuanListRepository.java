package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.CareJuanList;
import com.winstar.couponActivity.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * CareJuanListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:
 **/
public interface CareJuanListRepository extends JpaRepository<CareJuanList,String>,JpaSpecificationExecutor<CareJuanList> {
    List<CareJuanList> findByAccountId(String accountId);

    @Query("select o from CareJuanList o where o.creatTime between ?1 and ?2")
    List<CareJuanList> findByCreatTime(Date beginTime , Date endTime);

    @Query("select o from CareJuanList o where o.accountId = ?1 and o.type <> 0 and o.creatTime between ?2 and ?3")
    List<CareJuanList> findByAccountIdandJoinTypeAndTypeAndTime(String accountId,Date beginTime , Date endTime);

}
