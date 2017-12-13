package com.winstar.coupon.service;

import com.winstar.coupon.entity.CouponTemplate;
import org.springframework.stereotype.Service;

/**
 * 名称： CouponTeplateService
 * 作者： sky
 * 日期： 2017-12-13 10:05
 * 描述：
 **/
@Service
public interface CouponTemplateService {

    /**
     * 查询优惠券模板详情
     *
     * @param id 优惠券模板ID
     * @return CouponTemplate
     */
    CouponTemplate findById(String id);
}
