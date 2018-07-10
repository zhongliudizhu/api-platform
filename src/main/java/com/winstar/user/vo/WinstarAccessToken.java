package com.winstar.user.vo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import java.util.Date;

/**
 * 名称： WinstarAccessToken
 * 作者： dpw
 * 日期： 2018-07-10 10:40
 * 描述： 万盛达用户接口token信息
 **/
@Data
public class WinstarAccessToken {

    private String id;
    /**
     * 账户Id
     */
    private String accountId;
    /**
     * 令牌
     */
    private String token;
    /**
     * 账户类型:  微信小程序 0 微信公众号 1   android 2 ios 3 pc 4
     */
    private Integer source;

    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 失效时间
     */
    private Date expiredAt;
}
