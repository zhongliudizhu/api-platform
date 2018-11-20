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
    //身份证后6位跟电话号码 查询交安卡卡号
    @Query(value = "select card_number from cbc_eight_white_list where SUBSTR(driver_license,-6)= ?1 and phone_number=?2",nativeQuery = true)
    String findByDriverLicenseAndPhoneNumber(String driverLicense, String phone);
    //根据电话号码领取状态跟类型查询 是否领取优惠券
    EightWhiteList findByPhoneNumberAndIsGetAndType(String phoneNumber, Integer isGet,Integer type);
    //根据电话查询白名单是否存在该用户
    EightWhiteList findByPhoneNumberAndType(String phoneNumber,Integer type);
}