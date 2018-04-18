package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.LoveCarLog;
import com.winstar.couponActivity.entity.SaleVehicleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * SaleVehicleRecordRepository
 *
 * @author: Big BB
 * @create 2018-04-17 20:35
 * @DESCRIPTION:
 **/
public interface SaleVehicleRecordRepository extends JpaRepository<SaleVehicleRecord,String>,JpaSpecificationExecutor<SaleVehicleRecord> {

    SaleVehicleRecord findByAccountId(String accountId);
}
