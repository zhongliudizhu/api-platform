package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.CbcWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CbcWhiteListRepository extends JpaRepository<CbcWhiteList, String>, JpaSpecificationExecutor<CbcWhiteList> {

    List<CbcWhiteList> findByPhoneAndActivityCode(String phone, String activityCode);

    List<CbcWhiteList> findByActivityCodeAndCardNumberEndingWith(String activityCode, String cardNumber);

    List<CbcWhiteList> findByActivityCodeAndPhoneAndCardNumberEndingWith(String activityCode, String phone, String cardNumber);

}
