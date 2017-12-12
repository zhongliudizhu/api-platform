package com.winstar.user.repository;

import com.winstar.user.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
    AccessToken findByAccountId(String accountId);

    AccessToken findByTokenId(String tokenId);
}
