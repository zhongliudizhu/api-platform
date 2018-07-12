package com.winstar.obu.service;

import com.winstar.obu.entity.ObuAccount;
import com.winstar.obu.entity.ObuToken;
import com.winstar.obu.repository.ObuTokenRepository;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * ObuTokenService
 *
 * @author: Big BB
 * @create 2018-07-11 17:35
 * @DESCRIPTION:
 **/
@Service
public class ObuTokenService {
    @Autowired
    ObuTokenRepository obuTokenRepository;

    public ObuToken getToken(String phoneNumber){
        ObuToken obuToken = obuTokenRepository.findByPhoneNumberAndUpdateTimeGreaterThan(phoneNumber, new Date());
        return obuToken;
    }

    public ObuToken createObuToken(ObuToken obuToken) {
        obuToken.setCreateTime(new Date());
        obuToken.setUpdateTime(DateUtil.getTime30());
        obuToken = obuTokenRepository.save(obuToken);
        return obuToken;
    }

    public ObuToken findByTokenId(String tokenId){
        ObuToken obuToken = obuTokenRepository.findByTokenIdAndUpdateTimeGreaterThan(tokenId,new Date());
        return obuToken;
    }

    public ObuToken findByPhoneNumber(String phoneNumber){
        ObuToken obuToken = obuTokenRepository.findByPhoneNumber(phoneNumber);
        return obuToken;
    }
}
