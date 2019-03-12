package com.winstar.drawActivity.repository;

import com.winstar.drawActivity.entity.DrawRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * Classname: DrawRecordRepository
 * Description: TODO
 * Date: 2019/3/12 10:21
 * author: uu
 */
public interface DrawRecordRepository extends CrudRepository<DrawRecord, String> {
     DrawRecord findByAccountId(String accountId);
}
