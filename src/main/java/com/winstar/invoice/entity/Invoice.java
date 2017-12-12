package com.winstar.invoice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shoo on 2017/10/23 14:01.
 *  发票
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
    /*
    * 类型 0 个人  1 公司
    * */
    private Integer type;
    /*
    *姓名
    * */
    @Column(length = 50)
    private String personName;
    /*
    * 身份证号
    * */
    @Column(length = 50)
    private String identNumber;
    /*
    * 手机号
    * */
    @Column(length = 50)
    private String phoneNo;
    /*
    * 电子邮箱
    * */
    @Column(length = 50)
    private String email;
    /*
    * 公司名称
    * */
    @Column(length = 50)
    private String companyName;
    /*
    * 纳税人识别号
    * */
    @Column(length = 50)
    private String registrationNo;
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
    /*
    *  0 待开票  1 开票完成  2 信息变更 3 申请重开 4 已拒绝 5 已重开
    * */
    private Integer status;
    /*
    * 创建时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /*
    * 开票时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date invoiceTime;

    /*
    * 修改时间
    * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /*
    * 订单序列号
    * */
    @Column(length = 50)
    private String orderSerialNo;
    /*
    * 订单金额
    * */
    private Double payPrice;

    /*
    * 油券总值
    * */
    private Double oilTotalValue;
    /*
    * 是否删除  0 否  1 已删除
    * */
    @Column(length = 10)
    private String isDel;

}
