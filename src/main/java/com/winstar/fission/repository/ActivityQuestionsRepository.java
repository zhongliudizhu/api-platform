package com.winstar.fission.repository;

import com.winstar.fission.entity.ActivityQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityQuestionsRepository extends JpaRepository<ActivityQuestions, Integer> {
    int countByActivityId(String activityId);
}
