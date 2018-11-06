package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.SixWhiteList;
import com.winstar.couponActivity.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by qyc on 2018/10/25.
 */
public interface SixWhiteListRepository extends JpaRepository<SixWhiteList,String>,JpaSpecificationExecutor<SixWhiteList> {
    @Query(value = "select * from cbc_six_white_list where driver_license like ?1 and phone_number=?2",nativeQuery = true)
    SixWhiteList findByDriverLicenseAndPhoneNumber(String driverLicense, String phone);
    @Query(value="select * from cbc_six_white_list where phone_number = ?1 and is_get =?2 and type = 106",nativeQuery = true)
    SixWhiteList checkWhiteLists(String phoneNumber,Integer isGet);
    @Query(value="select * from cbc_six_white_list where phone_number = ?1 and type = 106",nativeQuery = true)
    SixWhiteList checkSixIfWhiteLists(String phoneNumber);
}
