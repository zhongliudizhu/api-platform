package com.winstar.costexchange.repository;

import com.winstar.costexchange.entity.ExchangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by zl on 2019/5/22
 */
public interface ExchangeRepository extends JpaRepository<ExchangeRecord, String> {

    List<ExchangeRecord> findByMobileAndStateAndCreatedAtBetween(String mobile, String state, Date beginTime, Date endTime);

    ExchangeRecord findByOrderNumber(String orderNumber);

    ExchangeRecord findByMobileAndTemplateIdAndState(String mobile, String templateId, String state);

}
