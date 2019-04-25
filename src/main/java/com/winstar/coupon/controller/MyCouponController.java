package com.winstar.coupon.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.entity.OilStation;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.coupon.service.OilStationService;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.exception.*;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.service.AccountService;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import com.winstar.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    OilOrderRepository oilOrderRepository;
    @Autowired
    MyOilCouponRepository myOilCouponRepository;
    @Autowired
    OilStationService oilStationService;

    /**
     * 我的优惠券列表
     *
     * @param request    HttpServletRequest
     * @param status     状态 0 未使用 1 已使用 2 已失效
     * @param pageNumber 默认 1
     * @param pageSize   默认 5
     * @return List<MyCoupon>
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> query(
            HttpServletRequest request,
            Integer status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10000") Integer pageSize
    ) throws NotRuleException, NotFoundException {
        List<MyCoupon> list = new LinkedList<>();
        String accountId = accountService.getAccountId(request);
        couponService.checkExpired(accountId);
        if (StringUtils.isEmpty(accountId)) throw new NotRuleException("accountId");
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
                list.add(cb.notEqual(root.<Integer>get("activityId"), "105"));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
        if (page.getContent().size() == 0) throw new NotFoundException("mycoupon");
        page.getContent().stream().forEach(bean -> {
            if (30.0 == bean.getAmount() && oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId) > 0 && Integer.parseInt(bean.getActivityId()) == ActivityIdEnum.ACTIVITY_ID_667.getActivity()) {
                bean.setUseRule(300.0);
            }
            list.add(bean);
        });

        return list;
    }

    @RequestMapping(value = "/queryType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> query(
            HttpServletRequest request,
            Integer status,
            String activityId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10000") Integer pageSize
    ) throws NotRuleException, NotFoundException {
        List<MyCoupon> list = new LinkedList<>();
        String accountId = accountService.getAccountId(request);
        couponService.checkExpired(accountId);

        if (StringUtils.isEmpty(accountId)) throw new NotRuleException("accountId");
        if (StringUtils.isEmpty(status)) throw new NotRuleException("status");
        if (StringUtils.isEmpty(activityId)) throw new NotRuleException("activityId");

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
        page.getContent().stream().forEach(bean -> {
            if (30.0 == bean.getAmount() && oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId) > 0 && Integer.parseInt(bean.getActivityId()) == ActivityIdEnum.ACTIVITY_ID_667.getActivity()) {
                bean.setUseRule(300.0);
            }
            list.add(bean);
        });
        return list;
    }

    /**
     * 发券
     *
     * @param activityId 活动ID
     * @param goodsId    商品ID
     * @return MyCoupon
     */
    @RequestMapping(value = "/sendCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyCoupon sendCoupon(
            HttpServletRequest request,
            String activityId,
            String goodsId
    ) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        MyCoupon myCoupon = couponService.sendCoupon(accountId, activityId, goodsId);
        return myCoupon;
    }

    /**
     * 活动3发券 20元优惠券
     *
     * @return MyCoupon
     */
    @RequestMapping(value = "/giveCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyCoupon sendCoupon_freedom(@RequestParam String openId) {
        String accountId = accountService.findAccountIdByOpenid(openId);
        String couponName = "X1-" + WsdUtils.getRandomNumber(8);
        Date time = DateUtil.addInteger(new Date(), Calendar.MONTH, 1);
        MyCoupon myCoupon = couponService.sendCoupon_freedom(accountId, "3", 20.0, DateUtil.getInputDate("2018-06-30 23:59:59"), 100.0, couponName, "20元优惠券");
        return myCoupon;
    }

    /**
     * 查询当前商品我可用的优惠券
     *
     * @param request
     * @param goodsId 商品id
     * @return List<MyCoupon>
     * @throws MissingParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/findMyUsableCoupon", method = RequestMethod.GET, produces = MediaType
            .APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> findMyUsableCoupon(
            HttpServletRequest request,
            String goodsId
    ) throws MissingParameterException, NotRuleException, NotFoundException {
        if (StringUtils.isEmpty(goodsId)) throw new MissingParameterException("goodsId");
        Goods goods = goodsRepository.findOne(goodsId);
        if (goods == null) throw new NotFoundException("this goods is NotFound");
        Double money = goods.getPrice();
        String accountId = accountService.getAccountId(request);
        couponService.checkExpired(accountId);
        List<MyCoupon> list = couponService.findMyUsableCoupon(accountId, money, goods);
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
    ) throws MissingParameterException, NotFoundException {
        if (openId == null) throw new MissingParameterException("openId");
        if (validEndAt == null) throw new MissingParameterException("validEndAt");
        String accountId = accountService.findAccountIdByOpenid(openId);
        if (StringUtils.isEmpty(accountId)) throw new NotFoundException("openId");
        MyCoupon myCoupon = couponService.sendCoupon_freedom(accountId, activityId, amount, validEndAt, useRule, name, description);

        return myCoupon;
    }


    @GetMapping(value = "/info")
    public Result getInfo(@RequestParam String id) throws Exception {
        Map<String, String> map = new HashMap<>();
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (ObjectUtils.isEmpty(myOilCoupon)) {
            return Result.fail("data_null", "该券不存在");
        }
        if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(myOilCoupon.getCreateTime()).getTime() < 1556640000000L) {
            return Result.fail("time_error", "无法查看详情");
        }
        if ("0".equals(myOilCoupon.getUseState())) {
            return Result.fail("error", "该油券未使用");
        }
        map.put("usedTime", myOilCoupon.getUseDate());
        OilStation oilStation = oilStationService.getOilStation(myOilCoupon.getTId());
        if (!ObjectUtils.isEmpty(oilStation)) {
            map.put("usedLocation", oilStation.getName());
        } else {
            map.put("usedLocation", "陕西省");
        }
        StringBuilder panCode = new StringBuilder(AESUtil.decrypt(myOilCoupon.getPan(), AESUtil.dekey));
        map.put("panCode", panCode.replace(2, 16, "** **** **** **** ").toString());
        return Result.success(map);
    }


}
