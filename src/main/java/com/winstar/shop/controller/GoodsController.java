package com.winstar.shop.controller;

import com.alibaba.fastjson.JSONArray;
import com.winstar.exception.*;
import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.entity.PageViewLog;
import com.winstar.user.service.AccountService;
import com.winstar.user.service.OneMoneyCouponRecordService;
import com.winstar.user.utils.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    static final String GoodId = "8"; //0.01元抢购券
    static final Integer Status = 1;
    static final Integer HOUR_BEGIN = 7;
    static final Integer HOUR_END = 23;
    static final String SORT = "createTime";
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
            String activityId
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

        if(!b || !checkTime()){
            for(int i=0;i<array.size();i++){
                if(array.getString(i).toString().equals(GoodId)){
                    array.remove(i);
                }
            }
        }
        List<Goods> list=goodsRepository.findByStatusAndIdIn(Status,array);

        if (list.size() == 0)  throw new NotFoundException("goods");
        return list;

    }

    /**
     * 每日7:00-24:00可抢购
     * @return
     */
    public static boolean checkTime(){
        int hour = DateUtil.getHour(new Date());
        if(HOUR_BEGIN<=hour && hour<=HOUR_END){
            return  true;
        }
        return  false;
    }

}
