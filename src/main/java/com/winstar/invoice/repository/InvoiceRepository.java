package com.winstar.invoice.repository;

import com.winstar.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author shoo on 2017/10/23 15:14.
 *  开具发票
 */
public interface InvoiceRepository extends JpaRepository<Invoice,String> {

    Invoice findByOrderNumber(String orderNumber);



}
