package com.winstar.order.utils;


import com.winstar.order.entity.OilOrder;
import com.winstar.order.vo.OilDetailVo;
import com.winstar.shop.entity.Goods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author shoo on 2017/7/7 14:44.
 *         --  --
 */
public class OilOrderUtil {
    public static final Logger logger = LoggerFactory.getLogger(OilOrderUtil.class);


    /**
     * 随机生成6位数
     * @Function:geRandomt
     * @Description:随机生成6位数
     * @return 6位数
     * @exception/throws  description
     */
    public static int getRandomNum(int dig) {
        int[] array = {1,2,3,4,5,6,7,8,9};
        Random rand = new Random();
        for (int i = 9; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for(int i = 0; i < dig; i++) {
            result = result * 10 + array[i];
        }
        return result;
    }

    /**
     * 生成订单序列号
     *
     * @return 订单序列号
     */
    public static String getSerialNumber(){
        int r1=getRandomNum(5);
        int r2 = getRandomNum(5);
        String serialNumber = DateUtil.DateToString(new Date(), "yyyyMMddHHmmss");
        serialNumber = serialNumber + "yj"+ String.valueOf(r1) + String.valueOf(r2);
        return serialNumber;
    }




    /**
     * 更新优惠券（即使用）
     * @param couponId 优惠券id
     * @param consumeCouponUrl url
     * @return 优惠券
     */





    /**
     * 发放优惠券
     * @param map 所需参数
     * @param giveCouponUrl url
     * @return
     */



    /**
     * 根据商品填充订单
     * @param order 订单
     * @return result
     */
    public static OilOrder initOrder(OilOrder order, Goods goods, Integer activityType){

        if(activityType==1){
            order.setPayPrice(goods.getSaledPrice()*goods.getDisCount());
            if(order.getPayPrice() < 0){
                order.setPayPrice(0.1);
            }
        }else if(activityType==2){
            order.setPayPrice(goods.getSaledPrice());
            order.setCouponTempletId(goods.getCouponTempletId());
        }
        order.setSalePrice(goods.getSaledPrice());
        order.setItemTotalValue(goods.getPrice());//油劵总面值
        order.setOilDetail(getOilDetail(goods));
        return order;
    }

    /**
     * 根据商品信息拼接油券详情  如：100*1+50*2+200*5
     * @return 油券详情字符串
     */
    private static String getOilDetail(Goods goods){
        StringBuilder sb = new StringBuilder("");
        List<OilDetailVo> oils = StringFormatUtils.jsonStr2List(goods.getCouponDetail(),OilDetailVo.class);
        for (OilDetailVo vo:oils
             ) {
            sb.append(vo.getPrice()).append("元x").append(vo.getNum()).append("张+");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }


    /**
     * accountId 查询Account
     * @param accountId accountId
     * @return  Account  账户信息
     */


    /**
     * 查询账户当天促销订单数量
     * @param accountId 账户id
     * @return 订单数量
     */


}
