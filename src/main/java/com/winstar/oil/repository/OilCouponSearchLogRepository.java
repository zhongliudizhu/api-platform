package com.winstar.oil.repository;

import com.winstar.oil.entity.OilCouponSearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 名称： OilCouponSearchLogRepository
 * 作者： sky
 * 日期： 2017-12-12 9:39
 * 描述：
 **/
public interface OilCouponSearchLogRepository extends JpaRepository<OilCouponSearchLog,String>,JpaSpecificationExecutor<OilCouponSearchLog> {

}
