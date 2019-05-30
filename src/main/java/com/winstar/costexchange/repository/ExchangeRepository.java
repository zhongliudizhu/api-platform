package com.winstar.costexchange.repository;

import com.winstar.costexchange.entity.ExchangeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by zl on 2019/5/22
 */
public interface ExchangeRepository extends JpaRepository<ExchangeRecord, String> {

    List<ExchangeRecord> findByMobileAndStateAndCreatedAtBetweenOrderByCreatedAtDesc(String mobile, String state, Date beginTime, Date endTime);

    ExchangeRecord findByOrderNumber(String orderNumber);

    List<ExchangeRecord> findByMobileAndTemplateIdAndStateOrderByCreatedAtDesc(String mobile, String templateId, String state);

    Page<ExchangeRecord> findByAccountId(String accountId, Pageable pageable);

}
