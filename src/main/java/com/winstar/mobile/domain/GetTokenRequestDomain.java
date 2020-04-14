package com.winstar.mobile.domain;

import com.winstar.mobile.config.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zl on 2020/3/6
 */
@Getter
@Setter
@AllArgsConstructor
public class GetTokenRequestDomain {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 应用密钥
     */
    private String clientSecret;

    public GetTokenRequestDomain(){
        this.userId = Config.userId;
        this.clientId = Config.clientId;
        this.clientSecret = Config.clientSecret;
    }

}
