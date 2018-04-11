package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.LoveCarLog;
import com.winstar.couponActivity.entity.VehicleValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * VehicleValueRepository
 *
 * @author: Big BB
 * @create 2018-04-11 18:51
 * @DESCRIPTION:
 **/
public interface VehicleValueRepository extends JpaRepository<VehicleValue,String>,JpaSpecificationExecutor<VehicleValue> {
    VehicleValue findByHphm(String hphm);
}
