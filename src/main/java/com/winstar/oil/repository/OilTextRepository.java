package com.winstar.oil.repository;

import com.winstar.oil.entity.OilText;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by qyc on 2017/12/19.
 */
public interface OilTextRepository extends JpaRepository<OilText,String> {
    OilText findByType(String type);
}
