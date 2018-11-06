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

    List<WhiteList> findByPhoneNumberAndDriverLicenseAndIsGetAndType(String phoneNumber,String driverLicense,Integer isGet,Integer type);

    @Query("select o from WhiteList o where o.phoneNumber = ?1 and o.driverLicense like CONCAT('%',?2) and o.isGet =?3 and o.time =?4 and o.type < 105")
    List<WhiteList> findByPhoneNumberAndDriverLicenseLikeAndIsGetAndTime(String phoneNumber,String driverLicense,Integer isGet, String time);

    @Query("select o from WhiteList o where o.phoneNumber = ?1 and o.driverLicense like CONCAT('%',?2) and o.isGet =?3 and  str_to_date(o.time, '%Y-%m-%d')<str_to_date('2018-12-31', '%Y-%m-%d') AND str_to_date(o.time, '%Y-%m-%d') > str_to_date('2018-08-31', '%Y-%m-%d') and o.type < 105")
    List<WhiteList> findByPhoneNumberAndDriverLicenseLikeAndIsGetAndTime2(String phoneNumber,String driverLicense,Integer isGet);

    @Query("select o from WhiteList o where o.phoneNumber = ?1 and o.isGet =?2 and o.type = 105")
    WhiteList checkWhiteList(String phoneNumber,Integer isGet);
    @Query("select o from WhiteList o where o.phoneNumber = ?1 and o.type = 105")
    WhiteList checkIfWhiteList(String phoneNumber);


    //List<WhiteList> findByDriverLicenseAndPhoneNumberAndTypeNotAndTimeAndIsGet(String driverLicense, String phoneNumber, Integer type, String time,Integer isGet);
    @Query(value = "select * from cbc_white_list where driver_license like ?1 and phone_number=?2",nativeQuery = true)
    WhiteList findByDriverLicenseAndPhoneNumber(String driverLicense,String phone);

    WhiteList findByAccountIdAndTypeAndTime(String accountId, Integer type, String time);
}
