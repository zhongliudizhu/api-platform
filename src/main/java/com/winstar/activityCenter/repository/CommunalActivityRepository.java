package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.CommunalActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author UU
 * @Classname CommunalActivityRepository
 * @Description TODO
 * @Date 2019/7/2 11:34
 */
public interface CommunalActivityRepository extends JpaRepository<CommunalActivity, String> {

    List<CommunalActivity> findAllByStatusAndDelAndShowDateBefore(String status, String del, Date showDate);

    CommunalActivity findByStatusAndDelAndShowDateBeforeAndOnlyNew(String status, String del, Date showDate, String onlyNew);
}
