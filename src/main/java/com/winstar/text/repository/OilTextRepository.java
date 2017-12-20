package com.winstar.text.repository;

import com.winstar.text.entity.OilText;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by qyc on 2017/12/19.
 */
public interface OilTextRepository extends JpaRepository<OilText,String> {
    OilText findByType(String type);
}
