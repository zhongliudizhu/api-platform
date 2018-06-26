package com.winstar.invoice.repository;

import com.winstar.invoice.entity.InvoiceStockSwitch;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dpw on 2018/06/25.
 */
public interface InvoiceStockSwitchRepository extends JpaRepository<InvoiceStockSwitch, String> {
    InvoiceStockSwitch findByType(String type);
}
