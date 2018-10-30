package com.winstar.obu.repository;

import com.winstar.obu.entity.ObuConfig;
import com.winstar.obu.entity.ObuInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ObuConfigRepository
 *
 * @author: Big BB
 * @create 2018-07-05 16:59
 * @DESCRIPTION:
 **/
public interface ObuConfigRepository extends JpaRepository<ObuConfig, String> {
    ObuConfig findByType(Integer type);
}
