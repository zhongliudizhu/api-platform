package com.PageViewLog;

import com.winstar.Application;
import com.winstar.user.service.OneMoneyCouponRecordService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class OneMoneyCouponRecordTest {

    @Autowired
    private OneMoneyCouponRecordService oneMoneyCouponRecordService;


    @Test
    public void testUpdate() {

        boolean hasChance = oneMoneyCouponRecordService.checkBuyAuth("1");
        System.out.print("SUCCESS");
    }

}
