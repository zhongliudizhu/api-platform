package com.winstar.breakfast.repository;

import com.winstar.breakfast.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zl on 2020/1/6
 */
@Repository(value = "breakfastAccountRepository")
public interface AccountRepository extends JpaRepository<Account, String> {
}
