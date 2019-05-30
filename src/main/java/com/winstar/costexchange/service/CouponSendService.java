package com.winstar.costexchange.service;

import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.costexchange.entity.ExchangeRecord;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.costexchange.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zl on 2019/5/30
 */
@Service
public class CouponSendService {

    @Autowired
    ExchangeRepository exchangeRepository;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Transactional
    public void sendCoupon(List<AccountCoupon> accountCoupons, ExchangeRecord exchangeRecord){
        accountCouponRepository.save(accountCoupons);
        exchangeRepository.save(exchangeRecord);
    }

}
