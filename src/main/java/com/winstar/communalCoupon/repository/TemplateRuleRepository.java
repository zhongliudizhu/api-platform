package com.winstar.communalCoupon.repository;

import com.winstar.communalCoupon.entity.TemplateRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author UU
 * @Classname TemplateRuleRepository
 * @Description TODO
 * @Date 2019/7/8 15:05
 */
public interface TemplateRuleRepository extends JpaRepository<TemplateRule, String>{
    TemplateRule findByTemplateId(String templateId);

    List<TemplateRule> findByIllegalStatus(String status);
}
