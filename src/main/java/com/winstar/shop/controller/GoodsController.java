package com.winstar.shop.controller;

import com.alibaba.fastjson.JSONArray;
import com.winstar.exception.*;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.entity.PageViewLog;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.OneMoneyCouponRecordService;
import com.winstar.user.utils.ServiceManager;
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
import java.util.Date;
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
    @Autowired
    AccountService accountService;

    @Autowired
    OneMoneyCouponRecordService oneMoneyCouponRecordService;
    /**
     * 根据活动Id查询商品
     *
     * @param request    HttpServletRequest
     * @param activityId 活动ID
     * @param pageNumber 起始页 默认1
     * @param pageSize   页面数 默认10
     * @return
     * @throws MissingParameterException
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Goods> query(
            HttpServletRequest request,
            String activityId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException, ServiceUnavailableException {

        if (StringUtils.isEmpty(activityId)) throw new MissingParameterException("activityId");
        String accountId = accountService.getAccountId(request);
        PageViewLog log = new PageViewLog();
        log.setCreateTime(new Date());
        log.setAccountId(accountId);
        log.setActivityId(activityId);
        log.setUrl(request.getRequestURI());
        ServiceManager.pageViewLogService.savePageViewLog(log);
        Activity activity = activityRepository.findOne(activityId);
        if (activity.getStatus() == 0)  throw new NotFoundException("this activity is closed");
        if (StringUtils.isEmpty(activity.getGoods()))  throw new NotFoundException("this activity has no goods");

        JSONArray array = JSONArray.parseArray(activity.getGoods());

        Boolean b=oneMoneyCouponRecordService.checkBuyAuth(accountId);
        if(!b){
            for(int i=0;i<array.size();i++){
                if(array.getString(i).toString().equals("8")){
                    array.remove(i);
                }
            }
        }
        Sort sorts = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sorts);
        Page<Goods> page = goodsRepository.findAll(new Specification<Goods>() {
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                Predicate in = root.get("id").in(array);
                list.add(cb.equal(root.<Integer>get("status"), 1));
                list.add(in);

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
        if (page.getContent().size() == 0)  throw new NotFoundException("goods");
        return page.getContent();
    }

}
