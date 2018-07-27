package com.PageViewLog;

import com.winstar.Application;
import com.winstar.couponActivity.entity.WhiteList;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.couponActivity.utils.TimeUtil;
import com.winstar.exception.InnerServerException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.obu.entity.ObuConfig;
import com.winstar.obu.entity.ObuInfo;
import com.winstar.obu.entity.ObuToken;
import com.winstar.obu.entity.ObuWhiteList;
import com.winstar.obu.repository.ObuAccountRepository;
import com.winstar.obu.repository.ObuConfigRepository;
import com.winstar.obu.repository.ObuRepository;
import com.winstar.obu.repository.ObuWhiteListRepository;
import com.winstar.obu.service.ObuDotService;
import com.winstar.obu.service.ObuTokenService;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.redis.RedisTools;
import com.winstar.shop.entity.Activity;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.service.OneMoneyCouponRecordService;

import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.UUIDUtils;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class OneMoneyCouponRecordTest {

    @Autowired
    WhiteListRepository whiteListRepository;


    @Test
    public void test(){
        MsgContent mc = new MsgContent();
        String phone = "15929369883";
        String infoCard ="6259655764046666";
        mc.setKh("6259655764046666");
        if(!StringUtils.isEmpty(phone)){
            phone = phone.substring(7,11);
        }
        mc.setSjh(phone);
        String msgParam = StringFormatUtils.bean2JsonStr(mc);
        System.out.println();
        //发送短息
        SendVerifyCodeMsgResult sendVerifyCodeMsgResult = ServiceManager.smsService.sendSms(msgParam);
        //判断返回结果
        if (sendVerifyCodeMsgResult != null && sendVerifyCodeMsgResult.getStatus().equals("success")) {
            CCBAuthParam ccbAuthParam = new CCBAuthParam();
            List<SendVerifyCodeEntity> sendVerifyCodeEntityList = sendVerifyCodeMsgResult.getResults();
            ccbAuthParam.setXh(sendVerifyCodeEntityList.get(0).getXh());
            ccbAuthParam.setKh(sendVerifyCodeEntityList.get(0).getKh());
            log.info(new StringBuilder("实名认证建行验证码发送：infoCard-->").append(infoCard).append("phone-->").append(phone).toString());
            log.info("序号："+sendVerifyCodeEntityList.get(0).getXh());

        } else {
            log.info(new StringBuilder("发送失败01：infoCard-->").append(infoCard).append("phone-->").append(phone).toString());
            log.error(new StringBuilder("发送失败02,").append(sendVerifyCodeMsgResult.getErrorMessage()).append(infoCard).append("_").append(phone).toString());
//            throw new NotRuleException(sendVerifyCodeMsgResult.getErrorMessage());
        }

    }


    @Test
    public void test1(){
        String phoneNumber = "15929369883";
        String infoCard ="6259655764046666";
        String msgVerifyId = "072718332900004";
        String msgVerifyCode = "264463";
        MsgContent mc = new MsgContent();
        mc.setKh(infoCard);
        mc.setXh(msgVerifyId);
        mc.setYzm(msgVerifyCode);
        //调用验证验证码接口
        String msgParam = StringFormatUtils.bean2JsonStr(mc);
        AuthVerifyCodeMsgResult authVerifyCodeMsgResult = ServiceManager.smsService.authSms(msgParam);
        if (!authVerifyCodeMsgResult.getStatus().equals("success")) {
            log.error(new StringBuilder("验证码验证失败,").append(authVerifyCodeMsgResult.getErrorMessage()).append("_")
                    .append(phoneNumber).append("_")
                    .append(msgVerifyCode).append("_").append(infoCard).toString());
             System.out.println("INVALID_VERIFY");
        }
        List<AuthVerifyCodeEntity> authVerifyCodeEntityList = authVerifyCodeMsgResult.getResults();
        //判读是否得到驾驶证号码
        if (StringUtils.isEmpty(authVerifyCodeEntityList.get(0).getSfzhm())) {
            log.error(new StringBuilder("银行卡账号不正确请检查数据").append(phoneNumber).append("_").append(msgVerifyCode).append("_").append(infoCard).toString());
//            throw new NotRuleException("CardNoOrMobile");
            System.out.println("CardNoOrMobile");
        }


    }
}
