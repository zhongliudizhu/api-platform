package com.winstar.couponActivity.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.coupon.util.MyCouponEnum;
import com.winstar.couponActivity.entity.*;
import com.winstar.couponActivity.repository.*;
import com.winstar.couponActivity.utils.*;
import com.winstar.couponActivity.vo.QueryLogParam;
import com.winstar.couponActivity.vo.SaleVehicleRecordParam;
import com.winstar.couponActivity.vo.VerifyResult;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;

import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.utils.WsdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

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
    private CouponService couponService;
    @Autowired
    CouponActivityRepository couponActivityRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    WhiteListRepository whiteListRepository;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Autowired
    CareJuanListRepository careJuanListRepository;
    @Autowired
    LoveCarRepository loveCarRepository;
    @Autowired
    JoinListRepository joinListRepository;
    @Autowired
    VehicleValueRepository vehicleValueRepository;
    @Autowired
    QueryLogRepository queryLogRepository;

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
        Account account = accountService.findById(accountId.toString());
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
     * 参加手心活动
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "joinActivity2",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public JoinList joinActivity2(HttpServletRequest request, String plateNumber) throws NotFoundException,NotRuleException{
        Object accountId = request.getAttribute("accountId");
        if(StringUtils.isEmpty(plateNumber)){
            throw new NotRuleException("joinActivity2.plateNumber");
        }
        JoinList joinList = joinListRepository.findByAccountId(accountId.toString());

        if(ObjectUtils.isEmpty(joinList)){
            joinList = new JoinList();
            joinList.setCreateTime(new Date());
            joinList.setIsVerify(ActivityIdEnum.ACTIVITY_VERIFY_0.getActivity());//0：为未领取千万加油补贴  1：已领取
        }

        if(!plateNumber.equals(joinList.getPlateNumber())){
            logger.info("ccb2:保存手心活动用户名单");
            joinList.setAccountId(accountId.toString());
            joinList.setPlateNumber(plateNumber);
            joinList = joinListRepository.save(joinList);
        }
        int count = joinListRepository.findByCreateTimeLessThanEqual(joinList.getCreateTime()).size();
        joinList.setNumber(10000 + count);
        return joinList;
    }

    /**
     * 参加手心活动
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "joinVerify",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public JoinList JoinVerify(HttpServletRequest request) throws NotFoundException,NotRuleException{
        Object accountId = request.getAttribute("accountId");

        JoinList joinList = joinListRepository.findByAccountId(accountId.toString());
        if(ObjectUtils.isEmpty(joinList)){
            throw new NotFoundException("CouponActivity.notJoin");
        }
        int count = joinListRepository.findByCreateTimeLessThanEqual(joinList.getCreateTime()).size();
        joinList.setNumber(10000 + count);
        return joinList;
    }

    /**
     * 查询二期活动
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Activity> findAll(HttpServletRequest request) throws NotFoundException{
        long startTime = new Date().getTime();
        Object accountId = request.getAttribute("accountId");
        logger.info("获取二期活动详情");
        List<Activity> list = getActivity(accountId);
        if(ObjectUtils.isEmpty(list)){
              throw new NotFoundException("couponActivity.notActivity");
        }
        long endTime = new Date().getTime();
        logger.info("获取二期活动详情耗时: "+ (endTime -startTime)+"ms");
        return list;
    }

    /**
     * 获取活动详情
     * @param accountId
     * @return
     */
    private List<Activity> getActivity(Object accountId){
        List<Integer> types = new LinkedList<>();
        types.add(ActivityIdEnum.ACTIVITY_ID_101.getActivity());
        types.add(ActivityIdEnum.ACTIVITY_ID_103.getActivity());
        types.add(ActivityIdEnum.ACTIVITY_ID_104.getActivity());
        List<Activity> list =  activityRepository.findByTypeIn(types);

        for (Activity activity : list) {
            activity.setIsGet(0);
            CouponActivity couponActivity = couponActivityRepository.findOne(String.valueOf(activity.getType()));
            Integer soldAmount = 0;
            if(activity.getType()==ActivityIdEnum.ACTIVITY_ID_103.getActivity()||
                    activity.getType()==ActivityIdEnum.ACTIVITY_ID_104.getActivity()){
//                soldAmount = myCouponRepository.findByActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(String.valueOf(activity.getType()),DateUtil.getInputDate("2018-03-29 00:00:01"), DateUtil.getInputDate("2018-06-30 23:59:59")).size();
                List<MyCoupon>  myCoupons = myCouponRepository.findByAccountIdAndActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(accountId, String.valueOf(activity.getType()),DateUtil.getInputDate("2018-03-29 00:00:01"), DateUtil.getInputDate("2018-06-30 23:59:59"));
//                if(Integer.parseInt(couponActivity.getSendRule()) <= soldAmount){
//                    activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_3.getActivity());//0 ：未领取 1：已领取 3:售罄
//                }
                if(!ObjectUtils.isEmpty(myCoupons)){
                   activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
                    if(myCoupons.get(0).getStatus() == MyCouponEnum.COUPON_NOT_USE_1.getStatus()){
                        activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_4.getActivity());
                    }
                }
            }else{
//                soldAmount =  myCouponRepository.findByActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(String.valueOf(activity.getType()),DateUtil.getMonthBegin(),DateUtil.getMonthEnd()).size();
                List<MyCoupon>  myCoupons = myCouponRepository.findByAccountIdAndActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(accountId, String.valueOf(activity.getType()),DateUtil.getMonthBegin(),DateUtil.getMonthEnd());
//                if(Integer.parseInt(couponActivity.getSendRule()) <= soldAmount){
//                    activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_3.getActivity());//0 ：未领取 1：已领取 3:售罄
//                }
                if(!ObjectUtils.isEmpty(myCoupons)){
                    activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity()); //0 ：未领取 1：已领取 3:售罄 4：已使用
                    if(myCoupons.get(0).getStatus() == MyCouponEnum.COUPON_NOT_USE_1.getStatus()){
                        activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_4.getActivity());
                    }
                    WhiteList whiteList = whiteListRepository.findByAccountIdAndTypeAndTime(accountId.toString(),ActivityIdEnum.ACTIVITY_ID_101.getActivity(),TimeUtil.getMonth());
                    if(ObjectUtils.isEmpty(whiteList)){
                        activity.setSign(ActivityIdEnum.ACTIVITY_sign_2.getActivity());
                    }else{
                        activity.setSign(whiteList.getSign());
                    }
                }
            }
            activity.setGoods("");//置空商品
        }
        return list;
    }

    /**
     * 建行二期活动发券
     * @param request
     * @param driverLicense
     * @param phoneNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/giveCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<VerifyResult>   giveCoupons(
            HttpServletRequest request, String driverLicense, String phoneNumber, String plateNumber
    ) throws NotRuleException, NotFoundException {
        long startTime = new Date().getTime();
        Object accountId = request.getAttribute("accountId");
        Account account = accountService.findById(accountId.toString());
        logger.info("openid:"+account.getOpenid()+"-----二期开始发券-----");

        if(StringUtils.isEmpty(driverLicense)){
            throw new NotRuleException("couponActivity.driverLicense");
        }
        if(StringUtils.isEmpty(phoneNumber)){
            throw new NotRuleException("couponActivity.phoneNumber");
        }
        //匹配车辆
        if(CouponActivityUtil.compareCars(plateNumber, driverLicense,restTemplate,getSixInOneCarsUrl,objectMapper)){
            logger.info("openid:"+account.getOpenid()+"-----二期开始发券[couponActivity.notMatchCars]");
            throw new NotRuleException("couponActivity.notMatchCars");
        }
        //白名单验证 依次验证
        List<WhiteList> whiteLists = whiteListRepository.findByPhoneNumberAndDriverLicenseLikeAndIsGetAndTime(phoneNumber,driverLicense.substring(10,18),0,TimeUtil.getMonth());
        if(ObjectUtils.isEmpty(whiteLists)){
            logger.info("openid:"+account.getOpenid()+"-----二期开始发券[couponActivity.notWhiteLists]");
            throw new NotRuleException("couponActivity.notWhiteLists");
        }else{
            updateJoinList(accountId);//更新参加状态
        }
        //识别纯储和纯信
        Integer sign = CouponActivityUtil.getSign(whiteLists);

        //领取资格&发卷  4个活动依次发送（101和102为互斥，并要进行打标）
        List<VerifyResult> list = new LinkedList<>();
        for (WhiteList whiteList:whiteLists) {
            logger.info("openid:"+account.getOpenid()+"-----二期开始发券[" + whiteList.getType() +"]");
            if(sign == 3 && whiteList.getType() == 102){
                whiteList.setIsGet(1);
                whiteListRepository.save(whiteList);
                continue;
            }else{
                if(whiteList.getType() == 103||whiteList.getType() == 104){
                    sign = 0;
                }
                List<VerifyResult> getCouponLists101 = getCouponLists(accountId, whiteList,sign);
                list.addAll(getCouponLists101);
            }
        }
        long endTime = new Date().getTime();
        logger.info("openid:"+account.getOpenid()+"-----发券完毕----- |"+ (endTime -startTime)+"ms");
        return list;
    }

    /**
     * 发券
     * @param accountId
     * @param whiteList
     * @return
     */
    private List<VerifyResult> getCouponLists(Object accountId,WhiteList whiteList, Integer sign ){
        List<VerifyResult> verifyResults = new LinkedList<>();
        //领取资格&发卷
        Integer activityId = 0;
        Integer isGet = 0;

        VerifyResult verifyResult = new VerifyResult();
        List<MyCoupon> coupons = null;
        //判断加油包是否被领取
        // 103、104  整个活动期间
        if (whiteList.getType() == ActivityIdEnum.ACTIVITY_ID_103.getActivity()||whiteList.getType() == ActivityIdEnum.ACTIVITY_ID_104.getActivity()){
            activityId = whiteList.getType();
            coupons = myCouponRepository.findByAccountIdAndActivityId(accountId, String.valueOf(whiteList.getType()));
       //101、102 每个月
        }else{
            activityId = ActivityIdEnum.ACTIVITY_ID_101.getActivity();
            coupons = myCouponRepository.findByAccountIdAndActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(accountId, String.valueOf(activityId), TimeUtil.getMonthStart(),TimeUtil.getMonthLast());
        }
        //如果用户没有领取券，则发放加油补贴
        if(ObjectUtils.isEmpty(coupons)){
            CouponActivity couponActivity = couponActivityRepository.findOne(String.valueOf(whiteList.getType()));
            String couponName = couponActivity.getDescription()+"-" + WsdUtils.getRandomNumber(8);
            int size = 0;//获取活动发券总量
            if(size <= Integer.parseInt(couponActivity.getSendRule())){
                logger.info("accountId:"+accountId+"|"+activityId+"-----发券-----");
                Date time = DateUtil.addInteger(new Date(), Calendar.MONTH,1);
                couponService.sendCoupon_freedom(
                        accountId.toString(),String.valueOf(activityId),couponActivity.getAmount(),time,couponActivity.getUseRule(), couponName, couponActivity.getName());
                //回填白名单  1、打标储蓄&信用 2、记录发送时间
                updateWhiteList(accountId,whiteList,sign);

                verifyResult.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());//领取成功
            }else{
                verifyResult.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_3.getActivity());//售罄
            }
        }else{
            verifyResult.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_2.getActivity());//重复领取
        }
        verifyResult.setAccountId(accountId.toString());
        verifyResult.setType(activityId);
        verifyResult.setSign(sign);
        //返回领取结果
        verifyResults.add(verifyResult);

        return verifyResults;
    }
    /**
     *  领取汽车养护券
     * @param request
     * @param name
     * @param phoneNumber
     * @param plateNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/giveCareCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CareJuanList  giveCareCoupons(
            HttpServletRequest request, String name, String phoneNumber, String plateNumber
    ) throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(phoneNumber)||StringUtils.isEmpty(plateNumber)){
            throw new NotRuleException("getCareCoupons.params");
        }

        CareJuanList  careJuanList = careJuanListRepository.findByAccountId(accountId.toString());
        if (!ObjectUtils.isEmpty(careJuanList)){
            throw new NotFoundException("getCareCoupons.isGet");
        }
        Integer careJuanListSzie = careJuanListRepository.findByCreatTime(DateUtil.getDayBegin(),DateUtil.getDayEnd()).size();
        if(careJuanListSzie >= 40){
            throw new NotRuleException("getCareCoupons.isOut");
        }
        careJuanList = new CareJuanList();
        careJuanList.setAccountId(accountId.toString());
        careJuanList.setName(name);
        careJuanList.setPhoneNumber(phoneNumber);
        careJuanList.setPlateNumber(plateNumber);
        careJuanList.setCreatTime(new Date());
        CareJuanList  getCareJuanList = careJuanListRepository.save(careJuanList);
        if (ObjectUtils.isEmpty(getCareJuanList)){
            throw new NotFoundException("getCareCoupons.error");
        }
        return getCareJuanList;
    }

    /**
     *  是否已经领取汽车养护券
     * @param request
     * @param name
     * @param phoneNumber
     * @param plateNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/getCareCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public boolean  getCareCoupons(
            HttpServletRequest request, String name, String phoneNumber, String plateNumber
    ) throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        boolean flag = false;
        CareJuanList  getCareJuanList = careJuanListRepository.findByAccountId(accountId.toString());
        if (!ObjectUtils.isEmpty(getCareJuanList)){
            flag = true;
        }
//        Integer careJuanListSzie = careJuanListRepository.findByCreatTime(DateUtil.getDayBegin(),DateUtil.getDayEnd()).size();
//        if(careJuanListSzie >= 40){
//            throw new NotRuleException("getCareCoupons.isOut");
//        }
        return flag;
    }

    /**
     *  领取爱车大礼包优惠券
     * @param request
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/giveLoveCarCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LoveCarLog  giveLoveCarCoupons(
            HttpServletRequest request
    ) throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        logger.info("accountId:"+accountId+"-----爱车10元优惠券-----");
        List<LoveCarLog>  loveCarLogs= loveCarRepository.findByAccountId(accountId.toString());
        if (!ObjectUtils.isEmpty(loveCarLogs)){
            throw new NotRuleException("getLoveCarCoupons.isGet");
        }

        LoveCarLog loveCarLog = new LoveCarLog();
        loveCarLog.setAccountId(accountId.toString());
        loveCarLog.setCreateTime(new Date());
        loveCarLog = loveCarRepository.save(loveCarLog);

        couponService.sendCoupon_freedom(accountId.toString(),"2",10.0,DateUtil.getInputDate("2018-06-30 23:59:59"),200.0, "人保滴滴：爱车大礼包10元优惠券", "人保滴滴：爱车大礼包10元优惠券");
        logger.info("accountId:"+accountId+"-----爱车10元优惠券，发送完毕-----");
        return loveCarLog;
    }

    /**
     *  是否已经领取爱车大礼包优惠券
     * @param request
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/getLoveCarCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public boolean  getCareCoupons(
            HttpServletRequest request
    ) throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        boolean flag = false;
        List<LoveCarLog>  loveCarLogs= loveCarRepository.findByAccountId(accountId.toString());
        if (!ObjectUtils.isEmpty(loveCarLogs)){
            flag = true;
        }
        return flag;
    }

    /**
     * 查询车辆价值
     * @param request
     * @param plateNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/vehicleValue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public VehicleValue  getVehicleValue(
            HttpServletRequest request, String plateNumber
    ) throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        if(StringUtils.isEmpty(plateNumber)){
          throw new NotRuleException("vehicleValue.plateNumber");
        }
        VehicleValue vehicleValue =  vehicleValueRepository.findByHphm(plateNumber);
        if(ObjectUtils.isEmpty(vehicleValue)){
            throw new NotFoundException("vehicleValue.not");
        }
        try {
            if (StringUtils.isEmpty(vehicleValue.getCl())){
                vehicleValue.setCl(TimeUtil.dayComparePrecise(TimeUtil.getStringToDate(vehicleValue.getCdrq()),new Date()));
                vehicleValueRepository.save(vehicleValue);
            }
        } catch (Exception e) {
            throw new NotRuleException("vehicleValue.getTime");
        }

        return vehicleValue;
    }

    /**
     * 访问记录
     * @param request
     * @param queryLogParam
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     */
    @RequestMapping(value = "queryLog", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryLog queryLog(HttpServletRequest request,@RequestBody QueryLogParam queryLogParam)throws NotFoundException, MissingParameterException {
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
    @Async
    private  void  updateJoinList(Object accountId){
        JoinList joinList = joinListRepository.findByAccountId(accountId.toString());
        if(!ObjectUtils.isEmpty(joinList)){
            joinList.setIsVerify(ActivityIdEnum.ACTIVITY_VERIFY_1.getActivity());
            joinListRepository.save(joinList);
        }
    }

    @Async
    private  void  updateWhiteList(Object accountId,WhiteList w,Integer sign){
        logger.info("accountId:"+accountId+"|"+sign+"-----打标-----");

        w.setSign(sign);
        w.setSendTime(TimeUtil.getCurrentDateTime(TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE));
        w.setAccountId(accountId.toString());
        w.setIsGet(1);
        whiteListRepository.save(w);
    }
}
