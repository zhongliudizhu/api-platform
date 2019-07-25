package com.winstar.costexchange.repository;

import com.winstar.costexchange.entity.MoveBusinessRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoveBusinessRecordRepository extends JpaRepository<MoveBusinessRecord, String> {
    List<MoveBusinessRecord> findByAccountId(String accountId);
}
