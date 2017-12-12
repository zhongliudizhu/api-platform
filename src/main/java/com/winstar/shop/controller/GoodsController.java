package com.winstar.shop.controller;

import com.alibaba.fastjson.JSONArray;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.exception.*;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
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
 * 名称： GoodsController
 * 作者： sky
 * 日期： 2017-12-12 11:36
 * 描述： 商品控制器
 **/
@RestController
@RequestMapping("/api/v1/cbc/goods")
public class GoodsController {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ActivityRepository activityRepository;

    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Goods> query(
            String  activityId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize
    )throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {

        if(StringUtils.isEmpty(activityId)){
            throw new MissingParameterException("activityId");
        }

        Activity activity=activityRepository.findOne(activityId);
        if(activity.getStatus()==0){
            throw new NotFoundException("this activity is closed");
        }
        if(StringUtils.isEmpty(activity.getGoods())){
            throw new NotFoundException("this activity has no goods");
        }

        JSONArray array=JSONArray.parseArray(activity.getGoods());
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<array.size();i++){
            jsonArray.add( array.get(i).toString());
        }
//        List<Goods> list=goodsRepository.findByIdIn(jsonArray);
//        System.out.println(list.size());

        Sort sorts = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sorts);
        Page<Goods> page = goodsRepository.findAll(new Specification<Goods>() {
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                CriteriaBuilder.In<String> in = cb.in(root.get("id"));
                for(int i=0;i<array.size();i++){
                    in.value(array.getString(i));
                }
                cb.and(in);
                list.add(cb.equal(root.<Integer>get("status"), 1));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
        if(page.getContent().size()==0){
            throw new NotFoundException("goods");
        }
        return page.getContent();
    }

}
