package com.winstar.couponActivity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.winstar.carLifeMall.repository.CarLifeOrdersRepository;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.couponActivity.entity.NewUserActivity;
import com.winstar.couponActivity.entity.QueryLog;
import com.winstar.couponActivity.repository.*;
import com.winstar.couponActivity.utils.CouponActivityUtil;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.couponActivity.vo.QueryLogParam;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CouponActivityController
 *
 * @author: Big BB
 * @create： 2018-03-16 9:39
 * @DESCRIPTION: 建行二期
 **/
@RestController
@RequestMapping("/api/v1/cbc/couponActivities")
public class CouponActivityController {

    public static final Logger logger = LoggerFactory.getLogger(CouponActivityController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    CouponActivityRepository couponActivityRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    WhiteListRepository whiteListRepository;
    @Autowired
    MyCouponRepository myCouponRepository;

    @Autowired
    CarLifeOrdersRepository carLifeOrdersRepository;
    @Autowired
    QueryLogRepository queryLogRepository;

    @Autowired
    NewUserActivityRepository newUserActivityRepository;
    @Autowired
    OilOrderRepository oilOrderRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${info.getTokenInfoUrl}")
    private String getTokenInfoUrl;
    @Value("${info.getLocalCarsUrl}")
    private String getLocalCarsUrl;
    @Value("${info.getSixInOneCarsUrl}")
    private String getSixInOneCarsUrl;

    /**
     * 查看优驾行绑定车辆
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "findCars",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> findCars(HttpServletRequest request) throws NotFoundException{
        Object accountId = request.getAttribute("accountId");
        Account account = accountService.findOne(accountId.toString());
        logger.info("openid:"+account.getOpenid());
        String token =  CouponActivityUtil.reqAccount(account.getOpenid(),"1",restTemplate,getTokenInfoUrl,objectMapper);//获取优驾行Token
        if(StringUtils.isEmpty(token)){
            throw  new NotFoundException("couponActivity.notYJXToken");
        }
        logger.info("token:"+token);
        List<String> localCars = CouponActivityUtil.reqCars(token,restTemplate,getLocalCarsUrl,objectMapper);//获取添加车辆
        if (ObjectUtils.isEmpty(localCars)){
           throw  new NotFoundException("couponActivity.notLocalCars");
        }
        return localCars;
    }
    /**
     * 新用户专享活动(8.5折购买100元油券)
     * @param request
     * @return
     */
    @RequestMapping(value = "newAccountActivity",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> newAccountActivity(HttpServletRequest request) {
        String accountId = request.getAttribute("accountId").toString();
        Map<String,Object> activityMap = Maps.newHashMap();
        NewUserActivity user=new NewUserActivity();
        Date currentTime=TimeUtil.getCurrentDateTime2();
        Date validEndAt=TimeUtil.getSenverDay(TimeUtil.getCurrentDateTime());
        NewUserActivity users=newUserActivityRepository.findByAccountId(accountId);
        //判断用户是否参与新用户活动
        long orderCount=oilOrderRepository.countValidOrderByActivityIdAndAccountId("666",accountId);
        //判断当前用户在活动表中是否存在
        if(!StringUtils.isEmpty(users)){
            activityMap.put("ac_time",users.getValidEndAt());
            //判断当前用户活动是否过期
            if(TimeUtil.dayComparePrecise2(currentTime,users.getValidEndAt())){
                if(orderCount>0){
                    users.setAcStatus(2);
                    activityMap.put("ac_state","2");//已经参与过活动
                }else{
                    users.setAcStatus(0);
                    activityMap.put("ac_state","0");//已经参与了活动而且活动没有过期，没有参与
                }
            }else{
                //过期就更新状态
                users.setAcStatus(1);
                activityMap.put("ac_state","1");//您参与的活动已过期
            }
            newUserActivityRepository.save(users);
        }else {
            if(oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId)>0||carLifeOrdersRepository.countByAccountIdAndIsAvailable(accountId,0)>0){
                activityMap.put("ac_state","3");//用户没有资格
            }else{
                user.setAccountId(accountId);
                user.setCreatedAt(currentTime);//创建时间：当前时间
                user.setValidEndAt(validEndAt);//结束时间：7天后的24点
                user.setAcStatus(0);//活动状态正常
                newUserActivityRepository.save(user);
                activityMap.put("ac_time",validEndAt);
                activityMap.put("ac_state","0");//参与活动成功，弹窗成功
            }
        }
        return  activityMap;
    }

    /**
     * 新用户活动校验接口
     * @param request
     * @return
     */
    @RequestMapping(value = "newAccountActivityValidate",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> newAccountActivityValidate(HttpServletRequest request) {
        String accountId = request.getAttribute("accountId").toString();
        Map<String,Object> activityMap = Maps.newHashMap();
        Date currentTime=TimeUtil.getCurrentDateTime2();
        Date validEndAt=TimeUtil.getSenverDay(TimeUtil.getCurrentDateTime());
        NewUserActivity users=newUserActivityRepository.findByAccountId(accountId);
        //判断用户是否参与新用户活动
        long orderCount=oilOrderRepository.countValidOrderByActivityIdAndAccountId("666",accountId);
        if(!StringUtils.isEmpty(users)) {
            activityMap.put("ac_time",users.getValidEndAt());
            //判断当前用户活动是否过期或失效
            if (TimeUtil.dayComparePrecise2(currentTime,users.getValidEndAt())) {
                if(orderCount>0){
                    activityMap.put("validate_state", "2");
                }else {
                    activityMap.put("validate_state", "0");
                }
            } else {
                activityMap.put("validate_state", "1");
            }
        }else {
            if(oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId)>0||carLifeOrdersRepository.countByAccountIdAndIsAvailable(accountId,0)>0){
                activityMap.put("ac_state","3");//用户没有资格
            }else{
                activityMap.put("ac_time",validEndAt);
                activityMap.put("ac_state","0");//参与活动成功，弹窗成功
            }
        }
        return activityMap;
    }


    /**
     * 访问记录
     * @param request
     * @param queryLogParam
     * @return
     * @throws MissingParameterException
     */
    @RequestMapping(value = "queryLog", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryLog queryLog(HttpServletRequest request,@RequestBody QueryLogParam queryLogParam)throws MissingParameterException {
        Object accountId = request.getAttribute("accountId");

        if (ObjectUtils.isEmpty(queryLogParam)) {
            throw new MissingParameterException("QueryLogParam.queryLogParam");
        }
        if (ObjectUtils.isEmpty(queryLogParam.getApplyUrl())) {
            throw new MissingParameterException("QueryLogParam.getApplyUrl");
        }
        if (StringUtils.isEmpty(queryLogParam.getCode())) {
            throw new MissingParameterException("QueryLogParam.getApplyUrl");
        }

        QueryLog queryLog = new QueryLog();
        queryLog.setApplyUrl(queryLogParam.getApplyUrl());
        queryLog.setCode(queryLogParam.getCode());
        queryLog.setCreatedAt(new Date());
        queryLogRepository.save(queryLog);
        return queryLog;
    }
}
