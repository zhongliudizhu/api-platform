package com.winstar.invoice.repository;

import com.winstar.invoice.entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dpw on 2018/06/25.
 */
public interface ManagementRepository extends JpaRepository<Management, String> {
    Management findByType(String type);
}
