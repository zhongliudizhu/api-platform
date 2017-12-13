package com.winstar.coupon.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.exception.*;
import com.winstar.user.service.AccountService;
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


    /**
     *  商品列表
     * @param request
     * @param status 状态 0 未使用 1 已使用 2 已失效
     * @param pageNumber 默认 1
     * @param pageSize  默认 5
     * @return
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
            @RequestParam(defaultValue = "0") Integer status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize
    )throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {

        String  accountId=accountService.getAccountId(request);
        couponService.checkExpired(accountId);
        if(StringUtils.isEmpty(accountId)){
            throw new NotRuleException("accountId");
        }
        Sort sorts = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sorts);
        Page<MyCoupon> page = myCouponRepository.findAll(new Specification<MyCoupon>() {
            public Predicate toPredicate(Root<MyCoupon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(accountId)) {
                    list.add(cb.equal(root.<String>get("accountId"), accountId));
                }
                if (status!=null) {
                    list.add(cb.equal(root.<Integer>get("status"), status));
                }
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
        if(page.getContent().size()==0){
            throw new NotFoundException("mycoupon");
        }

        return page.getContent();
    }

    /**
     * 发券
     * @param activityId
     * @param goodsId
     * @return MyCoupon
     */
    @RequestMapping(value = "/sendCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyCoupon sendCoupon(HttpServletRequest request,String activityId,String goodsId)throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {
        String  accountId=accountService.getAccountId(request);
        MyCoupon myCoupon=couponService.sendCoupon(accountId,activityId,goodsId);
        return myCoupon;
    }

    /**
     * 查询我可用的优惠券
     * @param request
     * @return
     * @throws MissingParameterException
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @RequestMapping(value = "/findMyUsableCoupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> findMyUsableCoupon(HttpServletRequest request)throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {
        String  accountId=accountService.getAccountId(request);
        couponService.checkExpired(accountId);
        List<MyCoupon> list=couponService.findMyUsableCoupon(accountId);
        if(list.size()==0){
            throw new NotFoundException("mycoupon");
        }
        return list;
    }

}
