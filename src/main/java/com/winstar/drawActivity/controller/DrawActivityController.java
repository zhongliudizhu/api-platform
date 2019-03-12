package com.winstar.drawActivity.controller;

import com.winstar.drawActivity.entity.DrawRecord;
import com.winstar.drawActivity.repository.DrawRecordRepository;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classname: DrawActivityController
 * Description: 建行第一季度锦鲤抽奖活动
 * Date: 2019/3/12 9:27
 * author: uu
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/cbc/drawActivity")
public class DrawActivityController {
    /**
     * 2019.03.31.23.59.59
     */
    private static final long ENDOFMARCH = 1554047999;

    /**
     * 99元隐形优惠券
     */
    private static final String SMALLPRIZE = "prize99";

    /**
     * 999元隐形优惠券
     */
    private static final String LARGEPRIZE = "prize999";

    private final RedisTools redisTools;

    private final DrawRecordRepository drawRecordRepository;

    private final AccountService accountService;

    @Autowired
    public DrawActivityController(DrawRecordRepository drawRecordRepository, RedisTools redisTools, AccountService accountService) {
        this.drawRecordRepository = drawRecordRepository;
        this.redisTools = redisTools;
        this.accountService = accountService;
    }

    @RequestMapping(value = "/judge")
    public Map<String, Object> judge(HttpServletRequest request) throws NotRuleException {
        Map<String, Object> map = new HashMap<>();
        //判断是否在活动时间内及奖品是否充足
        if (System.currentTimeMillis() > ENDOFMARCH || getPrizeNum() == 0) {
            map.put("code", "400");
            map.put("message", "该活动已结束");
            return map;
        }
        String accountId = accountService.getAccountId(request);
        DrawRecord drawRecord = drawRecordRepository.findByAccountId(accountId);
        //判断用户是否参与过活动
        if (drawRecord == null) {
            map.put("code", "301");
            map.put("message", "跳转authMsg");
            return map;
        }
        if ("NO".equals(drawRecord.getIsPrized()) || isBought()) {
            map.put("code", "401");
            map.put("message", "抱歉您已参与过该活动，无法重复参与哦");
            return map;
        }
        if (!isBought()) {
            map.put("code", "302");
            map.put("message", "已中奖未购买");
            map.put("data", drawRecord.getPrizeType());
            return map;
        }
        map.put("code", "200");
        map.put("message", "跳转authMsg");
        return map;
    }

    /**
     * 判断是否购买
     */
    private boolean isBought() {
        //TODO
        return true;
    }

    /**
     * 获取剩余奖品数量
     */
    private int getPrizeNum() {
        Set set = redisTools.setMembers(SMALLPRIZE);
        Set set2 = redisTools.setMembers(LARGEPRIZE);
        int num1 = set == null ? 0 : set.size();
        int num2 = set2 == null ? 0 : set2.size();
        return num1 + num2;
    }

}
