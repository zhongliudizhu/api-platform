package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.CbcBinDingWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface CbcBinDingWhitelistRepository extends JpaRepository<CbcBinDingWhitelist, String>, JpaSpecificationExecutor<CbcBinDingWhitelist> {
    List<CbcBinDingWhitelist> findByPhoneAndValidTimeAfter(String phone, Date date);
}
