package com.winstar.mobile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mobile_receive_qualification")
public class ReceiveQualification {


    /**
     * 主键Id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(columnDefinition = "varchar(50) COMMENT '主键id'")
    private String id;

    /**
     * 领取手机号
     */
    @Column(columnDefinition = "varchar(30) comment '领取手机号'")
    private String phone;

    /**
     * 领取时间
     */
    @Column(columnDefinition = "datetime comment '领取时间'")
    private Date receiveTime;


    /**
     * 活动渠道(mobile,bank)
     */
    @Column(columnDefinition = "varchar(15) comment '活动渠道'")
    private String platType;

}
