package com.winstar.obu.repository;

import com.winstar.obu.entity.ObuAccount;
import com.winstar.obu.entity.ObuConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * ObuConfigRepository
 *
 * @author: Big BB
 * @create 2018-07-05 16:59
 * @DESCRIPTION:
 **/
public interface ObuAccountRepository extends JpaRepository<ObuAccount, String>,JpaSpecificationExecutor<ObuAccount> {
    ObuAccount findByPhoneNumber(String phoneNumber);
}
