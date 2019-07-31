package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.ActivityResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ActivityResourceRepository extends JpaRepository<ActivityResource, String>, JpaSpecificationExecutor<ActivityResource> {

    List<ActivityResource> findByActivityTypeAndStatusOrderByTypeAsc(String activityType, String status);

    List<ActivityResource> findByStatusOrderByTypeAsc(String status);

}
