package com.winstar.oil.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zl on 2018/9/19
 */
@Entity
@Getter
@Setter
public class OilCouponVerificationLog {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 通知券码
     */
    private String pan;

    /**
     * 通知时间
     */
    private Date createTime;

    /**
     * 1 核销 2 撤销
     */
    private int type;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求结果数据
     */
    private String backData;

}
