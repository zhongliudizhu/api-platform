package com.winstar.invoice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 名称： InvoiceItem
 * 作者： sky
 * 日期： 2018-03-14 15:10
 * 描述：
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_winstar_invoice_item")
public class InvoiceItem {

    /*
  *主键
  * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;

    private String invoiceId;
    /**
     * 油券ID
     */
    private String oilId;
    /**
     * 用户Id
     */
    private String accountId;

    /**
     * 售价(计算后)
     */
    private Double salePrice;

}
