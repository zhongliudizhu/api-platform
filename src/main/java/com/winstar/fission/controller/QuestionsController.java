package com.winstar.fission.controller;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.fission.entity.AnswerQuestionRecord;
import com.winstar.fission.entity.ActivityQuestions;
import com.winstar.fission.entity.UserMileage;
import com.winstar.fission.repository.ActivityQuestionsRepository;
import com.winstar.fission.repository.AnswerQuestionRecordRepository;
import com.winstar.fission.repository.MileageLogRepository;
import com.winstar.fission.repository.UserMileageRepository;
import com.winstar.fission.service.CommonService;
import com.winstar.fission.service.QuestionsService;
import com.winstar.user.utils.ServiceManager;
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
@RequestMapping("/api/v1/cbc/fission/answer")
public class QuestionsController {

    @Autowired
    QuestionsService questionsService;
    @Autowired
    AnswerQuestionRecordRepository answerQuestionRecordRepository;
    @Autowired
    ActivityQuestionsRepository activityQuestionsRepository;
    @Autowired
    CommonService commonService;
    @Autowired
    UserMileageRepository userMileageRepository;
    @Autowired
    MileageLogRepository mileageLogRepository;

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
        String accountId = ServiceManager.accountService.getAccountId(request);
        checkParam(map);

        AnswerQuestionRecord updateAnswerQuestionRecord = answerQuestionRecordRepository.findByAccountId(accountId);

        String highestScore = MapUtils.getString(map, "highestScore");
        int highestScoreVal = Integer.valueOf(highestScore);

        boolean isFinished = highestScoreVal >= activityQuestionsRepository.countByActivityId(commonService.getActivityIdFission());

        if (null == updateAnswerQuestionRecord) {//用户是否首次答题
            updateAnswerQuestionRecord = initAnswerQuestionRecord(accountId, highestScoreVal);
            if (isFinished) {
                updateUserMileage(updateAnswerQuestionRecord);
            }
            return answerQuestionRecordRepository.save(updateAnswerQuestionRecord);
        }

        if (updateAnswerQuestionRecord.getHighestScore() < highestScoreVal) {
            updateIfNewScoreHigherThanOld(updateAnswerQuestionRecord, highestScoreVal);
        }

        if (isFinished) {
            updateUserMileage(updateAnswerQuestionRecord);
        }
        return answerQuestionRecordRepository.save(updateAnswerQuestionRecord);
    }

    void updateUserMileage(AnswerQuestionRecord answerQuestionRecord) {
        //todo 更新里程数

        answerQuestionRecord.setIsReceived(2);
        answerQuestionRecord.setUpdateTime(new Date());
        answerQuestionRecordRepository.save(answerQuestionRecord);
    }

    private AnswerQuestionRecord initAnswerQuestionRecord(String accountId, int highestScoreVal) {
        AnswerQuestionRecord updateAnswerQuestionRecord;
        updateAnswerQuestionRecord = new AnswerQuestionRecord();
        updateAnswerQuestionRecord.setHighestScore(highestScoreVal);
        updateAnswerQuestionRecord.setActivityId(commonService.getActivityIdFission());
        updateAnswerQuestionRecord.setAccountId(accountId);
        updateAnswerQuestionRecord.setCreateTime(new Date());
        updateAnswerQuestionRecord.setUpdateTime(new Date());
        updateAnswerQuestionRecord.setEndTime(new Date());
        updateAnswerQuestionRecord.setIsReceived(1);
        return updateAnswerQuestionRecord;
    }

    private void updateIfNewScoreHigherThanOld(AnswerQuestionRecord answerQuestionRecord, int highestScore) {

        answerQuestionRecord.setHighestScore(highestScore);
        answerQuestionRecord.setUpdateTime(new Date());
    }

    private void checkParam(Map map) throws NotRuleException {
        if (StringUtils.isEmpty(map)) {
            throw new NotRuleException("answer.record.requestNull");
        }
        String highestScore = MapUtils.getString(map, "highestScore");
        if (StringUtils.isEmpty(highestScore)) {
            throw new NotRuleException("answer.record.highestScore");
        }
    }

}
