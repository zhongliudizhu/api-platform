package com.winstar.drawActivity.controller;

import com.winstar.drawActivity.entity.AnswerRecord;
import com.winstar.drawActivity.repository.AnswerRecordRepository;
import com.winstar.exception.NotRuleException;
import com.winstar.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/cbc/answerRecord")
public class AnswerRecordController {
    @Autowired
    AnswerRecordRepository answerRecordRepository;
    @Autowired
    AccountService accountService;
    /**
     * 2019.03.31.23.59.59
     */
    private static final long END_OF_MARCH = 1554047999000L;

    /**
     * 提交答案
     */
    @RequestMapping(value = "/submitAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void submitAnswer(HttpServletRequest request, String answer) throws NotRuleException {
        if(System.currentTimeMillis()<END_OF_MARCH) {
            AnswerRecord answerRecord = new AnswerRecord();
            String accountId = accountService.getAccountId(request);
            answerRecord.setAccountId(accountId);
            answerRecord.setCreatedAt(new Date());
            answerRecord.setAnswer(answer);
            answerRecordRepository.save(answerRecord);
        }else {
            throw new NotRuleException("activityOverdue.drawActivity");
        }

    }


}
