package com.winstar.order.utils;


import com.winstar.cashier.construction.utils.Arith;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.vo.OilDetailVo;
import com.winstar.shop.entity.Goods;
import com.winstar.user.utils.ServiceManager;
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
        serialNumber = serialNumber + "wxyj"+ String.valueOf(r1) + String.valueOf(r2);
        return serialNumber;
    }


    /**
     * 根据商品填充订单
     * @param order 订单
     * @return result
     */
    public static OilOrder initOrder(OilOrder order, Goods goods, Integer activityType){

        if(activityType==1){
            if(goods.getId().equals(Constant.ONE_BUY_ITEMID)){
                order.setPayPrice(goods.getSaledPrice());
            }else{
                order.setPayPrice(Arith.mul(goods.getSaledPrice(),goods.getDisCount()));
            }
        }else if(activityType==2){
            order.setPayPrice(Arith.sub(goods.getSaledPrice(),order.getDiscountAmount()));
            order.setCouponTempletId(goods.getCouponTempletId());
        }else if(activityType==3){
            if(goods.getId().equals(Constant.ONE_BUY_ITEMID)){
                order.setPayPrice(goods.getSaledPrice());
            }else{
                order.setPayPrice(Arith.mul(goods.getSaledPrice(),goods.getDisCount()));
            }
        }
        order.setSalePrice(goods.getSaledPrice());
        order.setItemTotalValue(goods.getPrice());//油劵总面值
        order.setOilDetail(getOilDetail(goods));
        return order;
    }

    /**
     * 根据商品信息拼接油券详情  如：100元x1张+50元x2张+200元x5张
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

    /*
    * 用户是否能购买一分油券
    * */
    public static String isEnable(String accountId){

        if(todaySold().size()>=Constant.ONE_DAY_MAX){
            return "500";
        }
        if(thisMonth(accountId).equals("1")){
            return "1";
        }
        if(thisMonth(accountId).equals("2")){
            return "2";
        }
        return "ok";
    }
    /*
    * 判断用户是否能参加活动  0 可以购买   1 已购买  2 有未关闭订单
    * */
    public static String judgeActivity(String accountId, String activityId){
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndActivityId(accountId, activityId,DateUtil.getMonthBegin(), DateUtil.getMonthEnd() );
        if(oilOrders.size()<1){
            return "0";

        }else{
            for (OilOrder oilOrder:oilOrders
                 ) {
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }

    /*
    * 用户本月20元油券订单
    * */
    private static String thisMonth(String accountId){
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndAndItemId(accountId,Constant.ONE_BUY_ITEMID,DateUtil.getMonthBegin(),DateUtil.getMonthEnd());
        if(oilOrders.size()<=0){
            return "0";
        }else {
            for (OilOrder oilOrder:oilOrders
                    ) {
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }

    /*
    * 20元油券今天已售数量
    * */
    private static List<OilOrder> todaySold(){
       return ServiceManager.oilOrderRepository.findByIsAvailableAndItemIdAndCreateTime(Constant.IS_NORMAL_NORMAL,Constant.ONE_BUY_ITEMID,DateUtil.getDayBegin(),DateUtil.getDayEnd());
    }

    /*
    * 判断时间是否在 7:00——24:00
    * */
    private static boolean judgeTime(Date time){
        if(time.after(DateUtil.getDayHour(7)) && time.before( DateUtil.getDayEnd() )){
            return true;
        }
        return false;
    }


}
