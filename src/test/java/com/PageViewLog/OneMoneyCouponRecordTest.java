package com.PageViewLog;

import com.winstar.Application;
import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class OneMoneyCouponRecordTest {

    @Autowired
    WhiteListRepository whiteListRepository;

    @Autowired
    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;


    @Autowired
    AccountCouponService accountCouponService;

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Test
    public void sendCoupon() {
        String templateId = "000000006b96f5ca016bb166e6f60001";
        String accountId = "ff8080816b6dbd32016b876205b2002c";
        for (int i = 0; i < 10; i++) {

            ResponseEntity<Map> responseEntity = accountCouponService.getCoupon(templateId, "1");
            Map map = responseEntity.getBody();
            if (MapUtils.getString(map, "code").equals("SUCCESS")) {
                log.info("获取优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
                SendCouponDomain domain = new SendCouponDomain(templateId, accountId, AccountCoupon.TYPE_YJX, "1", null, null);
                accountCouponService.sendCoupon(domain, null);
//                List<AccountCoupon> accountCoupons = RequestUtil.getAccountCoupons(JSON.toJSONString(map.get("data")), "yjx", accountId, null, null);
//                accountCouponRepository.save(accountCoupons);
                log.info("发放优惠券成功！accountId is {} and templateId is {}", accountId, templateId);
            } else {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
    }

    @Test
    public void test() {
        MsgContent mc = new MsgContent();
        String phone = "15929369883";
        String infoCard = "6259655764046666";
        mc.setKh("6259655764046666");
        if (!StringUtils.isEmpty(phone)) {
            phone = phone.substring(7, 11);
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
            log.info("序号：" + sendVerifyCodeEntityList.get(0).getXh());

        } else {
            log.info(new StringBuilder("发送失败01：infoCard-->").append(infoCard).append("phone-->").append(phone).toString());
            log.error(new StringBuilder("发送失败02,").append(sendVerifyCodeMsgResult.getErrorMessage()).append(infoCard).append("_").append(phone).toString());
//            throw new NotRuleException(sendVerifyCodeMsgResult.getErrorMessage());
        }

    }

//    @Test
//    public void testEarly() {
//        EarlyAndEveningMarketConfig earlyAndEveningMarketConfig = new EarlyAndEveningMarketConfig();
//        earlyAndEveningMarketConfig.setMarketStartTime(0);
//        earlyAndEveningMarketConfig.setMarketEndTime(24);
//        earlyAndEveningMarketConfigService.checkTime(earlyAndEveningMarketConfig, new Date());
//    }

    @Test
    public void test1() {
        String phoneNumber = "15929369883";
        String infoCard = "6259655764046666";
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
