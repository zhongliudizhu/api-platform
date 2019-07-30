package com.winstar.costexchange.repository;

import com.winstar.costexchange.entity.FailSendRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailSendRecordRepository extends JpaRepository<FailSendRecord, String> {
}
