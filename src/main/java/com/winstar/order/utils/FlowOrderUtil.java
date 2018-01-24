package com.winstar.order.utils;


import com.winstar.cashier.construction.utils.Arith;
import com.winstar.order.entity.FlowOrder;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.vo.FlowResult;
import com.winstar.shop.entity.Goods;
import com.winstar.user.utils.ServiceManager;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author shoo on 2017/7/7 14:44.
 *         --  --
 */
public class FlowOrderUtil {
    public static final Logger logger = LoggerFactory.getLogger(FlowOrderUtil.class);


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
        serialNumber = serialNumber + "wxfl"+ String.valueOf(r1) + String.valueOf(r2);
        return serialNumber;
    }


    /**
     * 根据商品填充订单
     * @param order 订单
     * @return result
     */
    public static FlowOrder initFlowOrder(FlowOrder order, Goods goods){

        if(goods.getType()==1){//折上折
            order.setPayPrice(Arith.mul(goods.getSaledPrice(),goods.getDisCount()));
        }else if(goods.getType()==2){//优惠券
            order.setPayPrice(Arith.sub(goods.getSaledPrice(),order.getDiscountAmount()));
            order.setCouponTempletId(goods.getCouponTempletId());
        }
        order.setSalePrice(goods.getSaledPrice());
        order.setItemTotalValue(goods.getPrice());//油劵总面值
        order.setOilDetail("1G");
        //order.setOilDetail(getOilDetail(goods));
        return order;
    }




    /*
    * 判断用户是否能参加活动  0 可以购买   1 已购买  2 有未关闭订单
    * */
    public static String judgeItemId(String accountId, String itemId){
        List<FlowOrder> flowOrders = ServiceManager.flowOrderRepository.findByAccountIdAndItemId(accountId, itemId,DateUtil.getMonthBegin(), DateUtil.getMonthEnd() );
        if(flowOrders.size()<1){
            return "0";

        }else{
            for (FlowOrder flowOrder:flowOrders
                 ) {
                if(flowOrder.getPayStatus()==1){
                    return "1";
                }
            }
            return "2";
        }
    }

    public static FlowResult chargeFlow(String phoneNo, String prvFlag, String prdSize, String prdRange, String dPrice, String flowUrl){
        FlowResult result = null;
        //String flowUrl = "http://192.168.118.7:2300/api/v1/flow/order";
        RestTemplate restTemplate = new RestTemplate();
        String authString = "winstar" + ":" + "123456";
        HttpHeaders headers = setHeaders(authString);

        Map<String ,String> param = new LinkedHashMap<>();
        param.put("PhoneNo",phoneNo);
        param.put("PrvFlag",prvFlag);
        param.put("PrdSize",prdSize);
        param.put("PrdRange",prdRange);
        param.put("DPrice",dPrice);
        JSONObject jsonObject = JSONObject.fromObject(param);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonObject.toString(), headers);
        try {
            result = restTemplate.postForObject(flowUrl, formEntity, FlowResult.class);
        }catch (Exception ex){
            logger.error("流量充值失败,手机号" + phoneNo);
        }
        return result;
    }

    /**
     * 设置headers
     *
     * @param userAndPassword 验证信息
     * @return HttpHeaders
     */
    public static HttpHeaders setHeaders(String userAndPassword){
        if(StringUtils.isEmpty(userAndPassword)){
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            return headers;
        }
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        byte[] authEncBytes = Base64Utils.encode(userAndPassword.getBytes());
        String authStringEnc = new String(authEncBytes);
        headers.add("Authorization", "Basic " + authStringEnc);
        return headers;
    }


}
