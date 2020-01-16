package com.winstar.oil.repository;

import com.winstar.oil.entity.OilBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zl on 2020/1/16
 */
public interface OilBlackListRepository extends JpaRepository<OilBlackList, String> {

    OilBlackList findByAccountId(String accountId);

    List<OilBlackList> findByVip(String vip);

}
