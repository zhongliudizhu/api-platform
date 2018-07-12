package com.winstar.obu.service;

import com.winstar.obu.entity.ObuDot;
import com.winstar.obu.repository.ObuDotRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * ObuDotService
 *
 * @author: Big BB
 * @create 2018-07-02 11:13
 * @DESCRIPTION:
 **/
@Service
public class ObuDotService {

    @Autowired
    ObuDotRepository obuDotRepository;

//    @Cacheable(cacheNames = "OBU_DOTS", keyGenerator = "keyGenerator")
    @Cacheable(value = "obuDot",key = "'obudot_'")
    public List<ObuDot> findAll() {
        List<ObuDot> obuDots = obuDotRepository.findAll();
        return obuDots;
    }
}
