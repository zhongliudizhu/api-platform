package com.winstar.obu.controller;

import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.obu.entity.*;
import com.winstar.obu.repository.ObuAccountRepository;
import com.winstar.obu.repository.ObuConfigRepository;
import com.winstar.obu.repository.ObuRepository;
import com.winstar.obu.repository.ObuWhiteListRepository;
import com.winstar.obu.service.ObuDotService;
import com.winstar.obu.service.ObuTokenService;
import com.winstar.obu.utils.SendSmsRequest;
import com.winstar.obu.utils.SmsUtil;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.param.UpdateAccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.UUIDUtils;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import org.apache.log4j.Logger;
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
 * ObuActivityController
 *
 * @author: Big BB
 * @create 2018-06-28 11:40
 * @DESCRIPTION:
 **/
@RestController
@RequestMapping("/api/v1/cbc/obuActivity")
public class ObuActivityController {

    private static Logger logger = Logger.getLogger(ObuActivityController.class);

    @Autowired
    ObuRepository obuRepository;
    @Autowired
    ObuWhiteListRepository obuWhiteListRepository;
    @Autowired
    ObuDotService obuDotService;
    @Autowired
    ObuConfigRepository obuConfigRepository;
    @Autowired
    ObuAccountRepository obuAccountRepository;
    @Autowired
    ObuTokenService obuTokenService;
    @Autowired
    WhiteListRepository whiteListRepository;
    @Value("${send_sms_url}")
    String sendSmsUrl;
    @Value("${verify_sms_url}")
    String verifySmsUrl;
    @Value("${get_random_image_code}")
    String getRandomCodeImageUrl;
    @Value("${send_sms_image_url}")
    String verifyImageSmsUrl;

    final static int obuType = 1;

