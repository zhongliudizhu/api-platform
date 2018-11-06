package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.EightWhiteList;
import com.winstar.couponActivity.entity.SevenWhiteList;
import com.winstar.couponActivity.entity.SixWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by qyc on 2018/10/25.
 */
public interface EightWhiteListRepository extends JpaRepository<EightWhiteList,String>,JpaSpecificationExecutor<EightWhiteList> {
    @Query(value = "select * from cbc_eight_white_list where driver_license like ?1 and phone_number=?2",nativeQuery = true)
    EightWhiteList findByDriverLicenseAndPhoneNumber(String driverLicense, String phone);
    @Query(value="select * from cbc_eight_white_list where phone_number = ?1 and is_get =?2 and type = 108",nativeQuery = true)
    EightWhiteList checkWhiteLists(String phoneNumber, Integer isGet);
    @Query(value="select * from cbc_eight_white_list where phone_number = ?1 and type = 107",nativeQuery = true)
    EightWhiteList checkEightIfWhiteLists(String phoneNumber);
}
