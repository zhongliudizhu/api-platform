package com.winstar.user.repository;

import com.winstar.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByOpenid(String openid);
}
