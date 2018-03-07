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
     * 业务订单号
     */
    private String orderNumber;
    /**
     * 返回值 1000	返回成功
     1001	企业授权错误
     1002	流水已开票
     1003	无业务流水
     1004	接收失败，未知异常

     */
    private String result;
    /**
     * 开票时间
     */
    private Date time;
    /**
     * 发票代码
     */
    private String fpDm;
    /**
     * 发票号码
     */
    private String fpHm;
    /**
     * 业务类型 1，正常开票，2作废、3冲红
     */
    private String billingType;
    /**
     * Pdf下载路径
     */
    private String pdfUrl;
    /**
     * 发送时间
     */
    private Date sendTime;

}
