package com.winstar.invoice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by qyc on 2018/6/20.
 */
@Entity
@Table(name = "cbc_invoice_stock_switch")
public class InvoiceStockSwitch {
    @Transient
    public static final String STATUS_ON = "0";

    /*
   *主键
   * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 开启关闭按钮 0开启 1关闭
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
