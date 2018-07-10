package com.winstar.fission.controller;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.fission.entity.AnswerQuestionRecord;
import com.winstar.fission.entity.ActivityQuestions;
import com.winstar.fission.repository.AnswerQuestionRecordRepository;
import com.winstar.fission.service.QuestionsService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 派发题库
 *
 * @author Big BB
 * @create 2017-11-20 9:57
 * 派发题库
 **/
@RestController
@RequestMapping("/api/v1/cbc/fission/answer/")
public class QuestionsController {

    @Autowired
    QuestionsService questionsService;
    @Autowired
    AnswerQuestionRecordRepository answerQuestionRecordRepository;

    /**
     * 获取题库
     *
     * @return
     * @throws NotFoundException
     */
    @GetMapping("questions")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityQuestions> getQuestionsAll() throws NotFoundException {
        List<ActivityQuestions> list = questionsService.getQuestionAll();

        if (null == list || list.size() == 0) {
            throw new NotFoundException("questions.answer.activity");
        }
        Collections.shuffle(list);
        return list;
    }

    /**
     * 保存用户答题分数
     *
     * @param
     */
    @PostMapping("saveAnswerQuestionRecord")
    @ResponseStatus(HttpStatus.OK)
    public AnswerQuestionRecord saveAnswerQuestionRecord(HttpServletRequest request, @RequestBody Map map) throws NotRuleException {
        String accountId = request.getAttribute("accountId").toString();
        AnswerQuestionRecord answerQuestionRecord = checkParamAndInitAnswerRecord(map);
        AnswerQuestionRecord updateAnswerQuestionRecord = answerQuestionRecordRepository.findByAccountId(accountId);

        if (null == updateAnswerQuestionRecord) {//用户是否首次答题
            answerQuestionRecord.setAccountId(accountId);
            answerQuestionRecord.setCreateTime(new Date());
            answerQuestionRecord.setUpdateTime(new Date());
            return answerQuestionRecordRepository.save(answerQuestionRecord);
        } else {
            if (updateAnswerQuestionRecord.getHighestScore() < answerQuestionRecord.getHighestScore()) {
                updateIfNewScoreHigherThanOld(answerQuestionRecord, updateAnswerQuestionRecord);
            } else if (isHighestScoreEqualAndSecondsLessThanOld(answerQuestionRecord, updateAnswerQuestionRecord)) {
                updateAnswerQuestionRecord.setSeconds(answerQuestionRecord.getSeconds());
                updateAnswerQuestionRecord.setUpdateTime(new Date());
            }
            return answerQuestionRecordRepository.save(updateAnswerQuestionRecord);
        }
    }

    private boolean isHighestScoreEqualAndSecondsLessThanOld(AnswerQuestionRecord answerQuestionRecord, AnswerQuestionRecord updateAnswerQuestionRecord) {
        return updateAnswerQuestionRecord.getHighestScore() == answerQuestionRecord.getHighestScore() && updateAnswerQuestionRecord.getSeconds() < answerQuestionRecord.getSeconds();
    }

    private void updateIfNewScoreHigherThanOld(AnswerQuestionRecord answerQuestionRecord, AnswerQuestionRecord updateAnswerQuestionRecord) {
        updateAnswerQuestionRecord.setHighestScore(answerQuestionRecord.getHighestScore());
        updateAnswerQuestionRecord.setSeconds(answerQuestionRecord.getSeconds());
        updateAnswerQuestionRecord.setUpdateTime(new Date());
    }

    private AnswerQuestionRecord checkParamAndInitAnswerRecord(Map map) throws NotRuleException {
        AnswerQuestionRecord answerQuestionRecord = new AnswerQuestionRecord();
        if (StringUtils.isEmpty(map)) {
            throw new NotRuleException("answer.record.requestNull");
        }
        String highestScore = MapUtils.getString(map, "highestScore");
        if (StringUtils.isEmpty(highestScore)) {
            throw new NotRuleException("answer.record.highestScore");
        }
        answerQuestionRecord.setHighestScore(Integer.parseInt(highestScore));
        return answerQuestionRecord;
    }

}
