package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.CareJuanList;
import com.winstar.couponActivity.entity.NewUserActivity;
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
public interface NewUserActivityRepository extends JpaRepository<NewUserActivity,String>,JpaSpecificationExecutor<NewUserActivity> {
    /**
     * 根据用户id查询活动记录
     * @param accountId
     * @return
     */
    NewUserActivity findByAccountId(String accountId);
}
