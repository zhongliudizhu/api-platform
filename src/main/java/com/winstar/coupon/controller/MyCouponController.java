package com.winstar.coupon.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.exception.*;
import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.service.AccountService;
import com.winstar.utils.WsdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 名称： MyCouponController
 * 作者： sky
 * 日期： 2017-12-12 10:55
 * 描述： 我的优惠券
 **/
@RestController
@RequestMapping("/api/v1/cbc/mycoupon")
public class MyCouponController {

    @Autowired
    MyCouponRepository myCouponRepository;

    @Autowired
    CouponService couponService;

    @Autowired
    AccountService accountService;

    @Autowired
    GoodsRepository goodsRepository;

    /**
     * 我的优惠券列表
     *
     * @param request    HttpServletRequest
     * @param status     状态 0 未使用 1 已使用 2 已失效
     * @param pageNumber 默认 1
     * @param pageSize   默认 5
     * @return List<MyCoupon>
     * @throws MissingParameterException
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> query(
            HttpServletRequest request,
            Integer status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10000") Integer pageSize
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {

        String accountId = accountService.getAccountId(request);
        couponService.checkExpired(accountId);
        if (StringUtils.isEmpty(accountId))  throw new NotRuleException("accountId");
        Sort sorts = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sorts);
        Page<MyCoupon> page = myCouponRepository.findAll(new Specification<MyCoupon>() {
            public Predicate toPredicate(Root<MyCoupon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(accountId)) {
                    list.add(cb.equal(root.<String>get("accountId"), accountId));
                }
                if (status != null) {
                    list.add(cb.equal(root.<Integer>get("status"), status));
                }
                list.add(cb.notEqual(root.<Integer>get("activityId"), "3"));
                list.add(cb.notEqual(root.<Integer>get("activityId"), "101"));
                list.add(cb.notEqual(root.<Integer>get("activityId"), "102"));
                list.add(cb.notEqual(root.<Integer>get("activityId"), "103"));
                list.add(cb.notEqual(root.<Integer>get("activityId"), "104"));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
        if (page.getContent().size() == 0) throw new NotFoundException("mycoupon");

        return page.getContent();
    }

    @RequestMapping(value = "/queryType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> query(
            HttpServletRequest request,
            Integer status,
            String activityId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10000") Integer pageSize
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {

        String accountId = accountService.getAccountId(request);
        couponService.checkExpired(accountId);

        if (StringUtils.isEmpty(accountId))  throw new NotRuleException("accountId");
        if (StringUtils.isEmpty(status))  throw new NotRuleException("status");
        if (StringUtils.isEmpty(activityId))  throw new NotRuleException("activityId");

        Sort sorts = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sorts);
        Page<MyCoupon> page = myCouponRepository.findAll(new Specification<MyCoupon>() {
            public Predicate toPredicate(Root<MyCoupon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                list.add(cb.equal(root.<String>get("accountId"), accountId));
                list.add(cb.equal(root.<Integer>get("status"), 0));
                list.add(cb.equal(root.<Integer>get("activityId"), activityId));
                Date now = new Date();
                list.add(cb.greaterThan(root.get("validEndAt"), now.getTime()));

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
        if (page.getContent().size() == 0) throw new NotFoundException("mycoupon");

        return page.getContent();
    }

    /**
     * 发券
     *
     * @param activityId 活动ID
     * @param goodsId 商品ID
     * @return MyCoupon
     */
    @RequestMapping(value = "/sendCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyCoupon sendCoupon(
            HttpServletRequest request,
            String activityId,
            String goodsId
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {
        String accountId = accountService.getAccountId(request);
        MyCoupon myCoupon = couponService.sendCoupon(accountId, activityId, goodsId);
        return myCoupon;
    }
    /**
     * 活动3发券 20元优惠券
     * @return MyCoupon
     */
    @RequestMapping(value = "/giveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyCoupon sendCoupon_freedom(@RequestParam String openId) throws  ServiceUnavailableException {
        String accountId = accountService.findAccountIdByOpenid(openId);
        String couponName = "X1-" + WsdUtils.getRandomNumber(8);
        Date time = DateUtil.addInteger(new Date(), Calendar.MONTH,1);
        MyCoupon myCoupon = couponService.sendCoupon_freedom(accountId,"3",20.0,DateUtil.getInputDate("2018-06-30 23:59:59"),100.0,couponName,"20元优惠券");
        return myCoupon;
    }
    /**
     * 查询当前商品我可用的优惠券
     *
     * @param request
     * @param goodsId   商品id
     * @return List<MyCoupon>
     * @throws MissingParameterException
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @RequestMapping(value = "/findMyUsableCoupon", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> findMyUsableCoupon(
            HttpServletRequest request,
            String goodsId
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException,
            ServiceUnavailableException {
        if (StringUtils.isEmpty(goodsId)) throw new MissingParameterException("goodsId");
        Goods goods = goodsRepository.findOne(goodsId);
        if (goods == null) throw new NotFoundException("this goods is NotFound");
        Double money = goods.getPrice();
        String accountId = accountService.getAccountId(request);
        couponService.checkExpired(accountId);
        List<MyCoupon> list = couponService.findMyUsableCoupon(accountId, money);
        if (list.size() == 0) throw new NotFoundException("mycoupon");
        return list;
    }

    /**
     * 发券
     *
     * @return MyCoupon
     */
    @RequestMapping(value = "/wechatSendCoupon", method = RequestMethod.POST, produces = MediaType
            .APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyCoupon wechatSendCoupon(
            HttpServletRequest request,
            String openId,
            @RequestParam(defaultValue = "5") Double amount,
            Date validEndAt, String activityId,
            @RequestParam(defaultValue = "0.0") Double useRule,
            @RequestParam(defaultValue = "前端调用发券") String name,
            @RequestParam(defaultValue = "前端调用发券") String description
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException,
            ServiceUnavailableException {
        if(openId==null) throw  new MissingParameterException("openId");
        if(validEndAt==null) throw  new MissingParameterException("validEndAt");
        String accountId = accountService.findAccountIdByOpenid(openId);
        if(StringUtils.isEmpty(accountId)) throw new NotFoundException("openId");
        MyCoupon myCoupon = couponService.sendCoupon_freedom(accountId, activityId, amount, validEndAt, useRule, name, description);

        return myCoupon;
    }
}
