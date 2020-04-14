package com.winstar.mobile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zl on 2020/3/6
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenResponseDomain {

    /**
     * 应用id
     */
    private String clientId;

    /**
     * token
     */
    private String accessToken;

    /**
     * 有效时间（秒）
     */
    private String expiresTime;

}
