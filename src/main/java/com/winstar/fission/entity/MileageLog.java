package com.winstar.fission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qyc on 2017/12/19.
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "cbc_mileage_log")
public class MileageLog {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;

    /**
     * 邀请人accountId
     */
    @Column(length = 50)
    private String accountId;

    /**
     * 事件信息
     */
    @Column(length = 50)
    private String eventMsg;

    /**
     * 里程数
     */
    private Integer mileage;

    /**
     * 创建时间
     */
    private Date createTime;
}
