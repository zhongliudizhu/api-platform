package com.winstar.oil.service;

import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zl on 2018/3/14
 */
@Service
public class MyOilCouponService {

    @Autowired
    MyOilCouponRepository myOilCouponRepository;

    public Page<MyOilCoupon> findUsedCoupon(String accountId, List<String> pans, Pageable pageable){
        return myOilCouponRepository.findByAccountIdAndUseStateAndPanNotIn(accountId,"1",pans,pageable);
    }

}
