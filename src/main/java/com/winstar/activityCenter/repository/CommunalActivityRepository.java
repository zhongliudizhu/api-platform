package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.CommunalActivity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author UU
 * @Classname CommunalActivityRepository
 * @Description TODO
 * @Date 2019/7/2 11:34
 */
public interface CommunalActivityRepository extends JpaRepository<CommunalActivity, String> {
}
