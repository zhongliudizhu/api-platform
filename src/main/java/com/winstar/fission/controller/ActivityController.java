package com.winstar.fission.controller;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.fission.entity.ActivityQuestions;
import com.winstar.fission.entity.AnswerQuestionRecord;
import com.winstar.fission.repository.AnswerQuestionRecordRepository;
import com.winstar.fission.service.ActivityService;
import com.winstar.fission.service.QuestionsService;
import com.winstar.user.entity.Account;
import com.winstar.user.param.AuthDriverLicenseParam;
import com.winstar.user.utils.ServiceManager;
import com.winstar.user.utils.SimpleResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 裂变活动资格
 *
 * @author dpw
 * @create 2018-07-10 9:57
 **/
@RestController
@RequestMapping("/api/v1/cbc/fission/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;
    public static String TOKEN_TYPE_WECHAT = "1";

    /**
     * 校验资格
     *
     * @return
     * @throws NotFoundException
     */
    @GetMapping("checkQualified")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResult checkQualified(HttpServletRequest request) throws NotRuleException {
        String accountId = ServiceManager.accountService.getAccountId(request);
        Account account = ServiceManager.accountRepository.findOne(accountId);

        activityService.checkBindDriverLicense(account);
        activityService.checkOilOrder(accountId);

        return new SimpleResult("SUCCESS");
    }

    /**
     * 绑定信息卡
     *
     * @param request
     * @param authDriverLicenseParam
     * @return
     * @throws NotRuleException
     */
    @PostMapping("bindDriverLicense")
    public ResponseEntity authDriverLicense(HttpServletRequest request, @RequestBody AuthDriverLicenseParam authDriverLicenseParam) throws NotRuleException {
        if (null == authDriverLicenseParam)
            throw new NotRuleException("authDriverLicenseParam");
        authDriverLicenseParam.checkParam();

        String accountId = ServiceManager.accountService.getAccountId(request);
        Account account = ServiceManager.accountRepository.findOne(accountId);

        String token = ServiceManager.accountService.getAccessTokenWinstar(account.getOpenid(), TOKEN_TYPE_WECHAT);
        ResponseEntity result = ServiceManager.accountService.authDriverLicense(token, authDriverLicenseParam.getSerialNum(), authDriverLicenseParam.getInfoCard(), authDriverLicenseParam.getInfoCard(), authDriverLicenseParam.getPhone());
        if (result.getStatusCode().is2xxSuccessful()) {
            // todo 更新用户里程数
        }

        return result;
    }

}
