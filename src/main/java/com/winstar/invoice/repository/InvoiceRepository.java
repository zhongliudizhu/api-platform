package com.winstar.invoice.repository;

import com.winstar.invoice.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * @author shoo on 2017/10/23 15:14.
 *  开具发票
 */
public interface InvoiceRepository extends JpaRepository<Invoice,String> {

    List<Invoice> findByAccountId(String accountId);
    Page<Invoice> findByAccountId(String accountId,Pageable pageable);
}
