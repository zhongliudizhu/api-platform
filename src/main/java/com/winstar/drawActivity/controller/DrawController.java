package com.winstar.drawActivity.controller;

import com.winstar.cashier.construction.utils.Arith;
import com.winstar.drawActivity.comm.ErrorCodeEnum;
import com.winstar.drawActivity.entity.DrawRecord;
import com.winstar.drawActivity.repository.DrawRecordRepository;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.vo.Result;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zl on 2019/3/13
 */
@RestController
@RequestMapping("api/v1/cbc/draw")
public class DrawController {

    private static final Logger logger = LoggerFactory.getLogger(DrawController.class);

    private static final long endTime = 1554047999000L;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DrawRecordRepository drawRecordRepository;

    @Autowired
    RedisTools redisTools;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result draw(HttpServletRequest request, @RequestBody Map reqMap) throws Exception{
        logger.info("抽奖开始>>>>>>>>>>>>>>>>>>>>>>>");
        if(System.currentTimeMillis() > endTime){
            logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.description());
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_END.description());
        }
        logger.info("prize999_number:" + redisTools.get("prize_999"));
        logger.info("prize99_number:" + redisTools.get("prize_99"));
        logger.info("probability_999:" + redisTools.get("probability_999"));
        logger.info("probability_99:" + redisTools.get("probability_99"));
        String accountId = accountService.getAccountId(request);
        if(!redisTools.setIfAbsent(accountId, 1)){
            logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_ONLY_ONE.description());
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_ONLY_ONE.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_ONLY_ONE.description());
        }
        String cardNumber = MapUtils.getString(reqMap, "cardNumber");
        if(StringUtils.isEmpty(cardNumber)){
            logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_USER_NOT_RULE.description());
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_USER_NOT_RULE.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_USER_NOT_RULE.description());
        }
        DrawRecord drawRecord = drawRecordRepository.findByAccountId(accountId);
        if(!ObjectUtils.isEmpty(drawRecord)){
            logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_PARTAKE.description());
            return Result.fail(ErrorCodeEnum.ERROR_CODE_ACTIVITY_PARTAKE.value(), ErrorCodeEnum.ERROR_CODE_ACTIVITY_PARTAKE.description());
        }
        double randomNumber = Arith.mul(new Random().nextDouble(), 100, 2);
        logger.info("randomNumber：" + randomNumber);
        Map<String,String> retMap = new HashMap<>();
        retMap.put("prizedId", "0");
        if(randomNumber <= (Double) redisTools.get("probability_999") && (Integer) redisTools.get("prize_999") > 0){
            logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_HASPRIZE_999.description());
            retMap.put("prizedId", "2");
            redisTools.set("prize_999", (Integer) redisTools.get("prize_999") - 1);
            drawRecordRepository.save(saveDrawRecord(accountId, cardNumber, "2"));
            return Result.success(retMap);
        }
        if(randomNumber <= (Double) redisTools.get("probability_99") && (Integer) redisTools.get("prize_99") > 0){
            logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_HASPRIZE_99.description());
            retMap.put("prizedId", "1");
            redisTools.set("prize_99", (Integer) redisTools.get("prize_99") - 1);
            drawRecordRepository.save(saveDrawRecord(accountId, cardNumber, "1"));
            return Result.success(retMap);
        }
        drawRecordRepository.save(saveDrawRecord(accountId, cardNumber, "0"));
        return Result.success(retMap);
    }

    private static DrawRecord saveDrawRecord(String accountId, String cardNumber, String prizedId){
        DrawRecord drawRecord = new DrawRecord();
        drawRecord.setCreatedAt(new Date());
        drawRecord.setAccountId(accountId);
        drawRecord.setCardNumber(cardNumber);
        drawRecord.setIsPrized(prizedId.equals("0") ? "NO" : "YES");
        drawRecord.setPrizeType(prizedId);
        return drawRecord;
    }

}
