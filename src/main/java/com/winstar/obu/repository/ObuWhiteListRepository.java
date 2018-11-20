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
    @Query("select o from ObuWhiteList o where o.phoneNumber = ?1  and o.isGet =?2 and o.timeStart <= ?3 and o.timeEnd >= ?4")
    ObuWhiteList checkWhiteList(String phoneNumber, Integer isGet ,Date timeStart, Date timeEnd);
    @Query(value = "SELECT card_number FROM cbc_obu_white_list WHERE SUBSTR(driver_license,-6)=?1 AND phone_number=?2",nativeQuery = true)
    String findByDriverLicenseAndPhoneNumber(String driverLicense,String phone);
}



