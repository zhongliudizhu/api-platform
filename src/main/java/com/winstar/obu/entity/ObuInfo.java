package com.winstar.obu.entity;

import com.oracle.jrockit.jfr.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ObuInfo
 *
 * @author: Big BB
 * @create 2018-06-28 14:50
 * @DESCRIPTION:
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cbc_obu_check")
public class ObuInfo {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 核销状态(0:未使用,1:已使用,2:已失效)
     */
    private String state;
    /**
     * 序列号
     */
    private String serialNum;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String identity;
    /**
     * 手机号
     */
    private String phone;
    private String ETC;
    /**
     * 有效期
     */
    private Date endTime;

    private Date createTime;

    private String accountId;

    private Integer type;//1:赠送

}
