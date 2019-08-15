package com.winstar.activityCenter.repository;

import com.winstar.activityCenter.entity.InstallmentWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface InstallmentRepository extends JpaRepository<InstallmentWhitelist, String>, JpaSpecificationExecutor<InstallmentWhitelist> {
    List<InstallmentWhitelist> findByPhoneAndValidTimeAfter(String phone,Date date);
}
