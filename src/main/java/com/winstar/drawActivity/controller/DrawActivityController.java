package com.winstar.drawActivity.controller;

import com.winstar.drawActivity.comm.ErrorCodeEnum;
import com.winstar.drawActivity.entity.DrawRecord;
import com.winstar.drawActivity.repository.DrawRecordRepository;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.redis.RedisTools;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 奖品总数
     */
    private static final int MAX_PRIZE_NUM = 555;

    /**
     * 活动id
     */
    private static final String ACTIVITY_ID = "204";

    /**
     * 99元隐形优惠券
     */
    private static final String SMALL_PRIZE = "prize_99";

    /**
     * 999元隐形优惠券
     */
    private static final String LARGE_PRIZE = "prize_999";

    /**
     * 已投放总数
     */
    private static final String PUT_IN_NUM = "punInNum";

    private final DrawRecordRepository drawRecordRepository;

    private final AccountService accountService;

    private final RedisTools redisTools;

    private final OilOrderRepository oilOrderRepository;

    @Autowired
    public DrawActivityController(DrawRecordRepository drawRecordRepository, OilOrderRepository oilOrderRepository, RedisTools redisTools, AccountService accountService) {
        this.drawRecordRepository = drawRecordRepository;
        this.redisTools = redisTools;
        this.accountService = accountService;
        this.oilOrderRepository = oilOrderRepository;
    }

    @RequestMapping(value = "/judge")
    public Result judge(HttpServletRequest request) throws NotRuleException {
        //判断是否在活动时间内及奖品是否充足
        if (System.currentTimeMillis() > END_OF_MARCH || getPrizeNum() == 0) {
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.description());
        }
        String accountId = accountService.getAccountId(request);
        //判断用户是否绑定交安卡
        Account account = accountService.findOne(accountId);
        log.info("用户id是： {} ,交安卡号是： {} ,认证手机尾号是： {}", accountId, account.getAuthInfoCard(), account.getAuthMobile());
        if (ObjectUtils.isEmpty(account.getAuthInfoCard()) || ObjectUtils.isEmpty(account.getAuthMobile())) {
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_USER_DID_NOT_BIND.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_USER_DID_NOT_BIND.description());
        }
        DrawRecord drawRecord = drawRecordRepository.findByAccountId(accountId);
        log.info("用户的抽奖情况： {}", drawRecord);
        //判断用户是否参与过活动
        if (drawRecord == null) {
            Map<String, String> map = new HashMap<>();
            map.put("authInfoCard", account.getAuthInfoCard());
            return Result.success(map);
        }
        boolean isBought = isBought(accountId);
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
    private boolean isBought(String accountId) {
        boolean flag = false;
        List<OilOrder> oilOrders = oilOrderRepository.findByAccountIdAndActivityId(accountId, ACTIVITY_ID);
        if (!ObjectUtils.isEmpty(oilOrders)) {
            for (OilOrder o : oilOrders) {
                if (o.getPayStatus() == 1) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 获取剩余奖品数量
     */
    private int getPrizeNum() throws NotRuleException {
        Integer last_99 = (Integer) redisTools.get(SMALL_PRIZE);
        Integer last_999 = (Integer) redisTools.get(LARGE_PRIZE);
        Integer punInNum = (Integer) redisTools.get(PUT_IN_NUM);
        int lastNum = MAX_PRIZE_NUM - punInNum + last_99 + last_999;
        if (lastNum < 0) {
            throw new NotRuleException("奖品超出限制");
        }
        log.info("剩余奖品是：{}", lastNum);
        return lastNum;
    }

}
