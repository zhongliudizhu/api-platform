package com.winstar.fission.repository;

import com.winstar.fission.entity.AnswerQuestionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerQuestionRecordRepository extends JpaRepository<AnswerQuestionRecord, String>, JpaSpecificationExecutor {
    AnswerQuestionRecord findByAccountId(String accountId);
}
