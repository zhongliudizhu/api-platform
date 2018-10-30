package com.winstar.couponActivity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 查询日志
 * @author wanghaibo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CBC_QUERY_LOG")
public class QueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "ORDER_NUMBER", length = 50)
    private String orderNumber;

    /**
     * 访问时间
     */
    @Column(name = "CREATED_AT")
    private Date createdAt;

    /**
     * 客户端ip
     */
    @Column(name = "CUSTOMER_IP")
    private String customerIp;

    /**
     * 访问接口
     */
    @Column(name = "apply_url", length = 255)
    private String applyUrl;

    /**
     * 请求信息
     */
    @Column(name = "req_info", length = 4000)
    private String reqInfo;

    /**
     * 响应报文
     */
    @Column(name = "resp_info", length = 4000)
    private String respInfo;

    /**
     * 信息内容
     */
    @Column(name = "MESSAGE",length = 255)
    private String message;

    /**
     * 信息编码
     */
    @Column(name = "CODE",length = 32)
    private String code;

}
