package com.PageViewLog;

import com.winstar.Application;
import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.StringFormatUtils;
import com.winstar.user.param.CCBAuthParam;
import com.winstar.user.param.MsgContent;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.UUIDUtils;
import com.winstar.user.vo.AuthVerifyCodeEntity;
import com.winstar.user.vo.AuthVerifyCodeMsgResult;
import com.winstar.user.vo.SendVerifyCodeEntity;
import com.winstar.user.vo.SendVerifyCodeMsgResult;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class OrderRedPackageInfoTest {

    @Test
    public void test1() throws NotRuleException {
        ServiceManager.orderRedPackageInfoService.generateOrderRedPackageInfoByOrderId(String.valueOf(System.currentTimeMillis()), new BigDecimal(5));
    }

    @Test
    public void test2() throws NotRuleException {
        ServiceManager.orderRedPackageInfoService.receiveOrderRedPackage("ceshiceshi", "1542709602311");
    }

    @Test
    public void test3() throws NotRuleException {
        for (int i = 0; i < 100; i++)
            try {
                ServiceManager.orderRedPackageInfoService.receiveOrderRedPackage(UUIDUtils.getUUID(), "1542709602311");
            } catch (NotRuleException e) {
                log.error("领取失败", e);
            }
    }
}
