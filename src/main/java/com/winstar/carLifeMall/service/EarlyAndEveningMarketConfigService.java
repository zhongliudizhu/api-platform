package com.winstar.carLifeMall.service;

import com.winstar.carLifeMall.entity.EarlyAndEveningMarketConfig;
import com.winstar.carLifeMall.repository.EarlyAndEveningMarketConfigRepository;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 名称： EarlyAndEveningMarketConfigService
 * 作者： dpw
 * 日期： 2018-05-24 14:54
 * 描述： 早晚市配置
 **/
@Service
public class EarlyAndEveningMarketConfigService {
    @Autowired
    EarlyAndEveningMarketConfigRepository earlyAndEveningMarketConfigRepository;

    /**
     * 判断是否在售卖时间段内
     *
     * @return
     */
    public boolean checkIfOk(int type) throws NotRuleException, InvalidParameterException {
        EarlyAndEveningMarketConfig earlyAndEveningMarketConfig = earlyAndEveningMarketConfigRepository.findByType(type);
        if (earlyAndEveningMarketConfig == null) {
            if (type == EarlyAndEveningMarketConfig.TYPE_EARLY_MARKET)
                throw new NotRuleException("earlyMarketNotStarted");
            else if (type == EarlyAndEveningMarketConfig.TYPE_EVENING_MARKET)
                throw new NotRuleException("eveningMarketNotStarted");
            else
                throw new InvalidParameterException("type");
        }
        if (earlyAndEveningMarketConfig == null)
            return false;

        Date curDate = new Date();
        String week = DateUtil.getWeekOfDate(curDate);
        if (!checkWeekIsOk(earlyAndEveningMarketConfig, week) || !checkTime(earlyAndEveningMarketConfig, curDate))
            return false;
        return true;
    }

    private boolean checkWeekIsOk(EarlyAndEveningMarketConfig earlyAndEveningMarketConfig, String week) {
        return earlyAndEveningMarketConfig.getWeekDay().contains(week);
    }

    private boolean checkTime(EarlyAndEveningMarketConfig earlyAndEveningMarketConfig, Date curDate) {
       if (DateUtil.getHour(curDate) <= earlyAndEveningMarketConfig.getMarketStartTime() || DateUtil.getHour(curDate) >= earlyAndEveningMarketConfig.getMarketEndTime()) {
            return false;
        }
        return false;
    }

}
