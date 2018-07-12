package com.winstar.obu.repository;

import com.winstar.obu.entity.ObuAccount;
import com.winstar.obu.entity.ObuToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

/**
 * ObuConfigRepository
 *
 * @author: Big BB
 * @create 2018-07-05 16:59
 * @DESCRIPTION:
 **/
public interface ObuTokenRepository extends JpaRepository<ObuToken, String>,JpaSpecificationExecutor<ObuToken> {
    ObuToken findByPhoneNumberAndUpdateTimeGreaterThan(String phoneNumber, Date updateTime);

    ObuToken findByTokenIdAndUpdateTimeGreaterThan(String tokenId, Date updateTime);

    ObuToken findByTokenId(String tokenId);

    ObuToken findByPhoneNumber(String phoneNumber);
}
