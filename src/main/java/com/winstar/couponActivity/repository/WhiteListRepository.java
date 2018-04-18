package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * WhiteListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:
 **/
public interface WhiteListRepository extends JpaRepository<WhiteList,String>,JpaSpecificationExecutor<WhiteList> {
    List<WhiteList> findByDriverLicenseAndPhoneNumberAndTime(String driverLicense, String phoneNumber, String time);

    List<WhiteList> findByPhoneNumberAndDriverLicenseAndIsGetAndTypeAndTime(String phoneNumber,String driverLicense,Integer isGet,Integer type, String time);

    @Query("select o from WhiteList o where o.phoneNumber = ?1 and o.driverLicense like CONCAT('%',?2) and o.isGet =?3 and o.time =?4")
//@Query("select o from WhiteList o where o.phoneNumber = ?1 and o.driverLicense = substring(?2,10) and o.isGet =?3 and o.time =?4")
    List<WhiteList> findByPhoneNumberAndDriverLicenseLikeAndIsGetAndTime(String phoneNumber,String driverLicense,Integer isGet, String time);

//    List<WhiteList> findByDriverLicenseAndPhoneNumberAndTypeNotAndTimeAndIsGet(String driverLicense, String phoneNumber, Integer type, String time,Integer isGet);

    WhiteList findByAccountIdAndTypeAndTime(String accountId, Integer type, String time);
}
