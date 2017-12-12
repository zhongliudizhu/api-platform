package com.winstar.coupon.service.impl;

import com.winstar.coupon.entity.CouponTemplate;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.CouponTemplateRepository;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 名称： CouponServiceImpl
 * 作者： sky
 * 日期： 2017-12-12 10:47
 * 描述：
 **/

@Service
public class CouponServiceImpl implements CouponService {

    private static Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    CouponTemplateRepository couponTemplateRepository;

    @Autowired
    MyCouponRepository myCouponRepository;
    /**
     *  发券
     * @param accountId 用户id

     * @param couponTemplateId   优惠券模板ID
     */

    @Override
    @Transactional
    public MyCoupon sendCoupon(String accountId ,String couponTemplateId) {
        logger.info("----开始发放优惠券----accountId: "+accountId);
        CouponTemplate couponTemplate=couponTemplateRepository.findOne(couponTemplateId);
        MyCoupon coupon = new MyCoupon();
        coupon.setCreatedAt(new Date());
        coupon.setAccountId(accountId);
        coupon.setCouponTemplateId(couponTemplate.getId());
        coupon.setAmount(couponTemplate.getAmount());
        coupon.setDiscountRate(couponTemplate.getDiscountRate());
        coupon.setLimitDiscountAmount(couponTemplate.getLimitDiscountAmount());
        if(!ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && !ObjectUtils.isEmpty(couponTemplate.getValidEndAt())){
            coupon.setValidBeginAt(couponTemplate.getValidBeginAt());
            coupon.setValidEndAt(couponTemplate.getValidEndAt());
        }else if(!ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && ObjectUtils.isEmpty(couponTemplate.getValidEndAt())){
            coupon.setValidBeginAt(couponTemplate.getValidBeginAt());
            if(couponTemplate.getDays() == 0){
                try {
                    coupon.setValidEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").format(couponTemplate.getValidBeginAt()) + " 23:59:59"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                coupon.setValidEndAt(org.apache.commons.lang.time.DateUtils.addDays(couponTemplate.getValidBeginAt(),couponTemplate.getDays()));
            }
        }else if(ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && ObjectUtils.isEmpty(couponTemplate.getValidEndAt())){
            coupon.setValidBeginAt(new Date());
            coupon.setValidEndAt(org.apache.commons.lang.time.DateUtils.addDays(coupon.getValidBeginAt(),couponTemplate.getDays()));
        }
        coupon.setShowStatus(couponTemplate.getShowStatus());
        coupon.setStatus(0);
        coupon.setUseRule(ObjectUtils.isEmpty(couponTemplate) ? null : couponTemplate.getRules());
        coupon.setName(couponTemplate.getName());
        coupon.setDescription(couponTemplate.getDescription());
        MyCoupon myCoupon= myCouponRepository.save(coupon);
        logger.info("----发放优惠券----结束: "+myCoupon.toString());
        return myCoupon;
    }
}
