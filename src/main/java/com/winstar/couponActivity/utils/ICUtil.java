package com.winstar.couponActivity.utils;

/**
 * ICUtil
 *
 * @author: Big BB
 * @create 2018-04-20 18:51
 * @DESCRIPTION:
 **/
public class ICUtil {
    private ICUtil() {

    }

    private static final ICUtil instance = new ICUtil();

    public static ICUtil getInstance() {
        return instance;
    }


    /**
     * 将18位身份证号转化为15位返回,非18位身份证号原值返回
     * @param identityCard 18位身份证号
     * @return 15身份证
     */
    public static String get15Ic(String identityCard) {
        String retId = "";
        if(null == identityCard){
            return retId;
        }
        if(identityCard.length() == 18){
            retId = identityCard.substring(0, 6) + identityCard.substring(8, 17);
        }else{
            return identityCard;
        }
        return retId;
    }

    /**
     * 将15位身份证号转化为18位返回，非15位身份证号原值返回
     * @param identityCard
     * @return
     */
    public String get18Ic(String identityCard) {
        String retId = "";
        String id17 = "";
        int sum = 0;
        int y = 0;
        // 定义数组存放加权因子（weight factor）
        int[] wf = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        // 定义数组存放校验码（check code）
        String[] cc = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        if (identityCard.length() != 15) {
            return identityCard;
        }
        // 加上两位年19
        id17 = identityCard.substring(0, 6) + "19" + identityCard.substring(6);
        // 十七位数字本体码加权求和
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.valueOf(id17.substring(i, i + 1)) * wf[i];
        }
        // 计算模
        y = sum % 11;
        // 通过模得到对应的校验码 cc[y]
        retId = id17 + cc[y];
        return retId;
    }
}
