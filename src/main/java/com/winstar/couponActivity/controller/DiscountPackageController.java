package com.winstar.couponActivity.controller;

import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.NineWhiteList;
import com.winstar.couponActivity.repository.CouponActivityRepository;
import com.winstar.couponActivity.repository.NineWhiteListRepository;
import com.winstar.couponActivity.repository.OilSubsidyVerifyLogRepository;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by qyc on 2018/10/25.
 */
@RestController
@RequestMapping("/api/v1/cbc/discountPackage")
public class DiscountPackageController {
    public static final Logger logger = LoggerFactory.getLogger(OilSubsidyController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    CouponActivityRepository couponActivityRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    NineWhiteListRepository nineWhiteListRepository;
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
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "nineFind", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Activity getActivity(HttpServletRequest request) throws ParseException {
        Object accountId = request.getAttribute("accountId");
        Activity activity = new Activity();
        activity.setName("建行二期活动--优惠加油包");
        activity.setType(109);
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        //查询该用户是否验证过109
        NineWhiteList list = nineWhiteListRepository.findByAccountId(accountId.toString());
        //109白名单验证时间是否超过15天
        NineWhiteList nineWhiteList = nineWhiteListRepository.findByAccountIdAndSendTime(accountId.toString());
        if (!ObjectUtils.isEmpty(list)) {
            if (!ObjectUtils.isEmpty(nineWhiteList)) {
                if (list.getIsGet() == 1) {
                    activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
                    Date date = format.parse(nineWhiteList.getSendTime());
                    activity.setSendTime(date);
                }
            }else {
                activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_0.getActivity());
            }
        } else {
            activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_0.getActivity());
        }
        return activity;
    }

    @RequestMapping(value = "/nineValidationList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public NineWhiteList validationList(HttpServletRequest request,
                                        String driverLicense,
                                        String phoneNumber,
                                        String msgVerifyCode,
                                        String msgVerifyId)
            throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        Account account = accountService.findOne(accountId.toString());
        logger.info("openid:" + account.getOpenid() + "-----建行二期活动优惠包-----");
        logger.info("身份证后6位:" + driverLicense + "|电话号码:" + phoneNumber
                + "|验证码:" + msgVerifyCode + "|验证编号:" + msgVerifyId);

        if (StringUtils.isEmpty(driverLicense)) {
            logger.info("身份证号后6位:" + driverLicense + "为空");
            throw new NotRuleException("couponActivity.driverLicense");
        }
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new NotRuleException("couponActivity.phoneNumber");
        }
        if (StringUtils.isEmpty(msgVerifyCode)) {
            throw new NotRuleException("couponActivity.msgVerifyCode");
        }
        if (StringUtils.isEmpty(msgVerifyId)) {
            throw new NotRuleException("couponActivity.msgVerifyId");
        }
        String infoCard = "";

        //根据身份证跟电话号码查询交安卡卡号
        String nineWhiteList = nineWhiteListRepository.findByDriverLicenseAndPhoneNumber(driverLicense, phoneNumber);
        if (ObjectUtils.isEmpty(nineWhiteList)) {
            throw new NotFoundException("nineWhiteLists.notWhiteLists");
        } else {
            infoCard = nineWhiteList;
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


//        Activity activity = getActivityInfo();
        //判断是否领取109优惠券
        NineWhiteList whiteList = nineWhiteListRepository.findByPhoneNumberAndIsGetAndType(phoneNumber, 0, 109);
        if (ObjectUtils.isEmpty(whiteList)) {
            logger.info("电话号码:" + phoneNumber + "已认证过109活动");
            throw new NotFoundException("couponActivity.notWhiteLists");
        } else {
            whiteList.setSendTime(TimeUtil.getCurrentDateTime(TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE));
            whiteList.setAccountId(accountId.toString());
            whiteList.setIsGet(1);
            nineWhiteListRepository.save(whiteList);
        }
//        String nowMonth = TimeUtil.getMonth();
//        try {
//            if(whiteList.getTime().equals(nowMonth)||TimeUtil.getCheckTimeNextMonth(whiteList.getTime()).equals(nowMonth)){
//                activity.setIsGet(ActivityIdEnum.ACTIVITY_STATUS_1.getActivity());
//                giveCouponInfo(accountId.toString(),whiteList);
//            }else{
//                throw new NotFoundException("couponActivity.notWhiteLists");
//            }
//        } catch (ParseException e) {
//            throw new NotFoundException("couponActivity.notWhiteLists");
//        }
        return whiteList;
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
    @PostMapping(value = "/cbcNineSendAuthMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendAuth(@RequestParam String driverLicense, @RequestParam String phone, String infoCard, HttpServletRequest request)
            throws NotRuleException {
        //109白名单
        String nineWhiteList = nineWhiteListRepository.findByDriverLicenseAndPhoneNumber(driverLicense, phone);
        if (ObjectUtils.isEmpty(nineWhiteList)) {
            logger.info("身份证" + driverLicense + "电话号码" + phone + "用户不在109白名单");
            throw new NotRuleException("nineWhiteLists.notWhiteLists");
        } else {
            infoCard = nineWhiteList;
        }
        MsgContent mc = new MsgContent();
        mc.setKh(infoCard);
        if (!StringUtils.isEmpty(phone)) {
            phone = phone.substring(7, 11);
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
