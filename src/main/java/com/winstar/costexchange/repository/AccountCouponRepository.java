package com.winstar.costexchange.repository;

import com.winstar.costexchange.entity.AccountCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zl on 2019/5/24
 */
public interface AccountCouponRepository extends JpaRepository<AccountCoupon, String> {

    Page<AccountCoupon> findByAccountIdAndShowStatusAndState(String accountId, String showStatus, String state, Pageable pageable);

}
