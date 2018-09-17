package com.winstar.couponActivity.controller;

import com.google.common.collect.Maps;
import com.winstar.carLifeMall.repository.CarLifeOrdersRepository;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.*;
import com.winstar.couponActivity.repository.*;
import com.winstar.couponActivity.service.InviteUserService;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.couponActivity.utils.UtilConstants;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * CouponActivityController
 *
 * @author: liuyw
 * @create： 2018-08-29 9:39
 * @DESCRIPTION: 裂变活动
 **/
@RestController
@RequestMapping("/api/v1/cbc/fissionActivities")
public class FissionActivityController {

    @Autowired
    public static final Logger logger = LoggerFactory.getLogger(FissionActivityController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;
    @Autowired
    CouponActivityRepository couponActivityRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Autowired
    CarLifeOrdersRepository carLifeOrdersRepository;
    @Autowired
    InviteTableLogRepository inviteTableLogRepository;
    @Autowired
    OilOrderRepository oilOrderRepository;
    @Autowired
    MileageConsumLogRepository mileageConsumLogRepository;
    @Autowired
    MileageObtainLogRepository mileageObtainLogRepository;
    @Autowired
    InviteUserService inviteUserService;
    @Autowired
    CouponActivityTypeRepository couponActivityTypeRepository;

    private Integer FissionType=667;

    /**
     * 用户邀请资格校验
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "validateInvite",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> validateInvite(HttpServletRequest request){
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        if(oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId)>0||carLifeOrdersRepository.countByAccountIdAndIsAvailable(accountId,0)>0){
            activityMap.put("ac_state","0");
            logger.info("用户有邀请资格！！！");
        }else{
            activityMap.put("ac_state","1");
            logger.info("用户无邀请资格！！！");
        }
        return activityMap;
    }
    /**
     * 活动是否开启校验
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "validateActivity",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> validateActivity(HttpServletRequest request)throws NotFoundException{
        Map<String,Object> activityMap = Maps.newHashMap();
        Activity activity=activityRepository.findByType(FissionType);
        if(ObjectUtils.isEmpty(activity)){
            throw new NotFoundException("query.is.null");
        }
        if(activity.getStatus()==1){
            activityMap.put("ac_state","0");
            logger.info("裂变活动开启！！！");
        }else{
            activityMap.put("ac_state","1");
            logger.info("裂变活动暂未开放！！！");
        }
        return activityMap;
    }
    /**
     * 用户领取优惠券校验
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "receiverValidate",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> receiverValidate(HttpServletRequest request)throws NotFoundException{
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        //1.判断用户是否在优驾行消费过,判断用户是否领取过优惠券,是否有20元或30元优惠券（未使用或已使用）
        if(oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(accountId)>0||carLifeOrdersRepository.countByAccountIdAndIsAvailable(accountId,0)>0||myCouponRepository.findByAccountIdAndActivityId(accountId,FissionType.toString()).size()>0){
            activityMap.put("ac_state","0");
            logger.info("用户不符合邀请规则！！！");
        }else{
            Account account=accountService.findById(accountId);
            //2.是否绑定交安卡
            if (StringUtils.isEmpty(account.getAuthInfoCard())){
                activityMap.put("ac_state","1");//未绑定交安卡
            }else{
                activityMap.put("ac_state","2");//绑定了交安卡
            }
        }
        return activityMap;
    }
    /**
     * 用户领取优惠券
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "receiverMycoupon",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> receiverMycoupon(HttpServletRequest request,String inviteUserId,String couponSum)throws NotFoundException,NotRuleException{
        if(StringUtils.isEmpty(inviteUserId)){
            throw new NotFoundException("param.is.null");
        }
        if(StringUtils.isEmpty(couponSum)){
            throw new NotFoundException("param.is.null");
        }
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        InviteTableLog inviteTableLog;
        List<InviteTableLog> inviteTableLogList=new ArrayList<InviteTableLog>();
        MyCoupon myCoupon;
        if (Double.parseDouble(couponSum)>30.0){
            throw new NotRuleException("data.is.illegal");
        }
        if(inviteTableLogRepository.findByAccountIdAndInviteType(inviteUserId,1).size()>50){
            activityMap.put("ac_state","0");
            logger.info("用户已达到邀请上限！！！");
        }else{
            if(myCouponRepository.findByAccountIdAndActivityId(accountId,FissionType.toString()).size()>0){
                activityMap.put("ac_state","1");
                logger.info("已领取过优惠券！！！");
            }else{
                Date validEndAt=TimeUtil.getNextMonth();
                myCoupon=couponService.sendCoupon_freedom(accountId,FissionType.toString(),Double.parseDouble(couponSum),validEndAt,300.0,couponSum+"元优惠券","裂变"+couponSum+"元优惠券");
                logger.info(accountId+"领取"+couponSum+"元优惠券"+myCoupon.getId());
                inviteTableLog=new InviteTableLog(UUID.randomUUID().toString(),accountId,0,inviteUserId,1,null,TimeUtil.getCurrentDateTime2(),1);
                inviteTableLogList.add(inviteTableLog);
                logger.info(inviteUserId+"邀请"+accountId+"领取优惠券！！");
                InviteTableLog inviteTableLogSecond=inviteTableLogRepository.findByInvitedUserAndInviteType(inviteUserId,1);
                if (!StringUtils.isEmpty(inviteTableLog)){
                    inviteTableLog=new InviteTableLog(UUID.randomUUID().toString(),inviteTableLogSecond.getAccountId(),1,inviteUserId,1,null,TimeUtil.getCurrentDateTime2(),1);
                    inviteTableLogList.add(inviteTableLog);
                    logger.info(inviteTableLog.getAccountId()+"间接邀请了"+accountId);
                }
                inviteTableLogRepository.save(inviteTableLogList);
                activityMap.put("myCoupon",myCoupon);
                activityMap.put("ac_state","2");
            }
        }
        return  activityMap;
    }

    /**
     * 用户里程查询
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "getMileage",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> getMileage(HttpServletRequest request,String type)throws NotRuleException,NotFoundException {
        if (StringUtils.isEmpty(type)){
           throw new NotFoundException("param.is.null");
        }
        Map<String,Object> activityMap = Maps.newHashMap();
        String obtainMileage;
        String accountId = request.getAttribute("accountId").toString();
        if (type.equals("100")||type.equals("101")){
            obtainMileage=mileageObtainLogRepository.sumConsumerMileage(accountId);
        }else {
            obtainMileage=mileageObtainLogRepository.sumConsumerMileageByType(accountId,Integer.parseInt(type));
        }
        String consumMileage=mileageConsumLogRepository.sumConsumerMileage(accountId);
        Double sumMileage=0.0;
        if(StringUtils.isEmpty(obtainMileage)){
            obtainMileage="0";
        }
        if(StringUtils.isEmpty(consumMileage)){
            consumMileage="0";
        }
        if(type.equals("100")){
            sumMileage=Double.parseDouble(obtainMileage)-Double.parseDouble(consumMileage);
            if(sumMileage<0){
                throw new NotRuleException("data.is.illegal");
            }
        }else{
            sumMileage=Double.parseDouble(obtainMileage);
        }
        activityMap.put("mileage_sum",sumMileage);
        return activityMap;
    }
    /**
     * 用户里程兑换
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "mileageExchange",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> mileageExchange(HttpServletRequest request,String couponNumber,String couponRule)throws NotFoundException,NotRuleException{
        if(StringUtils.isEmpty(couponNumber)){
            throw new NotFoundException("param.is.null");
        }
        if(StringUtils.isEmpty(couponRule)){
            throw new NotFoundException("param.is.null");
        }
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        String obtainMileage=mileageObtainLogRepository.sumConsumerMileage(accountId);
        String consumMileage=mileageConsumLogRepository.sumConsumerMileage(accountId);
        Double sumMileage=0.0;
        if(StringUtils.isEmpty(obtainMileage)){
            obtainMileage="0";
        }
        if(StringUtils.isEmpty(consumMileage)){
            consumMileage="0";
        }
        MileageConsumLog mileageConsumLog=new MileageConsumLog();
        sumMileage=Double.parseDouble(obtainMileage)-Double.parseDouble(consumMileage);
        if(sumMileage<0||sumMileage==null){
            throw new NotRuleException("param.is.illegal");
        }
        if(Double.parseDouble(couponNumber)>sumMileage){
            activityMap.put("ac_state","1");
            logger.info(accountId+"里程不足，不能兑换！！！");
        }else {
            Date validEndAt=TimeUtil.getNextMonth();
            MyCoupon myCoupon=couponService.sendCoupon_freedom(accountId,FissionType.toString(),Double.parseDouble(couponNumber),validEndAt,Double.parseDouble(couponRule),"里程兑换的优惠券","这是裂变的里程兑换的优惠券");
            logger.info(accountId+"兑换"+couponNumber+"元优惠券！！！");
            mileageConsumLog.setAccountId(accountId);
            mileageConsumLog.setCreateTime(TimeUtil.getCurrentDateTime2());
            mileageConsumLog.setMileageNum(Double.parseDouble(couponNumber));
            mileageConsumLog.setCouponType(FissionType);
            mileageConsumLog.setMycouponId(myCoupon.getId());
            mileageConsumLog.setState(1);
            mileageConsumLogRepository.save(mileageConsumLog);
            logger.info(accountId+"消费"+couponNumber+"里程！！！");
            activityMap.put("ac_state","0");
        }
        return activityMap;
    }
    /**
     * 用户邀请人数查询
     * @param request
     * @return
     */
    @RequestMapping(value = "getInviteByAccountId",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> getInviteByAccountId(HttpServletRequest request,String inviteType,String state)throws NotFoundException {
        if(StringUtils.isEmpty(inviteType)){
            throw new NotFoundException("param.is.null");
        }
        if(StringUtils.isEmpty(state)){
            throw new NotFoundException("param.is.null");
        }
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        Integer counts=inviteTableLogRepository.findByAccountIdAndInviteTypeAndInvtiteState(accountId,Integer.parseInt(inviteType),Integer.parseInt(state)).size();
        logger.info("获取邀请人数"+counts);
        activityMap.put("invite_sum",counts);
        return activityMap;
    }

    /**
     * 用户邀请里程列表
     * @param request
     * @return
     */
    @RequestMapping(value = "getInviteList",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<InviteTableList> getInviteList(HttpServletRequest request) throws NotFoundException {
        String accountId = request.getAttribute("accountId").toString();
        return inviteUserService.getInviteList(accountId);
    }
    /**
     * 用户安全任务校验
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "safeTaskValidate",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> safeTaskValidate(HttpServletRequest request){
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        List<MileageObtainLog> mileageLists=mileageObtainLogRepository.findByAccountIdAndOptainType(accountId,3);
        if (mileageLists.size()>0){
            activityMap.put("ac_state","1");
        }else {
            activityMap.put("ac_state","0");
        }
        return activityMap;
    }
    /**
     * 用户安全任务新增
     * @param request
     * @return
     */
    @RequestMapping(value = "safeTaskCallback",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> safeTaskCallback(HttpServletRequest request)throws NotRuleException{
        Map<String,Object> activityMap = Maps.newHashMap();
        String accountId = request.getAttribute("accountId").toString();
        List<MileageObtainLog> mileageLists=mileageObtainLogRepository.findByAccountIdAndOptainType(accountId,3);
        if (mileageLists.size()==0||StringUtils.isEmpty(mileageLists)){
            MileageObtainLog mileageObtainLog=new  MileageObtainLog();
            mileageObtainLog.setMileageNum(UtilConstants.FissionActivityConstants.SAFE_TASK_MILEAFE);
            mileageObtainLog.setOptainType(3);
            mileageObtainLog.setAccountId(accountId);
            mileageObtainLog.setState(1);
            mileageObtainLog.setCreateTime(TimeUtil.getCurrentDateTime2());
            mileageObtainLogRepository.save(mileageObtainLog);
            activityMap.put("ac_state","0");
        }else {
           throw new NotRuleException("data.is.illegal");
        }
        return activityMap;
    }

    /**
     * 裂变活动优惠券查询
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "obainFassionCoupon",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> obainFassionCoupon(HttpServletRequest request,String activityId)throws NotFoundException{
        if(StringUtils.isEmpty(activityId)){
            throw new NotFoundException("param.is.null");
        }
        Map<String,Object> activityMap = Maps.newHashMap();
        List<CouponActivityType>  couponList=couponActivityTypeRepository.findByActivityIdAndShowStatus(activityId,1);
        if(StringUtils.isEmpty(couponList)){
            activityMap.put("ac_state","0");
        }else {
            activityMap.put("ac_state","1");
            activityMap.put("couponList",couponList);
        }
        return activityMap;
    }
}
