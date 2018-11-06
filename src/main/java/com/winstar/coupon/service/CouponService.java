package com.winstar.coupon.service;

import com.alibaba.fastjson.JSONArray;
import com.winstar.coupon.entity.CouponTemplate;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.CouponTemplateRepository;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.exception.NotFoundException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
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

    @Autowired
    OilOrderRepository oilOrderRepository;

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
        coupon.setValidBeginAt(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
        coupon.setValidEndAt(DateUtil.StringToDate(format.format(calendar.getTime()) +"23:59:59"));
//        if (!ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && !ObjectUtils.isEmpty(couponTemplate
//                .getValidEndAt())) {
//            coupon.setValidBeginAt(couponTemplate.getValidBeginAt());
//            coupon.setValidEndAt(couponTemplate.getValidEndAt());
//        } else if (!ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && ObjectUtils.isEmpty(couponTemplate
//                .getValidEndAt())) {
//            coupon.setValidBeginAt(couponTemplate.getValidBeginAt());
//            if (couponTemplate.getDays() == 0) {
//                try {
//                    coupon.setValidEndAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat
//                            ("yyyy-MM-dd").format(couponTemplate.getValidBeginAt()) + " 23:59:59"));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                coupon.setValidEndAt(org.apache.commons.lang.time.DateUtils.addDays(couponTemplate.getValidBeginAt(),
//                        couponTemplate.getDays()));
//            }
//        } else if (ObjectUtils.isEmpty(couponTemplate.getValidBeginAt()) && ObjectUtils.isEmpty(couponTemplate
//                .getValidEndAt())) {
//            coupon.setValidBeginAt(new Date());
//            coupon.setValidEndAt(org.apache.commons.lang.time.DateUtils.addDays(coupon.getValidBeginAt(),
//                    couponTemplate.getDays()));
//        }
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
     * 前端发送优惠券
     *
     * @param accountId  用户ID
     * @param amount 金额
     * @param validEndAt    到期时间  不传则默认当月最后一天
     * @param useRule    使用规则 满多少使用 0.0
     * @return MyCoupon
     */
    @Transactional
    public MyCoupon sendCoupon_freedom(String accountId, String activityId,
                                @RequestParam(defaultValue = "5") Double amount,
                                 Date validEndAt,
                                @RequestParam(defaultValue = "0.0") Double useRule,
                                @RequestParam(defaultValue = "前端调用发券") String name,
                                @RequestParam(defaultValue = "前端调用发券") String description
                                ) {

        MyCoupon coupon = new MyCoupon();
        coupon.setActivityId(activityId);
        coupon.setCreatedAt(new Date());
        coupon.setAccountId(accountId);
//        coupon.setCouponTemplateId(couponTemplate.getId());
        coupon.setAmount(amount);
//        coupon.setDiscountRate(couponTemplate.getDiscountRate());
//        coupon.setLimitDiscountAmount(couponTemplate.getLimitDiscountAmount());
        coupon.setValidBeginAt(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
        Date date=DateUtil.StringToDate(format.format(calendar.getTime()) +"23:59:59");
        if(validEndAt!=null){
            coupon.setValidEndAt(validEndAt);
        }else{
            coupon.setValidEndAt(date);
        }
        coupon.setShowStatus(0);
        coupon.setStatus(0);
        coupon.setUseRule(useRule);
        coupon.setName(name);
        coupon.setDescription(description);
        MyCoupon myCoupon = myCouponRepository.save(coupon);
        logger.info("----前端调用发优惠券----结束: " + myCoupon.toString());
        return myCoupon;
    }

    /**
     * 建行前端发送优惠券
     *
     * @param accountId  用户ID
     * @param amount 金额
     * @param validEndAt    到期时间  不传则默认当月最后一天
     * @param useRule    使用规则 满多少使用 0.0
     * @return MyCoupon
     */
    @Transactional
    public MyCoupon cbcsendCoupon_freedom(String accountId, String activityId,
                                       @RequestParam(defaultValue = "5") Double amount,
                                       Date validEndAt,
                                       @RequestParam(defaultValue = "0.0") Double useRule,
                                       @RequestParam(defaultValue = "前端调用发券") String name,
                                       @RequestParam(defaultValue = "前端调用发券") String description
    ) {

        MyCoupon coupon = new MyCoupon();
        coupon.setActivityId(activityId);
        coupon.setCreatedAt(new Date());
        coupon.setAccountId(accountId);
        coupon.setAmount(amount);
        coupon.setValidBeginAt(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
        Date date=DateUtil.StringToDate(format.format(calendar.getTime()) +"23:59:59");
        if(validEndAt!=null){
            coupon.setValidEndAt(validEndAt);
        }else{
            coupon.setValidEndAt(date);
        }
        coupon.setShowStatus(1);
        coupon.setStatus(0);
        coupon.setUseRule(useRule);
        coupon.setName(name);
        coupon.setDescription(description);
        MyCoupon myCoupon = myCouponRepository.save(coupon);
        logger.info("----前端调用发优惠券----结束: " + myCoupon.toString());
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
            if(coupon.getStatus()==0){
                if (coupon.getValidEndAt() != null && coupon.getValidEndAt().getTime() < now.getTime()) {
                    coupon.setStatus(2);
                    logger.info("----处理过期券----MyCouponId: " + coupon.getId() + " 已过期");
                    myCouponRepository.save(coupon);
                }
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
        if(myCoupon==null) return null;
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
    public List<MyCoupon> findMyUsableCoupon(String accountId, Double money,Goods goods)throws NotFoundException {
//        List<MyCoupon> list=myCouponRepository.findByAccountIdAndStatusAndUseRuleLessThanEqual(accountId, 0, money);
//        for(MyCoupon coupon:list){
//            Date now = new Date();
//           if(coupon.getValidEndAt().getTime()<now.getTime()){
//               logger.info("优惠券："+coupon.getId()+" 过期");
//               list.remove(coupon);
//           }
//            if(coupon.getActivityId().equals("3")
//                    ||coupon.getActivityId().equals("101")
//                    ||coupon.getActivityId().equals("102")
//                    ||coupon.getActivityId().equals("103")
//                    ||coupon.getActivityId().equals("104")){
//                logger.info("优惠券：不能使用类型3、101、102、103、104的券");
//                list.remove(coupon);
//            }
//        }
//      List<MyCoupon> list = new LinkedList<>();
        List<MyCoupon> list = new LinkedList<>();
        List<String> types = new LinkedList<>();
        types.add(String.valueOf(ActivityIdEnum.ACTIVITY_ID_3.getActivity()));
        types.add(String.valueOf(ActivityIdEnum.ACTIVITY_ID_101.getActivity()));
        types.add(String.valueOf(ActivityIdEnum.ACTIVITY_ID_103.getActivity()));
        types.add(String.valueOf(ActivityIdEnum.ACTIVITY_ID_104.getActivity()));
        types.add(String.valueOf(ActivityIdEnum.ACTIVITY_ID_105.getActivity()));
        types.add(String.valueOf(ActivityIdEnum.ACTIVITY_ID_667.getActivity()));
        if(goods.getType()==ActivityIdEnum.ACTIVITY_ID_667.getActivity()){
            list =myCouponRepository.findFassionMyUsableCoupon(accountId,0,money, new Date(),goods.getType().toString());
            if(list.size()<=0){
                throw  new NotFoundException("data.is.null");
            }
            if(oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId)>0){
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getAmount()==30.0&&goods.getPrice()==200){
                        list.remove(i);
                    }
                }
            }
        }else {
            list = myCouponRepository.findMyUsableCoupon(accountId,0,money, new Date(),types);
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
        if (coupon == null || coupon.getStatus() == 2 ||coupon.getStatus() ==1) {
            logger.info(couponId + "的优惠券不存在或已失效");
            return null;
        }
        Date now = new Date();
        if (coupon.getValidEndAt() != null && coupon.getValidEndAt().getTime() < now.getTime()) {
            logger.info(couponId + "的优惠券已失效");
            return null;
        }
        if (coupon.getUseRule() > saledPrice) {
            logger.info(couponId + "的优惠券不能使用，该优惠券满" + coupon.getUseRule() + "元可使用，商品价格：" + saledPrice);
            return null;
        }
        List<OilOrder>  oilOrderSize = oilOrderRepository.findByCouponId(couponId);
        if(!ObjectUtils.isEmpty(oilOrderSize)){
            logger.info(couponId + "的优惠券已使用");
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
