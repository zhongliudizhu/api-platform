package com.winstar.user.service;

import com.winstar.user.entity.AccessToken;
import com.winstar.user.utils.ServiceManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 名称 AccessTokenService
 * 作者 dpw
 * 日期 2018/11/14 14:01
 * 项目 winstar-cbc-platform-api
 * 描述
 */
@Service
public class AccessTokenService {
    @Cacheable(value = "findAccessTokenByAccountIdCbc", keyGenerator="tkKeyGenerator")
   public AccessToken findByAccountId(String accountId){
        return ServiceManager.accessTokenRepository.findByAccountId(accountId);
    }

    @Cacheable(value = "findAccessTokenByTokenIdCbc", keyGenerator="tkKeyGenerator")
  public  AccessToken findByTokenId(String tokenId){
      return ServiceManager.accessTokenRepository.findByTokenId(tokenId);
  }
}
