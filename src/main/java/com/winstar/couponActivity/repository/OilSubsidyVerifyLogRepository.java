package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.OilSubsidyVerifyLog;
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
public interface OilSubsidyVerifyLogRepository extends JpaRepository<OilSubsidyVerifyLog,String>,JpaSpecificationExecutor<OilSubsidyVerifyLog> {
    OilSubsidyVerifyLog findByAccountId(String accountId);

    List<OilSubsidyVerifyLog> findByCreateTimeLessThanEqual(Date time);

}
