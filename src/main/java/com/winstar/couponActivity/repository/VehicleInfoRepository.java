package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.VehicleInfo;
import com.winstar.couponActivity.entity.VehicleValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * VehicleInfoRepository
 *
 * @author: Big BB
 * @create 2018-04-11 18:51
 * @DESCRIPTION:
 **/
public interface VehicleInfoRepository extends JpaRepository<VehicleInfo,String>,JpaSpecificationExecutor<VehicleInfo> {
}
