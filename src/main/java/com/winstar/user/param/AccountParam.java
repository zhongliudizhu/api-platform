package com.winstar.user.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountParam {
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
}
