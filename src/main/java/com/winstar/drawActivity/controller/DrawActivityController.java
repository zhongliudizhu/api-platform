package com.winstar.drawActivity.controller;

import com.winstar.drawActivity.comm.ErrorCodeEnum;
import com.winstar.drawActivity.entity.DrawRecord;
import com.winstar.drawActivity.repository.DrawRecordRepository;
import com.winstar.exception.NotRuleException;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    private static final long END_OF_MARCH = 1554047999000L;

    /**
     * 99元隐形优惠券
     */
    private static final String SMALL_PRIZE = "prize_99";

    /**
     * 999元隐形优惠券
     */
    private static final String LARGE_PRIZE = "prize_999";

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
    public Result judge(HttpServletRequest request) throws NotRuleException {
        //判断是否在活动时间内及奖品是否充足
        if (System.currentTimeMillis() > END_OF_MARCH || getPrizeNum() == 0) {
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.description());
        }
        String accountId = accountService.getAccountId(request);
        DrawRecord drawRecord = drawRecordRepository.findByAccountId(accountId);
        log.info("用户的抽奖情况： {}", drawRecord);
        //判断用户是否参与过活动
        if (drawRecord == null) {
            return Result.success("");
        }
        boolean isBought = isBought();
        //未中奖或已中将且购买的用户不能重复参与
        if ("NO".equals(drawRecord.getIsPrized()) || isBought) {
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_PARTAKE.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_PARTAKE.description());
        }
        //已中奖未购买返回中奖类型
        if ("1".equals(drawRecord.getPrizeType())) {
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_HASPRIZE_99.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_HASPRIZE_99.description());
        } else if ("2".equals(drawRecord.getPrizeType())) {
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_HASPRIZE_999.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_HASPRIZE_999.description());
        } else {
            throw new NotRuleException("状态异常");
        }
    }

    /**
     * 判断是否购买
     */
    private boolean isBought() {
        //TODO
        return Math.random() > 0.5;
    }

    /**
     * 获取剩余奖品数量
     */
    private int getPrizeNum() {
        int num1 = (int) redisTools.get(SMALL_PRIZE);
        int num2 = (int) redisTools.get(LARGE_PRIZE);
        return num1 + num2;
    }

}
