package com.winstar.coupon.service;

import com.alibaba.fastjson.JSONArray;
import com.winstar.coupon.entity.CouponTemplate;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.CouponTemplateRepository;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

;

/**
 * 名称： CouponService
 * 作者： sky
 * 日期： 2017-12-12 10:44
 * 描述： 优惠券service
 **/
@Service
public class CouponService {
    private static Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    CouponTemplateRepository couponTemplateRepository;

    @Autowired
    MyCouponRepository myCouponRepository;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    ActivityRepository activityRepository;

    /**
     * 发送优惠券
     *
     * @param accountId  用户ID
     * @param activityId 活动ID
     * @param goodsId    商品Id
     * @return MyCoupon
     */
    @Transactional
    public MyCoupon sendCoupon(String accountId, String activityId, String goodsId) {
        logger.info("----开始发放优惠券----accountId: " + accountId);
        Goods goods = goodsRepository.findOne(goodsId);
        if (goods == null) {
            logger.info("----查询商品不存在----goodsId: " + goodsId);
            return null;
        }
        if (StringUtils.isEmpty(goods.getCouponTempletId())) {
            logger.info("----打折商品 不赠券----goodsId: " + goodsId);
            return null;
        }
        CouponTemplate couponTemplate = couponTemplateRepository.findOne(goods.getCouponTempletId());
        MyCoupon coupon = new MyCoupon();
        coupon.setActivityId(activityId);
        coupon.setCreatedAt(new Date());
        coupon.setAccountId(accountId);
        coupon.setCouponTemplateId(couponTemplate.getId());
        coupon.setAmount(couponTemplate.getAmount());
        coupon.setDiscountRate(couponTemplate.getDiscountRate());
        coupon.setLimitDiscountAmount(couponTemplate.getLimitDiscountAmount());
        if (!ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && !ObjectUtils.isEmpty(couponTemplate
                .getValidEndAt())) {
            coupon.setValidBeginAt(couponTemplate.getValidBeginAt());
            coupon.setValidEndAt(couponTemplate.getValidEndAt());
        } else if (!ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && ObjectUtils.isEmpty(couponTemplate
                .getValidEndAt())) {
            coupon.setValidBeginAt(couponTemplate.getValidBeginAt());
            if (couponTemplate.getDays() == 0) {
//                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                    coupon.setValidEndAt(DateUtil.StringToDate(format.format(calendar.getTime()) +"23:59:59"));
//                    coupon.setValidEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat
//                            ("yyyy-MM-dd").format(couponTemplate.getValidBeginAt()) + " 23:59:59"));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            } else {
                coupon.setValidEndAt(org.apache.commons.lang.time.DateUtils.addDays(couponTemplate.getValidBeginAt(),
                        couponTemplate.getDays()));
            }
        } else if (ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && ObjectUtils.isEmpty(couponTemplate
                .getValidEndAt())) {
            coupon.setValidBeginAt(new Date());
            coupon.setValidEndAt(org.apache.commons.lang.time.DateUtils.addDays(coupon.getValidBeginAt(),
                    couponTemplate.getDays()));
        }
        coupon.setShowStatus(couponTemplate.getShowStatus());
        coupon.setStatus(0);
        coupon.setUseRule(ObjectUtils.isEmpty(couponTemplate) ? null : couponTemplate.getRules());
        coupon.setName(couponTemplate.getName());
        coupon.setDescription(couponTemplate.getDescription());
        MyCoupon myCoupon = myCouponRepository.save(coupon);
        logger.info("----发放优惠券----结束: " + myCoupon.toString());
        return myCoupon;
    }

    /**
     * 检查过期
     *
     * @param accountId 用户Id
     */
    public void checkExpired(String accountId) {
        logger.info("----处理过期券----accountId: " + accountId);
        Date now = new Date();
        List<MyCoupon> list = myCouponRepository.findByAccountId(accountId);
        for (MyCoupon coupon : list) {
            if (coupon.getValidEndAt() != null && coupon.getValidEndAt().getTime() < now.getTime()) {
                coupon.setStatus(2);
                logger.info("----处理过期券----MyCouponId: " + coupon.getId() + " 已过期");
                myCouponRepository.save(coupon);
            }
        }
        logger.info("----处理过期券-结束---accountId: " + accountId);
    }

