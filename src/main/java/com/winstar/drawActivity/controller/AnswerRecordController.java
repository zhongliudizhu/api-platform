package com.winstar.drawActivity.controller;

import com.winstar.drawActivity.entity.AnswerRecord;
import com.winstar.drawActivity.repository.AnswerRecordRepository;
import com.winstar.exception.NotRuleException;
import com.winstar.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
     * 提交答案
     */
    @RequestMapping(value = "/submitAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerRecord submitAnswer(HttpServletRequest request,
                                     @RequestParam String answer) throws NotRuleException {
        AnswerRecord answerRecord = new AnswerRecord();
        String accountId = accountService.getAccountId(request);
        answerRecord.setAccountId(accountId);
        answerRecord.setCreatedAt(new Date());
        answerRecord.setAnswer(answer);
        answerRecordRepository.save(answerRecord);
        return answerRecord;
    }



}
