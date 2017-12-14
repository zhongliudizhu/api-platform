package com.winstar.coupon.service;

import com.winstar.coupon.entity.CouponTemplate;
import com.winstar.coupon.repository.CouponTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 名称： CouponTeplateService
 * 作者： sky
 * 日期： 2017-12-13 10:05
 * 描述：
 **/
@Service
public class CouponTemplateService {

    @Autowired
    CouponTemplateRepository couponTemplateRepository;
    /**
     * 查询优惠券模板详情
     *
     * @param id 优惠券模板ID
     * @return CouponTemplate
     */
    CouponTemplate findById(String id){
        return couponTemplateRepository.findOne(id);
    }
}
