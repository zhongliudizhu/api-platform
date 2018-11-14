package com.PageViewLog;

import com.winstar.utils.AESUtil;

/**
 * 名称 test
 * 作者 dpw
 * 日期 2018/11/7 14:31
 * 项目 winstar-cbc-platform-api
 * 描述
 */
public class test {
    public static void main(String[] args)throws Exception{
       System.out.println(AESUtil.decrypt("E7B7F1EFD1DB0CD11D43A5C92E991F7C8FB1E593DAE063583B7B8EF7052A2C8A",AESUtil.dekey));
        System.out.println(AESUtil.decrypt("BBEF0F8EA1A73FCAFEA3746D858424784A407A6ADA85A59DEB860376F92E6120",AESUtil.dekey));
    }
}
