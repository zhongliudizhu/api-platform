package com.winstar.user.entity;

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
@Table(name = "CBC_USER_ACCOUNT")
public class Account {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 32)
    private String id;
    /**
     * 用户来源 1 微信
     */
    private String regFrom;
    /**
     * openid
     */
    private String openid;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像地址
     */
    private String headImgUrl;
    /**
     * 电话号码
     */
    private String mobile;
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
    /**
     * 建行信息卡
     */
    @Column(length = 50)
    private String authInfoCard;
    /**
     * 信息卡绑定手机后四位
     */
    @Column(length = 50)
    private String authMobile;
    @Column(length = 50)
    private String authDriverLicense;
    /**
     * 信息卡绑定时间
     */
    private Date authTime;
}
