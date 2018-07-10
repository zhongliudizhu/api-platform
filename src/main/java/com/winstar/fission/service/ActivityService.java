package com.winstar.fission.service;

import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import org.springframework.stereotype.Service;

/**
 * 名称： ActivityService
 * 作者： dpw
 * 日期： 2018-07-10 10:13
 * 描述： ActivityService
 **/
@Service
public class ActivityService {
    public SimpleResult checkQualified(String accountId) {
        long finishedOilOrder = ServiceManager.oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(3, accountId, 0);

        long isBindDriveLicense = 0;
        return new SimpleResult("SUCCESS");
    }

    ;
}
