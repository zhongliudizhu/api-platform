package com.winstar.carActivity.repository;

import com.winstar.carActivity.entity.ActivityResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ActivityResourceRepository extends JpaRepository<ActivityResource, String>, JpaSpecificationExecutor<ActivityResource> {
}
