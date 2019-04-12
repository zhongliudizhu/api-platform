package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.NineWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface NineWhiteListRepository extends JpaRepository<NineWhiteList,String>, JpaSpecificationExecutor<NineWhiteList> {
    //身份证后6位跟电话号码 查询交安卡卡号
    @Query(value = "select card_number from cbc_nine_white_list where SUBSTR(driver_license,-6)= ?1 and phone_number=?2",nativeQuery = true)
    String findByDriverLicenseAndPhoneNumber(String driverLicense, String phone);
    //根据用户ID查询验证时间是否过期
    @Query(value = "select * from cbc_nine_white_list where account_id= ?1 and send_time>=DATE_SUB(NOW(),INTERVAL 15 DAY)",nativeQuery = true)
    NineWhiteList findByAccountIdAndSendTime(String accountId);
    NineWhiteList findByPhoneNumberAndIsGetAndType(String phoneNumber, Integer isGet, Integer type);

    NineWhiteList findByAccountId(String accountId);
}
