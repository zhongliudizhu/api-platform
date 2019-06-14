package com.winstar.order.utils;


import com.alibaba.fastjson.JSON;
import com.winstar.cashier.construction.utils.Arith;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.vo.OilDetailVo;
import com.winstar.shop.entity.Goods;
import com.winstar.user.utils.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
     * 生成汽车生活订单序列号
     *
     * @return 订单序列号
     */
    public static String getCarLifeSerialNumber(){
        int r1=getRandomNum(5);
        int r2 = getRandomNum(5);
        String serialNumber = DateUtil.DateToString(new Date(), "yyyyMMddHHmmss");
        serialNumber = serialNumber + "wxcar"+ String.valueOf(r1) + String.valueOf(r2);
        return serialNumber;
    }

    /**
     * 根据商品填充订单
     * @param order 订单
     * @return result
     */
    public static OilOrder initOrder(OilOrder order, Goods goods, Integer activityType){

        if(activityType == ActivityIdEnum.ACTIVITY_ID_1.getActivity()){
            if(goods.getId().equals(Constant.ONE_BUY_ITEMID)){
                order.setPayPrice(goods.getSaledPrice());
            }else{
                order.setPayPrice(Arith.mul(goods.getSaledPrice(),goods.getDisCount()));
            }
        }else if(activityType == ActivityIdEnum.ACTIVITY_ID_2.getActivity()){
            order.setPayPrice(Arith.sub(goods.getSaledPrice(),order.getDiscountAmount()));
            order.setCouponTempletId(goods.getCouponTempletId());
        }else if(activityType == ActivityIdEnum.ACTIVITY_ID_666.getActivity()){
            order.setPayPrice(Arith.sub(goods.getSaledPrice(),order.getDiscountAmount()));
            order.setCouponTempletId(goods.getCouponTempletId());
        }

        order.setSalePrice(goods.getSaledPrice());
        order.setItemTotalValue(goods.getPrice());//油劵总面值
        order.setOilDetail(getOilDetail(goods));
        return order;
    }

    /**
     * 周四秒杀日
     * @param order
     * @param goods
     * @param activityType
     * @return
     */
    public static OilOrder initOrderSecKill(OilOrder order, Goods goods, Integer activityType){

        order.setPayPrice(Arith.mul(goods.getSaledPrice(),goods.getDisCount()));
        order.setSalePrice(goods.getSaledPrice());
        order.setItemTotalValue(goods.getPrice());//油劵总面值
        order.setOilDetail(getOilDetail(goods));
        return order;
    }


    public static OilOrder initOrderSubsidy(OilOrder order, Goods goods, Integer activityType){

        order.setPayPrice(Arith.sub(goods.getSaledPrice(),order.getDiscountAmount()));
        order.setCouponTempletId(goods.getCouponTempletId());
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
        StringBuilder sb = new StringBuilder();
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
            for (OilOrder oilOrder:oilOrders) {
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }

    /*
   * 判断用户是否能参加周四秒杀活动  0 可以购买   1 已购买  2 有未关闭订单
   * */
    public static String judgeActivitySecKill(String accountId, String activityId){
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndActivityId(accountId, activityId,DateUtil.getWeekBegin(), DateUtil.getWeekEnd());
        if(oilOrders.size()<1){
            return "0";

        }else{
            for (OilOrder oilOrder:oilOrders) {
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }
    /*
  * 判断用户是否能参加106  0 可以购买   1 已购买  2 有未关闭订单
  * */
    public static String judgeActivity2(String accountId, String activityId){
        Date begin =  DateUtil.getInputDate("2018-09-29 00:00:01");
        Date end = DateUtil.getInputDate("2019-12-31 00:00:01");
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndActivityId(accountId, activityId,begin, end);
        if(oilOrders.size()<1){
            return "0";
        }else{
            for (OilOrder oilOrder:oilOrders) {
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }

    /*
       判断用户是否能购买锦鲤活动0 可以购买   1 已购买  2 有未关闭订单
     */
    public static String BrocadeCarp(String accountId, String activityId){
        Date begin =  DateUtil.getInputDate("2019-03-01 00:00:01");
        Date end = DateUtil.getInputDate("2019-04-01 00:00:01");
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndActivityId(accountId, activityId,begin, end);
        if(oilOrders.size()<1){
            return "0";
        }else{
            for (OilOrder oilOrder:oilOrders) {
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }

    /**
     *判断用户是否能购买109活动 0可以购买 1已购买 2有未关闭订单(300元商品)
     */
    public static String couponsPackage300(String accountId,String activityId){
        //面额300
        double figure=300;
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByAccountIdAndActivityIdAndItemTotalValue(accountId,activityId,figure);
        if(oilOrders.size()<1){
            return "0";
        }else {
            for (OilOrder oilOrder:oilOrders){
                if(oilOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }
    /**
     *判断用户是否能购买109活动 0可以购买 1已购买 2有未关闭订单(100元商品)
     */
    public static String couponsPackage100(String accountId,String activityId){
        //面额100
        double denomination=100;
        List<OilOrder> oilOrdersList = ServiceManager.oilOrderRepository.findByAccountIdAndActivityIdAndItemTotalValue(accountId,activityId,denomination);
        if(oilOrdersList.size()<2){
            return "0";
        }else {
            for (OilOrder oilOrder:oilOrdersList){
                if (oilOrder.getPayStatus()!=1){
                    return "2";
                }
            }
            return "1";
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
        return time.after(DateUtil.getDayHour(7)) && time.before(DateUtil.getDayEnd());
    }


    /*
    * 判断商品数量是否超过一定数量  超过  false
    * */
    public static boolean judgeOneDay(String itemId,Integer amount){
        List<OilOrder> oilOrders =  ServiceManager.oilOrderRepository.findByIsAvailableAndItemIdAndCreateTime(Constant.IS_NORMAL_NORMAL,itemId,DateUtil.getDayBegin(),DateUtil.getDayEnd());
        return oilOrders.size() <= amount;
    }

    /*
    * 查询商品数量
    * */
    public static Integer getSoldAmount( String itemId ,Date startDate, Date endDate ){
        return  ServiceManager.oilOrderRepository.findByItemId(itemId, startDate,endDate).size();
    }

    /**
     * 订单使用优惠券的处理
     * 2019-05-31
     * zl add
     */
    public static OilOrder orderUseCoupons(AccountCouponRepository accountCouponRepository, OilOrder oilOrder, Goods goods, Integer activityType, String couponId) throws NotFoundException, NotRuleException{
        logger.info(oilOrder.getSerialNumber() + "使用优惠券" +  couponId);
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndCouponIdIn(oilOrder.getAccountId(), couponId.split(","));
        if(ObjectUtils.isEmpty(accountCoupons)){
            logger.error("根据couponId查询优惠券失败，couponId：" + couponId);
            throw new NotFoundException("coupon");
        }
        if(accountCoupons.size() != couponId.split(",").length){
            logger.error("优惠券中有查询不到的优惠券，couponId：" + couponId);
            throw new NotRuleException("coupon_have_invalid.order");
        }
        logger.info(JSON.toJSONString(accountCoupons));
        List<AccountCoupon> normalCoupons = accountCoupons.stream().filter(s -> !s.getState().equals(AccountCouponService.NORMAL)).collect(Collectors.toList());
        if(normalCoupons.size() > 0){
            logger.info("优惠券有非正常的优惠券，非正常优惠券：" + JSON.toJSONString(normalCoupons));
            throw new NotRuleException("coupon_not_use.order");
        }
        ResponseEntity<Map> resp = AccountCouponService.checkCoupon(couponId, goods.getPrice().toString(), goods.getTags());
        logger.info("map:" + resp.getBody().toString());
        Map map = resp.getBody();
        if (!"SUCCESS".equals(map.get("code"))) {
            logger.error("优惠券不可用！");
            throw new NotRuleException("coupon_not_use.order");
        }
        Double discountAmount = accountCoupons.stream().mapToDouble(AccountCoupon::getAmount).sum();
        if(!ObjectUtils.isEmpty(goods.getCouponLimitAmount()) && discountAmount > goods.getCouponLimitAmount()){
            logger.error("优惠金额大于限制！");
            throw new NotRuleException("priceLimit.order");
        }
        oilOrder.setCouponId(couponId);
        oilOrder.setDiscountAmount(discountAmount);
        oilOrder = initOrder(oilOrder, goods, activityType);
        if(oilOrder.getPayPrice() <= 0){
            logger.error("优惠后的订单价格不能小于0！");
            throw new NotRuleException("price.order");
        }
        return oilOrder;
    }

}
