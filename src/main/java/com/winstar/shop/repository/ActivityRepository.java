package com.winstar.shop.repository;

import com.winstar.shop.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * 名称： ActivityRepository
 * 作者： sky
 * 日期： 2017-12-12 9:40
 * 描述：
 **/
public interface ActivityRepository extends JpaSpecificationExecutor<Activity>,JpaRepository<Activity,String> {
    List<Activity>  findByTypeIn(Collection types);
}
