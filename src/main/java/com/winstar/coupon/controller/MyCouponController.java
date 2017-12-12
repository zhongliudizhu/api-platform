package com.winstar.coupon.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.exception.*;
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


    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyCoupon> query(
            String  accountId,
            @RequestParam(defaultValue = "0") Integer status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize
    )throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {

        if(StringUtils.isEmpty(accountId)){
            throw new MissingParameterException("accountId");
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

}
