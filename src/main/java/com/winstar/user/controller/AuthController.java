package com.winstar.user.controller;

import com.google.common.collect.Maps;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.user.entity.Account;
import com.winstar.user.entity.Fans;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.param.UpdateAccountParam;
import com.winstar.user.repository.FansRepository;
import com.winstar.user.service.FansService;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import com.winstar.vo.Result;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.winstar.user.utils.ServiceManager.accountService;

/**
 * 名称： AuthController
 * 作者： dpw
 * 日期： 2018-01-26 15:08
 * 描述： 信息卡绑定
 **/
@RestController
@RequestMapping("/api/v1/cbc/auth")
public class AuthController {
    static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Value("${send_sms_url}")
    String sendSmsUrl;

    @Autowired
    FansRepository fansRepository;
    @Autowired
    FansService fansService;

    @GetMapping(value = "/fansInfo")
    public Result userTagInfo(HttpServletRequest request) {
        String openId = (String) request.getAttribute("openId");
        if (ObjectUtils.isEmpty(openId)) {
            return Result.fail("openId_not_exist", "openId不存在");
        }
        Fans fans = fansService.getByOpenId(openId);
        if (ObjectUtils.isEmpty(fans)) {
            return Result.fail("fans_not_exist", "粉丝信息不存在");
        }
        return Result.success(fans);
    }

    /**
     * 校验手机号码是否绑定
     *
     * @return
     * @throws InvalidParameterException, NotRuleException
     */
    @PostMapping("/validateMobile")
    public Map<String, Object> validateMobile(HttpServletRequest request) throws NotRuleException {
        Map<String, Object> activityMap = Maps.newHashMap();
        String accountId = accountService.getAccountId(request);
        Account account = ServiceManager.accountService.findOne(accountId);
        if (StringUtils.isEmpty(account)) {
            activityMap.put("ac_state", "0");
        } else {
            if (StringUtils.isEmpty(account.getMobile())) {
                activityMap.put("ac_state", "0");
            } else {
                activityMap.put("ac_state", "1");
            }
        }
        return activityMap;
    }

    /**
     * 补全手机号码
     *
     * @param updateAccountParam updateAccountParam
     * @return
     */
    @PostMapping("/updateMobile")
    public Account updateMobile(@RequestBody UpdateAccountParam updateAccountParam, HttpServletRequest request) throws NotRuleException, NotFoundException {
        if (!ServiceManager.smsService.verifySms(updateAccountParam)) {
            logger.info("验证码错误！！");
            throw new NotRuleException("code.is.error");
        }
        if (!accountService.checkBindMobileUnique(updateAccountParam.getMobile())) {
            logger.info("该手机号已被绑定！！");
            throw new NotRuleException("phone.had.bind");
        }
        if (null == updateAccountParam || StringUtils.isEmpty(updateAccountParam.getMobile())) {
            logger.info("参数不合法！！！");
            throw new NotFoundException("param.is.null");
        }
        String accountId = accountService.getAccountId(request);
        Account account = ServiceManager.accountService.findOne(accountId);
        account.setMobile(updateAccountParam.getMobile());
        account.setUpdateTime(new Date());

        ServiceManager.redisTools.remove(ServiceManager.REDIS_KEY_FIND_ACCOUNT_BY_ID + accountId);
        ServiceManager.redisTools.remove(ServiceManager.REDIS_KEY_FIND_ACCOUNT_BY_OPENID + account.getOpenid());
        return ServiceManager.accountRepository.save(account);
    }

    /**
     * 校验是否绑定过手机号
     *
     * @param request
     * @return
     * @throws NotRuleException
     */
    @GetMapping("/isBindMobile")
    public SimpleResult isBindMobile(HttpServletRequest request) throws NotRuleException {
        return accountService.checkBindMobile(request);
    }


