package com.winstar.order.utils;


import com.winstar.order.entity.OilOrder;
import com.winstar.order.entity.OilOrderItems;
import com.winstar.order.vo.GiftsVo;
import com.winstar.order.vo.GoodsVo;
import com.winstar.order.vo.PayInfoVo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.stream.Collectors.*;

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
     * 修改订单付款信息
     * @param order 订单
     * @param payInfoVo 付款信息
     * @return result
     */
    public static OilOrder updateOrderPayInfo(OilOrder order, PayInfoVo payInfoVo){
        order.setBankSerialNo(payInfoVo.getBankSerialNumber());
        order.setPayStatus(payInfoVo.getPayState());
        order.setPayTime(payInfoVo.getBankTime());
        order.setSendStatus(3);//付款成功的默认发货成功，如果失败张林会通知
        order.setUpdateTime(new Date());
        return order;
    }

    /**
     * 查询商品信息
     * @param goodId 商品id
     * @param getGoodsUrl url
     * @return result
     */


    /**
     * 查询秒杀规则
     * @param activityId 秒杀商品id
     * @param getReceiveParamUrl url
     * @return 规则字符串
     */


    /**
     * 查询优惠券详情
     * @param couponId 优惠券id
     * @param getCouponDetailUrl url
     * @param salePrice 订单价格
     * @param type 类型  1：加油券   2：审车
     * @return 优惠券详情
     *
     */


    /**
     * 更新优惠券（即使用）
     * @param couponId 优惠券id
     * @param consumeCouponUrl url
     * @return 优惠券
     */



    /**
     * 用户是否买过油券
     * @param accountId 账户id
     * @return 没买过返回 true
     */







    /**
     * 查询秒杀规则
     * @param secKillId 秒杀商品id
     * @param getRulesUrl url
     * @return 规则字符串
     */




    /**
     * 发放优惠券
     * @param map 所需参数
     * @param giveCouponUrl url
     * @return
     */


    /**
     * 解析秒杀规则
     * @param ruleStr 规则字符串
     * @return result
     */
    public static String analysisRule(String secKillId, String accountId, String ruleStr){
        return "error";
    }

    /**
     * 根据商品填充订单
     * @param order 订单
     * @param goodsVo 商品信息
     * @return result
     */
    public static OilOrder initOrder(OilOrder order, GoodsVo goodsVo,Integer moneyEnvironment){
        if(moneyEnvironment==1){
            order.setPayPrice(0.01-order.getDiscountAmount());//测试方便，改为0.01元

        }else{
            order.setPayPrice(goodsVo.getSalePrice()-order.getDiscountAmount());//实际付款 = 商品价格 -  优惠价格
        }
        if(order.getPayPrice() < 0){
            order.setPayPrice(0.1);
        }
        order.setSalePrice(goodsVo.getSalePrice());
        order.setItemTotalValue(goodsVo.getShopPrice());//油劵总面值
        order.setOilDetail(getOilDetail(goodsVo));
        return order;
    }

    /**
     * 根据商品信息拼接油券详情  如：100*1+50*2+200*5
     * @param goodsVo 商品信息
     * @return 油券详情字符串
     */
    private static String getOilDetail(GoodsVo goodsVo){
        StringBuilder sb = new StringBuilder("");
        //找出油券
        List<GiftsVo> oils = goodsVo.getGifts().stream().filter(g -> "0".equals(g.getIsGifts())).collect(toList());
        //油券按价格分组，取数量
        Map<Integer,Integer> priceList = oils.stream().collect(groupingBy(GiftsVo::getShopPrice,summingInt(GiftsVo::getShopNumber)));
        for (Integer price:priceList.keySet()
             ) {
            sb.append(price).append("元x").append(priceList.get(price)).append("张+");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    /**
     * 根据商品填充订单项
     * @param orderItems 订单项
     * @param goodsVo 商品信息
     * @param orderSerialNo 订单序列号
     * @return result
     */
    public static List<OilOrderItems> initOilOrderItems(List<OilOrderItems> orderItems, GoodsVo goodsVo, String orderSerialNo){
        //商品
        List<GiftsVo> goodses = goodsVo.getGifts().stream().filter( g -> "0".equals(g.getIsGifts())).collect(toList());
        //赠品
        List<GiftsVo> gifts = goodsVo.getGifts().stream().filter( g -> "1".equals(g.getIsGifts())).collect(toList());
        for (GiftsVo goods:goodses
             ) {
            OilOrderItems oilTemp = new OilOrderItems();
            oilTemp.setName(goods.getShopName());
            oilTemp.setUnitPrice((double)goods.getShopPrice());
            oilTemp.setAmount(goods.getShopNumber());
            oilTemp.setItemType(Integer.valueOf(goods.getIsGifts()));
            oilTemp.setOrderSerialNo(orderSerialNo);
            oilTemp.setStatus(1);
            oilTemp.setGoodsMsg(goods.toString());
            orderItems.add(oilTemp);
        }
        for (GiftsVo gift:gifts
             ) {
            OilOrderItems oilTemp = new OilOrderItems();
            oilTemp.setName(gift.getShopName());
            oilTemp.setUnitPrice((double)gift.getShopPrice());
            oilTemp.setAmount(gift.getShopNumber());
            oilTemp.setItemType(Integer.valueOf(gift.getIsGifts()));
            oilTemp.setOrderSerialNo(orderSerialNo);
            oilTemp.setGoodsMsg(gift.toString());
            orderItems.add(oilTemp);
        }


        return orderItems;
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
