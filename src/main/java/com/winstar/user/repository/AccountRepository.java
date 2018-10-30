package com.winstar.user.repository;

import com.winstar.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByOpenid(String openid);
    long countByAuthInfoCard(String authInfoCard);

    List<Account> findByMobile(String phone);

}
