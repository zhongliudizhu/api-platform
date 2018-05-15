package com.PageViewLog;

import com.winstar.Application;
import com.winstar.redis.RedisTools;
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

    /*@Autowired
    private OneMoneyCouponRecordService oneMoneyCouponRecordService;*/
    @Autowired
    RedisTools redisTools;

    @Test
    public void testUpdate() {
        redisTools.set("bb","abc");

        redisTools.set("bb1","abcxxx",60*2L);

        System.out.println(redisTools.get("bb"));
        System.out.println(redisTools.exists("bb"));
        System.out.println(redisTools.exists("bbx"));
        System.out.println(redisTools.get("bb1"));
        redisTools.remove("bb1");

/*
        boolean hasChance = oneMoneyCouponRecordService.checkBuyAuth("1");
        System.out.print("SUCCESS");*/
    }

}
