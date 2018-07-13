package com.winstar.couponActivity.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.coupon.util.MyCouponEnum;
import com.winstar.couponActivity.entity.CouponActivity;
import com.winstar.couponActivity.entity.OilSubsidyVerifyLog;
import com.winstar.couponActivity.entity.WhiteList;
import com.winstar.couponActivity.repository.CouponActivityRepository;
import com.winstar.couponActivity.repository.OilSubsidyVerifyLogRepository;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.exception.InnerServerException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.obu.utils.SmsUtil;
import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.param.UpdateAccountParam;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * OilSubsidyController
 * 百万加油补贴--第三季度活动
 *
 * @author: Big BB
 * @create 2018-06-21 16:59
 * @DESCRIPTION:
 **/
@RestController
@RequestMapping("/api/v1/cbc/oilSubsidy")
public class OilSubsidyController {
    public static final Logger logger = LoggerFactory.getLogger(OilSubsidyController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    CouponActivityRepository couponActivityRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    WhiteListRepository whiteListRepository;
    @Autowired
    OilSubsidyVerifyLogRepository oilSubsidyVerifyLogRepository;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Autowired
    private CouponService couponService;

    @Value("${send_sms_url}")
    String sendSmsUrl;
    @Value("${verify_sms_url}")
    String verifySmsUrl;


    /**
     * 活动以及活动状态查询
     * @param request
     * @return
     */
    @RequestMapping(value = "find",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Activity getActivity(HttpServletRequest request) throws NotFoundException{
        Object accountId = request.getAttribute("accountId");
        Activity activity = new Activity();
        activity.setName("百万加油补贴--第三季度活动");
        activity.setType(105);

//        OilSubsidyVerifyLog oilSubsidyVerifyLog = oilSubsidyVerifyLogRepository.findByAccountId(accountId.toString());
//        if(!ObjectUtils.isEmpty(oilSubsidyVerifyLog)){
//           activity.setIsVerify(1); //0 :未验证  1：已验证
//        }else{
//            activity.setIsVerify(0);
//        }
        List<MyCoupon> myCoupons = myCouponRepository.findByAccountIdAndActivityIdAndStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(accountId, String.valueOf(105),0, DateUtil.getInputDate("2018-07-01 00:00:01"), DateUtil.getInputDate("2019-01-31 23:59:59"));//0: 未使用
        if(!ObjectUtils.isEmpty(myCoupons)){
            activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
        }else{
            activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_0.getActivity());
        }
        return  activity;
    }

    @RequestMapping(value = "sendSMS", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResponseEntity sendSMS(HttpServletRequest request, String phoneNumber) throws NotFoundException, NotRuleException{
        ResponseEntity resp = SmsUtil.sendSms(phoneNumber,sendSmsUrl);

        return resp;
    }

    @RequestMapping(value = "/giveCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Activity giveCoupons(HttpServletRequest request, String driverLicense, String phoneNumber, String msgVerifyCode, String msgVerifyId)
       throws NotRuleException, NotFoundException ,InnerServerException{
        Object accountId = request.getAttribute("accountId");
        Account account = accountService.findById(accountId.toString());
        logger.info("openid:"+account.getOpenid()+"-----百万加油补贴活动【发券】-----");

        if(StringUtils.isEmpty(driverLicense)){
            throw new NotRuleException("couponActivity.driverLicense");
        }
        if(StringUtils.isEmpty(phoneNumber)){
            throw new NotRuleException("couponActivity.phoneNumber");
        }
        if(StringUtils.isEmpty(msgVerifyCode)){
            throw new NotRuleException("couponActivity.msgVerifyCode");
        }
        if(StringUtils.isEmpty(msgVerifyId)){
            throw new NotRuleException("couponActivity.msgVerifyId");
        }

        UpdateAccountParam updateAccountParam =new UpdateAccountParam();
        updateAccountParam.setMobile(phoneNumber);
        updateAccountParam.setMsgVerifyCode(msgVerifyCode);
        updateAccountParam.setMsgVerifyId(msgVerifyId);

        if (!SmsUtil.verifySms(updateAccountParam,verifySmsUrl))
            throw new NotRuleException("msgInvalid.code");

        Activity activity = getActivityInfo();

        WhiteList whiteList = whiteListRepository.checkWhiteList(driverLicense, phoneNumber, 0);
        if(ObjectUtils.isEmpty(whiteList)){
            throw new NotFoundException("couponActivity.notWhiteLists");
        }
//        else{
//            activity.setIsVerify(1);//已验证
//            saveOilSubsidyVerifyLog(accountId.toString());//记录验证日志
//        }

        String nowMonth = TimeUtil.getMonth();
        try {
            if(whiteList.getTime().equals(nowMonth)||TimeUtil.getCheckTimeNextMonth(whiteList.getTime()).equals(nowMonth)){
                activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
                giveCouponInfo(accountId.toString(),whiteList);
            }else{
                throw new NotFoundException("couponActivity.notWhiteLists");
            }
        } catch (ParseException e) {
            throw new NotFoundException("couponActivity.notWhiteLists");
        }

        return activity;
    }
    /**
     * 异步保存验证日志
     * @param accountId
     */
    @Async
    public void saveOilSubsidyVerifyLog(String accountId ){
        OilSubsidyVerifyLog oilSubsidyVerifyLog = new OilSubsidyVerifyLog();
        oilSubsidyVerifyLog.setAccountId(accountId);
        oilSubsidyVerifyLog.setCreateTime(new Date());
        oilSubsidyVerifyLogRepository.save(oilSubsidyVerifyLog);
    }

    public Activity getActivityInfo(){
        Activity activity = new Activity();
        activity.setName("百万加油补贴--第三季度活动");
        activity.setType(ActivityIdEnum.ACTIVITY_ID_105.getActivity());
        activity.setIsVerify(0);
        activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_0.getActivity());
        return  activity;
    }

    /**
     * 异步发卷
     * @param accountId
     * @param whiteList
     */
    @Async
    public void giveCouponInfo(String accountId, WhiteList whiteList){
        List<MyCoupon> coupons = myCouponRepository.findByAccountIdAndActivityId(accountId, "105");
        if(ObjectUtils.isEmpty(coupons)){
            CouponActivity couponActivity = couponActivityRepository.findOne("105");

            String couponName = "C1"+"-" + WsdUtils.getRandomNumber(8);
            couponService.sendCoupon_freedom(
                    accountId.toString(),"105",couponActivity.getAmount(),DateUtil.getNextMonthEnd(),couponActivity.getUseRule(), couponName, couponActivity.getName());
            //回填白名单  2、记录发送时间
            logger.info("accountId:"+accountId+"|回填白名单");
            whiteList.setSendTime(TimeUtil.getCurrentDateTime(TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE));
            whiteList.setAccountId(accountId.toString());
            whiteList.setIsGet(1);
            whiteListRepository.save(whiteList);
        }
    }
}
