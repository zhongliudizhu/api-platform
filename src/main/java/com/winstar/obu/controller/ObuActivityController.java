package com.winstar.obu.controller;

import com.winstar.ClientErrorHandler;
import com.winstar.couponActivity.entity.WhiteList;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.obu.entity.*;
import com.winstar.obu.repository.*;
import com.winstar.obu.service.ObuDotService;
import com.winstar.obu.service.ObuTokenService;
import com.winstar.obu.utils.Result;
import com.winstar.obu.utils.SmsUtil;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AccountParam;
import com.winstar.user.param.UpdateAccountParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.UUIDUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Value("${send_sms_url}")
    String sendSmsUrl;
    @Value("${verify_sms_url}")
    String verifySmsUrl;

    final static int obuType = 1;

    /**
     * 发送短信
     * @param request
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value = "sendSMS", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ResponseEntity  sendSMS(HttpServletRequest request,
                                  @RequestParam String phoneNumber,
                                  @RequestParam(defaultValue = "0")String type) throws NotFoundException, NotRuleException{
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
     * 校验验证码  未用
     * @param phoneNumber
     * @param msgVerifyCode
     * @param msgVerifyId
     * @return
     * @throws NotFoundException
     * @throws NotRuleException
     */
    @RequestMapping(value = "verifySMS", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public boolean verifySMS(String phoneNumber, String msgVerifyCode, String msgVerifyId) throws NotFoundException, NotRuleException{
        UpdateAccountParam updateAccountParam =new UpdateAccountParam();
        if(StringUtils.isEmpty(phoneNumber)){
            throw new NotRuleException("obu.phoneNumber");
        }
        if(StringUtils.isEmpty(msgVerifyCode)){
            throw new NotRuleException("couponActivity.msgVerifyCode");
        }
        if(StringUtils.isEmpty(msgVerifyId)){
            throw new NotRuleException("couponActivity.msgVerifyId");
        }
        Boolean aBoolean = SmsUtil.verifySms(getUpdateAccountParam(phoneNumber,msgVerifyCode,msgVerifyId),verifySmsUrl);
        Result result = new Result();
        if(aBoolean){
            result.setResult("success");
            result.setStatus(1);
        }else{
            result.setResult("fail");
            result.setStatus(0);
        }
        return aBoolean;
    }

    /**
     * 获取Token
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

        if (!SmsUtil.verifySms(getUpdateAccountParam(phoneNumber,msgVerifyCode,msgVerifyId),verifySmsUrl))
            throw new NotRuleException("msgInvalid.code");

        ObuToken obuToken = obuTokenService.getToken(phoneNumber);
        if (null == obuToken) {
            throw new NotRuleException("no_oauth");
        }
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
     * 发送OBU
     * @param request
     * @param driverLicense
     * @param phoneNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "check", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ObuInfo checkObu(HttpServletRequest request, String driverLicense,
                            String phoneNumber,
                            String msgVerifyCode,
                            String msgVerifyId) throws NotRuleException, NotFoundException{
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

        if (!SmsUtil.verifySms(getUpdateAccountParam(phoneNumber,msgVerifyCode,msgVerifyId),verifySmsUrl))
            throw new NotRuleException("msgInvalid.code");

        ObuConfig obuConfig = obuConfigRepository.findByType(obuType);
        long count = obuRepository.countByType(obuType);//1:赠送  count:赠送总量

        if(count > obuConfig.getLimitNum() || count == 0){
            throw new NotFoundException("obu.isLimit");
        }

        ObuWhiteList obuWhiteList = obuWhiteListRepository.checkWhiteList(phoneNumber, driverLicense, 0, new Date(), new Date());

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
        };
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
     * @throws NotFoundException
     * @throws NotRuleException
     */
    @RequestMapping(value = "getSwitch", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ObuConfig getSwitch(HttpServletRequest request) throws NotFoundException, NotRuleException{
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

}