    /**
     *  短信服务（公司内部）-发送短信
     * @param request
     * @return
     */
    @RequestMapping(value = "sendSMS", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResponseEntity  sendSMS(HttpServletRequest request,
                                  @RequestParam String phoneNumber,
                                  @RequestParam(defaultValue = "0")String type) {
        ResponseEntity resp = SmsUtil.sendSms(phoneNumber,sendSmsUrl);

        if(type.equals("0")){
            ObuToken obuToken = obuTokenService.findByPhoneNumber(phoneNumber);
            if(ObjectUtils.isEmpty(obuToken)){
                obuToken = new ObuToken();
                obuToken.setTokenId(UUIDUtils.getUUID());
                obuToken.setPhoneNumber(phoneNumber);
            }
            logger.info("createToken:"+obuToken.getTokenId());
            obuTokenService.createObuToken(obuToken);
        }
        return resp;
    }

    /**
     * 获取Token（包含验证验证码码合法性（公司内部）、生成token、更新token等）
     * @param request
     * @param phoneNumber
     * @param regFrom
     * @param msgVerifyCode
     * @param msgVerifyId
     * @return
     * @throws NotRuleException
     */
    @RequestMapping(value = "getToken", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ObuToken getToken(HttpServletRequest request,
                             String phoneNumber, String regFrom,
                             String msgVerifyCode, String msgVerifyId) throws NotRuleException {
        if(StringUtils.isEmpty(phoneNumber)){
            throw new NotRuleException("obu.phoneNumber");
        }
        if(StringUtils.isEmpty(msgVerifyCode)){
            throw new NotRuleException("couponActivity.msgVerifyCode");
        }
        if(StringUtils.isEmpty(msgVerifyId)){
            throw new NotRuleException("couponActivity.msgVerifyId");
        }

        if (!SmsUtil.verifySms(getUpdateAccountParam(phoneNumber,msgVerifyCode,msgVerifyId),verifySmsUrl)) {
            throw new NotRuleException("msgInvalid.code");
        }
        //生成/更新token
        ObuToken obuToken = obuTokenService.findByPhoneNumber(phoneNumber);
        if(ObjectUtils.isEmpty(obuToken)){
            obuToken = new ObuToken();
            obuToken.setTokenId(UUIDUtils.getUUID());
            obuToken.setPhoneNumber(phoneNumber);
        }
        logger.info("createToken:"+obuToken.getTokenId());
        obuToken = obuTokenService.createObuToken(obuToken);

        ObuAccount account = obuAccountRepository.findByPhoneNumber(phoneNumber);
        if (null == account) {
            obuAccountRepository.save(getObuAccount(phoneNumber,regFrom));
        }
        logger.info("getToken:"+obuToken.getTokenId());
        return obuToken;
    }

    public ObuAccount getObuAccount(String phoneNumber, String regFrom){
        ObuAccount obuAccount = new ObuAccount();
        obuAccount.setCreateTime(new Date());
        obuAccount.setUpdateTime(new Date());
        obuAccount.setPhoneNumber(phoneNumber);
        obuAccount.setRegFrom(regFrom);
        return  obuAccount;
    }


    /**
     * OBU发送验证码(建行短信服务)
     *
     * @param infoCard
     * @param phone
     * @return
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @PostMapping(value = "/sendAuthMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendAuth(@RequestParam String driverLicense, @RequestParam String phone,String infoCard,HttpServletRequest request)
            throws NotRuleException {
        driverLicense="%"+driverLicense;
        //OBU白名单
        ObuWhiteList obuWhiteListcardNumber = obuWhiteListRepository.findByDriverLicenseAndPhoneNumber(driverLicense, phone);
        if(ObjectUtils.isEmpty(obuWhiteListcardNumber)){
            throw new NotRuleException("obu.notWhiteLists");
        }else {
            infoCard = obuWhiteListcardNumber.getCardNumber();
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

    /**
     * 发送OBU（生成obu、验证短信合法性（建行）等）
     * @param request
     * @param driverLicense
     * @param phoneNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "check", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ObuInfo checkObu(HttpServletRequest request,
                            String driverLicense,
                            String phoneNumber,
                            String msgVerifyCode,
                            String msgVerifyId) throws NotRuleException, NotFoundException, ParseException {
        String tokenId = request.getHeader("obu_token_id");
        logger.info("check:"+tokenId);
        if(StringUtils.isEmpty(tokenId)){
            throw new NotRuleException("no_oauth");
        }
        ObuToken obuToken = obuTokenService.findByTokenId(tokenId);
        if(ObjectUtils.isEmpty(obuToken)){
            throw new NotRuleException("no_oauth");
        }
        if(StringUtils.isEmpty(driverLicense)){
            throw new NotRuleException("obu.driverLicense");
        }
        if(StringUtils.isEmpty(phoneNumber)){
            throw new NotRuleException("obu.phoneNumber");
        }
        if(StringUtils.isEmpty(msgVerifyCode)){
            throw new NotRuleException("couponActivity.msgVerifyCode");
        }
        if(StringUtils.isEmpty(msgVerifyId)){
            throw new NotRuleException("couponActivity.msgVerifyId");
        }
        driverLicense="%"+driverLicense;
       String infoCard ="";
//        //根据身份证跟电话号码查询交安卡卡号

        ObuWhiteList obuWhiteListcardNumber = obuWhiteListRepository.findByDriverLicenseAndPhoneNumber(driverLicense, phoneNumber);
        if(ObjectUtils.isEmpty(obuWhiteListcardNumber)){
            throw new NotFoundException("obu.notWhiteLists");
        }else {
            infoCard = obuWhiteListcardNumber.getCardNumber();
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

//        if (!SmsUtil.verifySms(getUpdateAccountParam(phoneNumber,msgVerifyCode,msgVerifyId),verifySmsUrl))
//            throw new NotRuleException("msgInvalid.code");

        ObuConfig obuConfig = obuConfigRepository.findByType(obuType);
        long count = obuRepository.countByType(obuType);//1:赠送  count:赠送总量

        if(count > obuConfig.getLimitNum() || obuConfig.getLimitNum() == 0){
            throw new NotFoundException("obu.isLimit");
        }

        ObuWhiteList obuWhiteList = obuWhiteListRepository.checkWhiteList(phoneNumber, 0, DateUtil.getNowDay(), DateUtil.getNowDay());

        if(ObjectUtils.isEmpty(obuWhiteList)){
            throw new NotFoundException("obu.notWhiteLists");
        }
        ObuInfo obuInfo = null;
        try {
            obuInfo = getObuInfo(obuWhiteList, obuToken.getPhoneNumber());
        } catch (ParseException e) {
            throw new NotRuleException("obu.error");
        }
        updateObuWhiteList(obuWhiteList, obuToken.getPhoneNumber());
        return obuInfo;
    }

    public ObuInfo getObuInfo(ObuWhiteList obuWhiteList, String phoneNumber) throws ParseException {
        ObuInfo obuInfo = new ObuInfo();
        obuInfo.setCreateTime(new Date());
        obuInfo.setAccountId(phoneNumber);
        obuInfo.setETC(obuWhiteList.getEtc());
        obuInfo.setName(obuWhiteList.getName());
        obuInfo.setIdentity(obuWhiteList.getDriverLicense());
        obuInfo.setPhone(obuWhiteList.getPhoneNumber());
        String serialNum = DateUtil.getCurrentYear()+obuWhiteList.getEtc();
        obuInfo.setSerialNum(serialNum);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        obuInfo.setEndTime(simpleDateFormat.parse("2018-12-31"));
        obuInfo.setType(obuType);//1:赠送
        obuInfo.setState("0");
        obuRepository.save(obuInfo);
        return obuInfo;
    }

    public void updateObuWhiteList(ObuWhiteList obuWhiteLis ,String phoneNumber){
        obuWhiteLis.setAccountId(phoneNumber);
        obuWhiteLis.setIsGet(1);
        obuWhiteLis.setSendTime(new Date());
        obuWhiteListRepository.save(obuWhiteLis);
    }

    /**
     * 获取所有网点
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "getDot", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public List<ObuDot> getDots(HttpServletRequest request) throws NotFoundException{
        List<ObuDot> obuDots = obuDotService.findAll();
        if(ObjectUtils.isEmpty(obuDots)){
            throw new NotFoundException("obu.notObuDots");
        }
        return obuDots;
    }

    /**
     * 查看我的OBU
     * @param request
     * @return
     * @throws NotFoundException
     * @throws NotRuleException
     */
    @RequestMapping(value = "myObu", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public List<ObuInfo> myObuInfo(HttpServletRequest request) throws NotFoundException, NotRuleException{
        String tokenId = request.getHeader("obu_token_id");
        if(StringUtils.isEmpty(tokenId)){
            throw new NotRuleException("no_oauth");
        }
        logger.info("myObu:"+tokenId);
        ObuToken obuToken = obuTokenService.findByTokenId(tokenId);
        if(ObjectUtils.isEmpty(obuToken)){
            throw new NotRuleException("no_oauth");
        }
        List<ObuInfo> obuInfo = obuRepository.findByAccountId(obuToken.getPhoneNumber());
        if(ObjectUtils.isEmpty(obuInfo)){
           throw new NotFoundException("obu.not");
        }
        return obuInfo;
    }

    /**
     * 获取活动开关
     * @param request
     * @return
     */
    @RequestMapping(value = "getSwitch", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ObuConfig getSwitch(HttpServletRequest request) {
        ObuConfig obuConfig = obuConfigRepository.findByType(2);
        if(ObjectUtils.isEmpty(obuConfig)){
             obuConfig = new ObuConfig();
             obuConfig.setLimitNum(0);
        }
        return obuConfig;
    }

    public UpdateAccountParam getUpdateAccountParam(String phoneNumber, String msgVerifyCode, String msgVerifyId){
        UpdateAccountParam updateAccountParam =new UpdateAccountParam();
        updateAccountParam.setMobile(phoneNumber);
        updateAccountParam.setMsgVerifyCode(msgVerifyCode);
        updateAccountParam.setMsgVerifyId(msgVerifyId);
        return updateAccountParam;
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getRandomCode", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResponseEntity  getRandomCode(HttpServletRequest request) {
        logger.info("获取图片验证码");
        ResponseEntity resp = SmsUtil.getRandomCode(getRandomCodeImageUrl);
        return resp;
    }

    /**
     * 1、验证图片验证码
     * 2、发送短信
     */
    @RequestMapping(value = "smsImageSend", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ResponseEntity  smsImageSend(@RequestBody SendSmsRequest sendSmsRequest) {
        logger.info("验证图片验证码，并发短息");
        ResponseEntity resp = SmsUtil.verifyImageSmsUrl(verifyImageSmsUrl,sendSmsRequest);
        return resp;
    }
}
