package com.winstar.obu.repository;

import com.winstar.couponActivity.entity.WhiteList;
import com.winstar.obu.entity.ObuWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * ObuWhiteListRepository
 *
 * @author: Big BB
 * @create 2018-06-28 16:30
 * @DESCRIPTION:
 **/
public interface ObuWhiteListRepository extends JpaRepository<ObuWhiteList,String>,JpaSpecificationExecutor<ObuWhiteList> {
    @Query("select o from ObuWhiteList o where o.phoneNumber = ?1 and o.driverLicense = ?2 and o.isGet =?3 and o.timeStart <= ?4 and o.timeEnd >= ?5")
    ObuWhiteList checkWhiteList(String phoneNumber, String driverLicense, Integer isGet ,Date timeStart, Date timeEnd);
}
