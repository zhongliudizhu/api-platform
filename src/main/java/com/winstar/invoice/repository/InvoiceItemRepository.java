package com.winstar.invoice.repository;

import com.winstar.invoice.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 名称： InvoiceItemRepository
 * 作者： sky
 * 日期： 2018-03-14 15:17
 * 描述：
 **/
public interface InvoiceItemRepository extends JpaSpecificationExecutor<InvoiceItem>,JpaRepository<InvoiceItem,String> {

    List<InvoiceItem> findByAccountId(String accountId);
    InvoiceItem findByOilId(String oilId);
}
