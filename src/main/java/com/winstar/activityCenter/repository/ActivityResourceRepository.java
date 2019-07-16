package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.ActivityResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ActivityResourceRepository extends JpaRepository<ActivityResource, String>, JpaSpecificationExecutor<ActivityResource> {
}
