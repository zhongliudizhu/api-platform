package com.winstar.fission.service;

import com.winstar.exception.NotRuleException;
import com.winstar.user.entity.Account;
import com.winstar.user.utils.ServiceManager;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 名称： ActivityService
 * 作者： dpw
 * 日期： 2018-07-10 10:13
 * 描述： ActivityService
 **/
@Service
@Log4j
public class ActivityService {
    static int OIL_ORDER_STATUS_FINISHED = 3;
    static String OIL_ORDER_IS_AVAILABLE_VALID = "0";
    static String USER_TYPE_WINSTAR_WECHAT = "1";

    public void checkBindDriverLicense(Account account) throws NotRuleException {
        String token = ServiceManager.accountService.getAccessTokenWinstar(account.getOpenid(), USER_TYPE_WINSTAR_WECHAT);
        ResponseEntity responseEntity = ServiceManager.accountService.checkBindDriverLicenseWinstar(token);
        log.debug(responseEntity.getBody().toString());
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new NotRuleException("notBindDriverLicense.noQualified.fission");
    }

    public void checkOilOrder(String accountId) throws NotRuleException {
        long finishedOilOrder = ServiceManager.oilOrderRepository.countByStatusAndAccountIdAndIsAvailable(OIL_ORDER_STATUS_FINISHED, accountId, OIL_ORDER_IS_AVAILABLE_VALID);
        if (finishedOilOrder == 0)
            throw new NotRuleException("noOilOrder.noQualified.fission");
    }
}
