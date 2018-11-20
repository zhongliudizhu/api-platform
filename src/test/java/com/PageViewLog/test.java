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
        System.out.println(AESUtil.decrypt("A1DAC05B97DB91D5EC033723C2BC05DAE826DCA97071B71B46B24B8F09F7FFB0",AESUtil.dekey));
       System.out.println(AESUtil.decrypt("1AF29CA6C6254DD2DE97901AE8EAF57432A8588539E91F6480231646D50BB8D0",AESUtil.dekey));
        System.out.println(AESUtil.decrypt("2F3023571098DD08A565FDAC2D8B3E68EDD312239AB0A8027CF3D38962D3566B",AESUtil.dekey));
    }
}