    /**
     * 校验是否认证过
     *
     * @param request
     * @return
     */
    @GetMapping("/checkIsAuth")
    public SimpleResult checkAuth(HttpServletRequest request) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        Account account = ServiceManager.accountService.findOne(accountId);
        if (null == account || StringUtils.isEmpty(account.getAuthInfoCard())) {
            return new SimpleResult("NO");
        }
        return new SimpleResult("YES");
    }

    /**
     * 发送验证码
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
    public ResponseEntity sendAuth(@RequestParam String infoCard, @RequestParam String phone, HttpServletRequest request)
            throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        MsgContent mc = new MsgContent();
        mc.setKh(infoCard);
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
            logger.info(new StringBuilder("实名认证建行验证码发送：infoCard-->").append(infoCard).append("phone-->").append(phone).append("accountId-->").append(accountId).toString());
            return new ResponseEntity(ccbAuthParam, HttpStatus.OK);
        } else {
            logger.info(new StringBuilder("发送失败01：infoCard-->").append(infoCard).append("phone-->").append(phone).append("accountId-->").append(accountId).toString());
            logger.error(new StringBuilder("发送失败02,").append(sendVerifyCodeMsgResult.getErrorMessage()).append(infoCard).append("_").append(phone).toString());
            throw new NotRuleException(sendVerifyCodeMsgResult.getErrorMessage());
        }
    }

    /**
     * 建行信息卡验证& 用户信息卡认证
     *
     * @param msgContent
     * @param request
     * @return
     * @throws NotRuleException
     */
    @PostMapping(value = "/authMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    public Account authAccount(@RequestBody MsgContent msgContent, HttpServletRequest request) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        Account account = ServiceManager.accountService.findOne(accountId);
        if (null != account && !StringUtils.isEmpty(account.getAuthInfoCard()) && !StringUtils.isEmpty(account.getAuthMobile())) {
            return account;
        }

        if (ServiceManager.accountRepository.countByAuthInfoCard(msgContent.getKh()) > 0) {
            throw new NotRuleException("infoCardIsBind");
        }

        //设置短息
        MsgContent mc = new MsgContent();
        mc.setKh(msgContent.getKh());
        mc.setXh(msgContent.getXh());
        mc.setYzm(msgContent.getYzm());
        //调用验证验证码接口
        String msgParam = StringFormatUtils.bean2JsonStr(mc);
        AuthVerifyCodeMsgResult authVerifyCodeMsgResult = ServiceManager.smsService.authSms(msgParam);
        if (!authVerifyCodeMsgResult.getStatus().equals("success")) {
            logger.error(new StringBuilder("验证码验证失败,").append(authVerifyCodeMsgResult.getErrorMessage()).append("_")
                    .append(msgContent.getSjh()).append("_")
                    .append(msgContent.getYzm()).append("_").append(msgContent.getKh()).toString());
            throw new NotRuleException("INVALID_VERIFY");
        }
        List<AuthVerifyCodeEntity> authVerifyCodeEntityList = authVerifyCodeMsgResult.getResults();
        //判读是否得到驾驶证号码
        if (StringUtils.isEmpty(authVerifyCodeEntityList.get(0).getSfzhm())) {
            logger.error(new StringBuilder("银行卡账号不正确请检查数据").append(msgContent.getSjh()).append("_").append(msgContent.getYzm()).append("_").append(msgContent.getKh()).toString());
            throw new NotRuleException("CardNoOrMobile");
        }
        String driverLicense = authVerifyCodeEntityList.get(0).getSfzhm();
        account.setAuthInfoCard(msgContent.getKh());
        account.setAuthMobile(msgContent.getSjh());
        account.setAuthTime(new Date());
        account.setAuthDriverLicense(driverLicense);
        Account accountSaved = ServiceManager.accountRepository.save(account);
        ServiceManager.redisTools.remove(ServiceManager.REDIS_KEY_FIND_ACCOUNT_BY_ID + accountId);
        ServiceManager.redisTools.remove(ServiceManager.REDIS_KEY_FIND_ACCOUNT_BY_OPENID + account.getOpenid());
        return accountSaved;
    }
}
