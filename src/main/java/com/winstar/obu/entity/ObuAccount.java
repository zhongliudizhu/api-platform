package com.winstar.obu.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CBC_OBU_ACCOUNT")
public class ObuAccount {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;
    /**
     * 用户来源 1：优驾行 2：建行公众号  3：建行APP
     */
    private String regFrom;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date updateTime;
}
