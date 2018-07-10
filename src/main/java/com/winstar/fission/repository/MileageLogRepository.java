package com.winstar.fission.repository;

import com.winstar.fission.entity.MileageLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 名称： UserMileageRepository
 * 作者： dpw
 * 日期： 2018-07-10 9:38
 * 描述： UserMileageRepository
 **/
public interface MileageLogRepository extends JpaRepository<MileageLog, String> {

}
