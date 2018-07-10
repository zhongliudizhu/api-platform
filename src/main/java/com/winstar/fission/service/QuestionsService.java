package com.winstar.fission.service;

import com.winstar.fission.entity.ActivityQuestions;
import com.winstar.fission.repository.ActivityQuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bianjiang on 2017/2/9
 */
@Service
public class QuestionsService {

    @Autowired
    ActivityQuestionsRepository activityQuestionsRepository;

    @Cacheable(cacheNames = "ACTIVITY_QUESTION", keyGenerator = "tkKeyGenerator")
    public List<ActivityQuestions> getQuestionAll(){
        List<ActivityQuestions> list = activityQuestionsRepository.findAll();
        return list;
    }

}