    /**
     * 查询我的优惠券
     *
     * @param accountId 用户ID
     * @return MyCoupon
     */
    public List<MyCoupon> findMyCoupon(String accountId) {
        return myCouponRepository.findByAccountId(accountId);
    }

    /**
     * 使用优惠券
     *
     * @param id
     * @return MyCoupon
     */
    public MyCoupon useCoupon(String id) {
        MyCoupon myCoupon = myCouponRepository.findOne(id);
        myCoupon.setUseDate(new Date());
        myCoupon.setStatus(1);
        MyCoupon coupon = myCouponRepository.save(myCoupon);
        return coupon;
    }

    /**
     * 撤销已用的优惠券
     *
     * @param id 优惠券Id
     * @return
     */
    public MyCoupon cancelMyCoupon(String id) {
        MyCoupon myCoupon = myCouponRepository.findOne(id);
        myCoupon.setUseDate(null);
        myCoupon.setStatus(0);
        MyCoupon coupon = myCouponRepository.save(myCoupon);
        return coupon;
    }

    /**
     * 查询当前金额可用的优惠券
     *
     * @param accountId 用户ID
     * @param money     金额
     * @return MyCoupon
     */
    public List<MyCoupon> findMyUsableCoupon(String accountId, Double money) {
        List<MyCoupon> list=myCouponRepository.findByAccountIdAndStatusAndUseRuleLessThanEqual(accountId, 0, money);
        for(MyCoupon coupon:list){
            Date now = new Date();
           if(coupon.getValidEndAt().getTime()<now.getTime()){
               logger.info("优惠券："+coupon.getId()+" 过期");
               list.remove(coupon);
           }
        }
        return list;
    }

    /**
     * 查询优惠券是否可用
     * 1、判断优惠券是否过期 2、检查优惠券使用范围是否满足当前商品
     *
     * @param saledPrice  商品价格
     * @param couponId 优惠券ID
     * @return 可用返回 MyCoupon  否返回null
     */
    public MyCoupon checkIfMyCouponAvailable(Double saledPrice, String couponId) {
        MyCoupon coupon = myCouponRepository.findOne(couponId);
        if (coupon == null || coupon.getStatus() == 2) {
            logger.info(couponId + "的优惠券不存在或已失效");
            return null;
        }
        Date now = new Date();
        if (coupon.getValidEndAt() != null && coupon.getValidEndAt().getTime() < now.getTime()) {
            logger.info(couponId + "的优惠券已失效");
            return null;
        }
        if (coupon.getUseRule() >= saledPrice) {
            logger.info(couponId + "的优惠券不能使用，该优惠券满" + coupon.getUseRule() + "元可使用，商品价格：" + saledPrice);
            return null;
        }
        return coupon;
    }

    /**
     * 验证活动是否能用优惠券
     *
     * @param activityId
     * @param goodId
     * @return true 可用， false 不可用
     */
    public boolean checkActivityAndGoods(String activityId, String goodId, String myCouponId) {
        Activity activity = activityRepository.findOne(activityId);
        if (activity.getType() == 1) {
            logger.info("活动一不能使用优惠券！");
            return false;
        }
        JSONArray array = JSONArray.parseArray(activity.getGoods());
        int k = 0;
        for (int i = 0; i < array.size(); i++) {
            if (array.getString(i).equals(goodId)) {
                k += 1;
            }
        }
        if (k == 0) {
            logger.info("此活动：" + activityId + " 不包含商品：" + goodId);
            return false;
        }
        Goods goods = goodsRepository.findOne(goodId);

        MyCoupon myCoupon = myCouponRepository.findOne(myCouponId);

        if (myCoupon.getValidEndAt().getTime() < new Date().getTime()) {
            logger.info("此优惠券已过期！" + myCouponId);
            return false;
        }

        if (myCoupon.getUseRule() < goods.getSaledPrice()) {
            logger.info("此优惠券使用金额当前商品不满足 ：" + goodId);
            return false;
        }

        return true;
    }

}
