package com.winstar.user.controller;

import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.user.entity.Account;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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

    /**
     * 校验是否认证过
     *
     * @param request
     * @return
     */
    @GetMapping("/checkIsAuth")
    public SimpleResult checkAuth(HttpServletRequest request)throws NotRuleException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        Account account = ServiceManager.accountRepository.findOne(accountId);
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
        String accountId = ServiceManager.accountService.getAccountId(request);
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
        String accountId = ServiceManager.accountService.getAccountId(request);

        Account account = ServiceManager.accountRepository.findOne(accountId);
        if (null != account && !StringUtils.isEmpty(account.getAuthInfoCard()) && !StringUtils.isEmpty(account.getAuthMobile())) {
            throw new NotRuleException("repeatedAuth");
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

        return accountSaved;
    }
}
