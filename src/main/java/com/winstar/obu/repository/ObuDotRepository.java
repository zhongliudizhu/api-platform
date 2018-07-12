package com.winstar.obu.repository;

import com.winstar.obu.entity.ObuDot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * ObuDotRepository
 *
 * @author: Big BB
 * @create 2018-07-02 11:05
 * @DESCRIPTION:
 **/
public interface ObuDotRepository extends JpaRepository<ObuDot, String>, JpaSpecificationExecutor<ObuDot> {
}
