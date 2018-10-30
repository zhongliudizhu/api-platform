package com.winstar.couponActivity.repository;

import com.winstar.couponActivity.entity.MileageObtainLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * CareJuanListRepository
 *
 * @author: Big BB
 * @create 2018-03-21 14:15
 * @DESCRIPTION:里程获取记录
 **/
public interface MileageObtainLogRepository extends JpaRepository<MileageObtainLog,String>,JpaSpecificationExecutor<MileageObtainLog> {

    /**
     * 根据用户id获取用户的里程数
     * @param accountId
     * @return
     */
    @Query("SELECT SUM(t.mileageNum) FROM MileageObtainLog  t WHERE t.accountId=?1 AND state='1'GROUP BY t.accountId")
    String sumConsumerMileage(String accountId);

    /**
     * 根据用户id获取该用户的里程列表
     * @param accountId
     * @return
     */
    List<MileageObtainLog>  findByAccountId(String accountId);

    /**
     * 根据用户id和获取类型得到里程
     * @param accountId
     * @param i
     * @return
     */
    List<MileageObtainLog> findByAccountIdAndOptainType(String accountId, int i);
    /**
     * 根据用户id和状态得到里程
     * @param accountId
     * @param i
     * @return
     */
    List<MileageObtainLog> findByAccountIdAndState(String accountId, int i);
    /**
     * 根据用户id和类型获取用户的里程数
     * @param accountId
     * @return
     */
    @Query("SELECT SUM(t.mileageNum) FROM MileageObtainLog  t WHERE t.accountId=?1 AND t.optainType=?2 AND state='1' GROUP BY t.accountId")
    String sumConsumerMileageByType(String accountId, Integer type);
}
