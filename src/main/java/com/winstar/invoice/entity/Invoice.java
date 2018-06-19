package com.winstar.invoice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shoo on 2017/10/23 14:01.
 * 发票
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_winstar_invoice")
public class Invoice {
    /*
    *主键
    * */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 50)
    private String id;
    /**
     * 用户Id
     */
    @Column(length = 50)
    private String accountId;

    /**
     * 发票金额
     */
    private Double price;

    /**
     * 类型 1 ：个人 2：单位
     */
    private Integer type;

    /**
     * 姓名
     */
    @Column(length = 50)
    private String name;

    /**
     * 油品种类
     */
    @Column(length = 5)
    private String oilType;

    /**
     * 邮箱
     */
    @Column(length = 30)
    private String email;
    /**
     * 手机
     */
    @Column(length = 80)
    private String phone;
    /**
     * 公司
     */
    @Column(length = 80)
    private String companyName;
    /**
     * 纳税人识别号
     */
    @Column(length = 80)
    private String taxpayerNumber;
    /**
     * 审请时间
     */
    private Date createDate;
    /**
     * 开票时间
     */
    private Date updateDate;

    /*
  * 公司地址
  * */
    @Column(length = 200)
    private String companyAddress;
    /*
    * 电话
    * */
    @Column(length = 50)
    private String telephone;
    /*
    * 开户银行
    * */
    @Column(length = 50)
    private String depositBank;
    /*
    * 银行账号
    * */
    @Column(length = 50)
    private String bankAccount;

    /**
     * 开票状态 0 待开 1 已开 2开票中 3失败
     */
    private Integer status;

    /*
       * 操作人
       * */
    @Column(length = 50)
    private String doPerson;

    @Column(length = 150)
    private String invoiceUrl;

}
