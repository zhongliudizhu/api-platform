package com.winstar.oil.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.utils.PriceAndNum;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.service.OilOrderService;
import com.winstar.redis.OilRedisTools;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
import com.winstar.utils.WsdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zl on 2017/12/13
 */
@Service
public class SendOilCouponService {

    private static final Logger logger = LoggerFactory.getLogger(SendOilCouponService.class);

    @Autowired
    MyOilCouponRepository myOilCouponRepository;

    @Autowired
    ShopService shopService;

    @Autowired
    OilOrderService orderService;

    @Autowired
    OilRedisTools oilRedisTools;

    public ResponseEntity checkCard(String orderNumber) throws Exception{
        logger.info(orderNumber + "，执行油卡发送操作。。。");
        if(!oilRedisTools.setIfAbsent(orderNumber, 600)){
            logger.info("600秒之内不执行同一订单的发券操作！订单号：" + orderNumber);
            throw new NotRuleException("oilCoupon.loading");
        }
        long beginTime = System.currentTimeMillis();
        logger.info("执行发券开始时间：" + beginTime);
        OilOrder oilOrder = orderService.getOrder(orderNumber);
        String shopId = oilOrder.getItemId();
        String accountId = oilOrder.getAccountId();
        logger.info("orderId:" + orderNumber + "，shopId:" + shopId + "，accountId:" + accountId);
        if(WsdUtils.isEmpty(shopId)){
            logger.info("shopId为空");
            throw new MissingParameterException("shopId");
        }
        if(WsdUtils.isEmpty(orderNumber)){
            logger.info("orderId为空");
            throw new MissingParameterException("orderId");
        }
        if(WsdUtils.isEmpty(accountId)){
            logger.info("accountId为空");
            throw new MissingParameterException("accountId");
        }
        //判断是否发过券
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdOrderByUseStateAsc(orderNumber);
        if(WsdUtils.isNotEmpty(myOilCoupons) && myOilCoupons.size() > 0){
            logger.info(orderNumber + "订单号已经发过券了");
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        }
        Goods shopInfo = shopService.findByGoodsId(shopId);
        if(WsdUtils.isEmpty(shopInfo)){
            throw new NotFoundException("shopInfo");
        }
        Map<String,String> resultMap = send(shopInfo,accountId,orderNumber);
        long endTime = System.currentTimeMillis();
        logger.info(orderNumber + "，执行发券结束时间：" + endTime);
        logger.info(orderNumber + "，发券消耗时间：" + (endTime - beginTime) + "ms");
        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }

    /**
     * 手动发券
     * @throws Exception
     */
    public ResponseEntity handlerSendOilCoupon(String orderNumber, String shopId, String accountId) throws Exception{
        logger.info(orderNumber + "，手动执行油卡发送操作。。。");
        if(!oilRedisTools.setIfAbsent(orderNumber, 600)){
            logger.info("600秒之内不执行同一订单的发券操作！订单号：" + orderNumber);
            throw new NotRuleException("oilCoupon.loading");
        }
        long beginTime = System.currentTimeMillis();
        logger.info("手动执行发券开始时间：" + beginTime);
        logger.info("orderId:" + orderNumber + "，shopId:" + shopId + "，accountId:" + accountId);
        if(WsdUtils.isEmpty(shopId)){
            logger.info("shopId为空");
            throw new MissingParameterException("shopId");
        }
        if(WsdUtils.isEmpty(orderNumber)){
            logger.info("orderId为空");
            throw new MissingParameterException("orderId");
        }
        if(WsdUtils.isEmpty(accountId)){
            logger.info("accountId为空");
            throw new MissingParameterException("accountId");
        }
        //判断是否发过券
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdOrderByUseStateAsc(orderNumber);
        if(WsdUtils.isNotEmpty(myOilCoupons) && myOilCoupons.size() > 0){
            logger.info(orderNumber + "订单号已经发过券了");
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        }
        Goods shopInfo = shopService.findByGoodsId(shopId);
        if(WsdUtils.isEmpty(shopInfo)){
            throw new NotFoundException("shopInfo");
        }
        Map<String,String> resultMap = send(shopInfo,accountId,orderNumber);
        long endTime = System.currentTimeMillis();
        logger.info("执行发券结束时间：" + endTime);
        logger.info("发券消耗时间：" + (endTime - beginTime) + "ms");
        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }

    /**
     * 发卡
     */
    @Transactional
    private Map<String,String> send(Goods goods,String accountId,String orderId) {
        Map<String,String> map = Maps.newHashMap();
        if(WsdUtils.isEmpty(goods)){
            logger.info("无此商品！");
            map.put("status","error");
            map.put("msg","无此商品！");
            return map;
        }
        if(WsdUtils.isEmpty(goods.getCouponDetail())){
            logger.info("商品中没有油卡！");
            map.put("status","error");
            map.put("msg","商品中没有油卡！");
            return map;
        }
        List<MyOilCoupon> myOilCoupons = new ArrayList<>();
        List<PriceAndNum> priceAndNumList = JSON.parseArray(goods.getCouponDetail(),PriceAndNum.class);
        for(PriceAndNum priceAndNum : priceAndNumList){
            for(int i=0;i<priceAndNum.getNum();i++){
                MyOilCoupon myOilCoupon = new MyOilCoupon();
                myOilCoupon.setAccountId(accountId);
                myOilCoupon.setOrderId(orderId);
                myOilCoupon.setPanAmt(priceAndNum.getPrice());
                myOilCoupon.setShopId(goods.getId());
                myOilCoupon.setShopPrice(goods.getPrice());
                myOilCoupon.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                myOilCoupon.setUseState("0");
                myOilCoupon.setSendState("0");
                myOilCoupons.add(myOilCoupon);
            }
        }
        logger.info(orderId + "订单对应的商品id：" + goods.getId() + ",商品详情：" + goods.getCouponDetail() + ", 集合条数：" + myOilCoupons.size());
        myOilCouponRepository.save(myOilCoupons);
        logger.info(orderId + "油卡发卡成功！");
        map.put("status","OK");
        return map;
    }

}
