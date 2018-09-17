package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.MileageConsumLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * CareJuanListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:里程消费记录
 **/
public interface MileageConsumLogRepository extends JpaRepository<MileageConsumLog,String>,JpaSpecificationExecutor<MileageConsumLog> {
    /**
     * 获取该用户消费的总里程数
     * @param accountId
     * @return
     */
    @Query("SELECT SUM(t.mileageNum) FROM MileageConsumLog t WHERE t.accountId =?1 AND t.state = '1'GROUP BY t.accountId")
    String sumConsumerMileage(String accountId);
}
