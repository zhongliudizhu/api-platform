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
import java.util.Calendar;
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
    public void submitAnswer(HttpServletRequest request, String answer, Date nowTime) throws NotRuleException {
        Date date1 = new Date(2019, 4, 01);
        Date now;
        Calendar c = Calendar.getInstance();
        now = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        if(date1.after(now)) {
            //设置时间大于当前时间
            AnswerRecord answerRecord = new AnswerRecord();
            String accountId = accountService.getAccountId(request);
            answerRecord.setAccountId(accountId);
            answerRecord.setCreatedAt(new Date());
            answerRecord.setAnswer(answer);
            answerRecordRepository.save(answerRecord);
        }else {
            //设置时间小于当前时间（活动已过期）
            throw new NotRuleException("activityOverdue.drawActivity");
        }
    }


}
