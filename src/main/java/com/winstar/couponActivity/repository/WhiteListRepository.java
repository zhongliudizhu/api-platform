package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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

    List<WhiteList> findByDriverLicenseAndPhoneNumberAndTypeAndTimeAndIsGet(String driverLicense, String phoneNumber, Integer type, String time,Integer isGet);
    List<WhiteList> findByDriverLicenseAndPhoneNumberAndTypeNotAndTimeAndIsGet(String driverLicense, String phoneNumber, Integer type, String time,Integer isGet);
    WhiteList findByAccountIdAndTypeAndTime(String accountId, Integer type, String time);
}
