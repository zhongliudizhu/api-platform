package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.SevenWhiteList;
import com.winstar.couponActivity.entity.SixWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by qyc on 2018/10/25.
 */
public interface SevenWhiteListRepository extends JpaRepository<SevenWhiteList,String>,JpaSpecificationExecutor<SevenWhiteList> {
    @Query(value = "select * from cbc_seven_white_list where driver_license like ?1 and phone_number=?2",nativeQuery = true)
    SevenWhiteList findByDriverLicenseAndPhoneNumber(String driverLicense, String phone);
    @Query(value="select * from cbc_seven_white_list where phone_number = ?1 and is_get =?2 and type = 107",nativeQuery = true)
    SevenWhiteList checkWhiteLists(String phoneNumber,Integer isGet);
    @Query(value="select * from cbc_seven_white_list where phone_number = ?1 and type = 107",nativeQuery = true)
    SevenWhiteList checkSevenIfWhiteLists(String phoneNumber);
}
