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

    CouponTemplate findById(String id);
}
