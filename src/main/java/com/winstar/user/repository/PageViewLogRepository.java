package com.winstar.user.repository;

import com.winstar.user.entity.PageViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewLogRepository extends JpaRepository<PageViewLog, String> {

}
