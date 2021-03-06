package com.winstar.oil.service;

import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zl on 2018/3/14
 */
@Service
public class MyOilCouponService {

    @Autowired
    MyOilCouponRepository myOilCouponRepository;
    @Autowired
    OilOrderRepository oilOrderRepository;

//    public Page<MyOilCoupon> findUsedCoupon(String accountId, List<String> ids, Pageable pageable){
//        return myOilCouponRepository.findByAccountIdAndUseStateAndIdNotIn(accountId,"1",ids,pageable);
//    }

    public List<MyOilCoupon> findByOrderId(String orderId){
        return myOilCouponRepository.findByOrderIdOrderByUseStateAsc(orderId);
    }

    public MyOilCoupon findOne(String id){
        return myOilCouponRepository.findOne(id);
    }

    /**
     *  三天前---三个月内已使用的
     * @param accountId
     * @param endTime
     * @param ids
     * @param pageable
     * @return
     */
    public Page<MyOilCoupon> findUsedCoupon(String accountId,Date endTime, List<String> ids, Pageable
            pageable){
        Page<MyOilCoupon> page = myOilCouponRepository.findAll(new Specification<MyOilCoupon>() {
            public Predicate toPredicate(Root<MyOilCoupon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (!StringUtils.isEmpty(accountId)) {
                    list.add(cb.equal(root.<String>get("accountId"), accountId));
                }
//                if (startTime!=null) {
//                    list.add(cb.greaterThan(root.get("useDate"), startTime));
//                }
                if (endTime!=null) {
                    list.add(cb.lessThan(root.get("useDate"), endTime));
                }
                Predicate[] p = new Predicate[list.size()];
                if(ids.size()>0){
                    Predicate in=root.get("id").in(ids);
                    list.add(cb.not(in));
                }
                return cb.and(list.toArray(p));
            }
        }, pageable);
        return  page;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrderAndMyOilCoupon(List<MyOilCoupon> myOilCoupons, OilOrder oilOrder) {
        myOilCouponRepository.save(myOilCoupons);
        oilOrderRepository.save(oilOrder);

    }

}
