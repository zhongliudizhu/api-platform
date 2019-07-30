package com.winstar.costexchange.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cbc_fail_send_record")
public class FailSendRecord {

    /**
     * 唯一标识
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) comment '主键id'")
    private String id;

    /**
     * 用户Id
     */
    @Column(columnDefinition = "varchar(50) comment '用户Id'")
    private String accountId;

    /**
     * 模板ID
     */
    @Column(columnDefinition = "varchar(50) comment '模板ID'")
    private String templateId;

    /**
     * 失败信息
     */
    @Column(columnDefinition = "varchar(100) comment '失败信息'")
    private String failMsg;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime comment '创建时间'")
    private Date createdAt;

}
