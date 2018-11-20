package com.winstar.couponActivity.controller;

import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.CouponActivity;
import com.winstar.couponActivity.entity.OilSubsidyVerifyLog;
import com.winstar.couponActivity.entity.SixWhiteList;
import com.winstar.couponActivity.entity.WhiteList;
import com.winstar.couponActivity.repository.CouponActivityRepository;
import com.winstar.couponActivity.repository.OilSubsidyVerifyLogRepository;
import com.winstar.couponActivity.repository.SixWhiteListRepository;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.exception.*;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.repository.ActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
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

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by qyc on 2018/10/19.
 */
@RestController
@RequestMapping("/api/v1/cbc/oilNewCard")
public class OilNewCardController {
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
    SixWhiteListRepository sixWhiteListRepository;
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
    public Activity getActivity(HttpServletRequest request) {
        Object accountId = request.getAttribute("accountId");
        Activity activity = new Activity();
        activity.setName("新办交安卡购--第四季度活动");
        activity.setType(106);

        List<MyCoupon> myCoupons = myCouponRepository.findByAccountIdAndActivityIdAndStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(accountId, String.valueOf(106),0, DateUtil.getInputDate("2018-07-01 00:00:01"), DateUtil.getInputDate("2018-12-31 23:59:59"));//0: 未使用
        if(!ObjectUtils.isEmpty(myCoupons)){
            activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
        }else{
            activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_0.getActivity());
        }
        return  activity;
    }


    @RequestMapping(value = "/giveCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Activity giveCoupons(HttpServletRequest request,
                                String driverLicense,
                                String phoneNumber,
                                String msgVerifyCode,
                                String msgVerifyId)
            throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        Account account = accountService.findOne(accountId.toString());
        logger.info("openid:"+account.getOpenid()+"-----办新卡8折【发券】-----");
        logger.info("driverLicense:"+driverLicense+"|phoneNumber:"+phoneNumber
                +"|msgVerifyCode:"+msgVerifyCode+"|msgVerifyId:"+msgVerifyId);

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
        String infoCard ="";
        //根据身份证跟电话号码查询交安卡卡号
        String sixWhiteList = sixWhiteListRepository.findByDriverLicenseAndPhoneNumber(driverLicense, phoneNumber);
        if(ObjectUtils.isEmpty(sixWhiteList)){
            throw new NotFoundException("couponActivity.notWhiteLists");
        }else {
            infoCard = sixWhiteList;
        }
        //设置短息
        MsgContent mc = new MsgContent();
        mc.setKh(infoCard);
        mc.setXh(msgVerifyId);
        mc.setYzm(msgVerifyCode);
        //调用验证验证码接口
        String msgParam = StringFormatUtils.bean2JsonStr(mc);
        AuthVerifyCodeMsgResult authVerifyCodeMsgResult = ServiceManager.smsService.authSms(msgParam);
        if (!authVerifyCodeMsgResult.getStatus().equals("success")) {
            logger.error(new StringBuilder("验证码验证失败,").append(authVerifyCodeMsgResult.getErrorMessage()).append("_")
                    .append(phoneNumber).append("_")
                    .append(msgVerifyCode).append("_").append(infoCard).toString());
            throw new NotRuleException("INVALID_VERIFY");
        }
        List<AuthVerifyCodeEntity> authVerifyCodeEntityList = authVerifyCodeMsgResult.getResults();
        //判读是否得到驾驶证号码
        if (StringUtils.isEmpty(authVerifyCodeEntityList.get(0).getSfzhm())) {
            logger.error(new StringBuilder("银行卡账号不正确请检查数据").append(phoneNumber).append("_").append(msgVerifyCode).append("_").append(infoCard).toString());
            throw new NotRuleException("CardNoOrMobile");
        }


        Activity activity = getActivityInfo();
        //判断该用户是否存在在105白名单
        WhiteList ifwhitList= whiteListRepository.checkIfWhiteList(phoneNumber);
        //判断是否领取105优惠券
        WhiteList whiteList = whiteListRepository.checkWhiteList(phoneNumber, 0);
        //判断是否领取106活动优惠券
        SixWhiteList whiteLists = sixWhiteListRepository.findByPhoneNumberAndIsGetAndType(phoneNumber,0,106);
        if(ObjectUtils.isEmpty(ifwhitList)) {
           if(ObjectUtils.isEmpty(whiteLists)){
               logger.info("电话号码:"+phoneNumber+"已认证过106活动");
               throw new NotFoundException("couponActivity.notWhiteLists");
           }
        }else {
            if (ObjectUtils.isEmpty(whiteLists) || ObjectUtils.isEmpty(whiteList)) {
                logger.info("电话号码:"+phoneNumber+"已认证过105活动或者106活动");
                throw new NotFoundException("couponActivity.notWhiteLists");
            }
        }
        String nowMonth = TimeUtil.getMonth();
        try {
            if(whiteLists.getTime().equals(nowMonth)||TimeUtil.getCheckTimeNextMonth(whiteLists.getTime()).equals(nowMonth)){
                activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
                giveCouponInfo(accountId.toString(),whiteLists);
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
        activity.setName("新办交安卡购--第四季度活动");
        activity.setType(ActivityIdEnum.ACTIVITY_ID_106.getActivity());
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
    public void giveCouponInfo(String accountId, SixWhiteList whiteList){
        List<MyCoupon> coupons = myCouponRepository.findByAccountIdAndActivityId(accountId, "106");
        if(ObjectUtils.isEmpty(coupons)){
            CouponActivity couponActivity = couponActivityRepository.findOne("106");

            String couponName = "C2"+"-" + WsdUtils.getRandomNumber(8);
            couponService.cbcsendCoupon_freedom(
                    accountId,"106",couponActivity.getAmount(),DateUtil.getNextMonthEnd(),couponActivity.getUseRule(), couponName, couponActivity.getName());
            //回填白名单  2、记录发送时间
            logger.info("accountId:"+accountId+"|回填白名单");
            whiteList.setSendTime(TimeUtil.getCurrentDateTime(TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE));
            whiteList.setAccountId(accountId);
            whiteList.setIsGet(1);
            sixWhiteListRepository.save(whiteList);
        }
    }
    /**
     * cbc发送验证码(建行短信服务)
     *
     * @param infoCard
     * @param phone
     * @return
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @PostMapping(value = "/cbcSixSendAuthMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendAuth(@RequestParam String driverLicense, @RequestParam String phone, String infoCard, HttpServletRequest request)
            throws NotRuleException {
        //106白名单
        String sixWhiteList = sixWhiteListRepository.findByDriverLicenseAndPhoneNumber(driverLicense, phone);
        if(ObjectUtils.isEmpty(sixWhiteList)){
            logger.info("身份证"+driverLicense+"电话号码"+phone+"用户不在106白名单");
            throw new NotRuleException("WhiteLists.notWhiteLists");
        }else {
            infoCard = sixWhiteList;
        }
        MsgContent mc = new MsgContent();
        mc.setKh(infoCard);
        if(!StringUtils.isEmpty(phone)){
            phone = phone.substring(7,11);
        }
        mc.setSjh(phone);
        String msgParam = StringFormatUtils.bean2JsonStr(mc);
        //发送短息
        SendVerifyCodeMsgResult sendVerifyCodeMsgResult = ServiceManager.smsService.sendSms(msgParam);
        //判断返回结果
        if (sendVerifyCodeMsgResult != null && sendVerifyCodeMsgResult.getStatus().equals("success")) {
            CCBAuthParam ccbAuthParam = new CCBAuthParam();
            List<SendVerifyCodeEntity> sendVerifyCodeEntityList = sendVerifyCodeMsgResult.getResults();
            ccbAuthParam.setXh(sendVerifyCodeEntityList.get(0).getXh());
            ccbAuthParam.setKh(sendVerifyCodeEntityList.get(0).getKh());
            logger.info(new StringBuilder("实名认证建行验证码发送：infoCard-->").append(infoCard).append("phone-->").append(phone).toString());
            return new ResponseEntity(ccbAuthParam, HttpStatus.OK);
        } else {
            logger.info(new StringBuilder("发送失败01：infoCard-->").append(infoCard).append("phone-->").append(phone).toString());
            logger.error(new StringBuilder("发送失败02,").append(sendVerifyCodeMsgResult.getErrorMessage()).append(infoCard).append("_").append(phone).toString());
            throw new NotRuleException(sendVerifyCodeMsgResult.getErrorMessage());
        }
    }
}
