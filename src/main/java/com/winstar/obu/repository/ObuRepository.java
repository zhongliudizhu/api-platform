package com.winstar.obu.repository;

import com.winstar.obu.entity.ObuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * ObuRepository
 *
 * @author: Big BB
 * @create 2018-06-28 15:40
 * @DESCRIPTION:
 **/
public interface ObuRepository extends JpaRepository<ObuInfo, String>, JpaSpecificationExecutor<ObuInfo>{

    List<ObuInfo> findByAccountId(String accountId);

    Long countByType(Integer type);

}
