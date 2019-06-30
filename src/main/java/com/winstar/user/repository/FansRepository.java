package com.winstar.user.repository;

import com.winstar.user.entity.Fans;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by zl on 2019/6/19
 */
public interface FansRepository extends PagingAndSortingRepository<Fans, String>, JpaSpecificationExecutor<Fans> {

    Fans findByOpenid(String openid);

    List<Fans> findByOpenidIn(List<String> openids);

    List<Fans> findByGroupId(String s);


}
