package com.winstar.fission.controller;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.fission.entity.ActivityQuestions;
import com.winstar.fission.entity.AnswerQuestionRecord;
import com.winstar.fission.repository.AnswerQuestionRecordRepository;
import com.winstar.fission.service.QuestionsService;
import com.winstar.user.utils.SimpleResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 派发题库
 *
 * @author Big BB
 * @create 2017-11-20 9:57
 * 派发题库
 **/
@RestController
@RequestMapping("/api/v1/cbc/fission/activity")
public class ActivityController {


    /**
     * 校验资格
     *
     * @return
     * @throws NotFoundException
     */
    @GetMapping("checkQualified")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResult checkQualified() throws NotFoundException {

        return new SimpleResult("SUCCESS");
    }

}
