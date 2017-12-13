package com.winstar.coupon.service.impl;

import com.winstar.coupon.entity.CouponTemplate;
import com.winstar.coupon.repository.CouponTemplateRepository;
import com.winstar.coupon.service.CouponTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 名称： CouponTemplateServiceImpl
 * 作者： sky
 * 日期： 2017-12-13 10:06
 * 描述：
 **/
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    CouponTemplateRepository couponTemplateRepository;

    @Override
    public CouponTemplate findById(String id) {
        return couponTemplateRepository.findOne(id);
    }
}
