package com.winstar.mobile.repository;

import com.winstar.mobile.entity.ReceiveQualification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReceiveQualificationRepository extends JpaRepository<ReceiveQualification, String> {
    List<ReceiveQualification> findByPhoneAndPlatTypeAndReceiveTimeAfter(String phone,String platType, Date receiveTime);
}
