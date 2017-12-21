package com.winstar.oil.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.utils.PriceAndNum;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
import com.winstar.utils.WsdUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity checkCard(@RequestBody Map map) throws Exception{
        logger.info("执行油卡发送操作。。。");
        String shopId = MapUtils.getString(map,"shopId");
        String orderId = MapUtils.getString(map,"orderId");
        String accountId = MapUtils.getString(map,"accountId");
        if(WsdUtils.isEmpty(shopId)){
            logger.info("shopId为空");
            throw new MissingParameterException("shopId");
        }
        if(WsdUtils.isEmpty(orderId)){
            logger.info("orderId为空");
            throw new MissingParameterException("orderId");
        }
        if(WsdUtils.isEmpty(accountId)){
            logger.info("accountId为空");
            throw new MissingParameterException("accountId");
        }
        //判断是否发过券
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdOrderByUseStateAsc(orderId);
        if(WsdUtils.isNotEmpty(myOilCoupons) && myOilCoupons.size() > 0){
            logger.info(orderId + "订单号已经发过券了");
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        }
        Goods shopInfo = shopService.findByGoodsId(shopId);
        if(WsdUtils.isEmpty(shopInfo)){
            throw new NotFoundException("shopInfo");
        }
        Map<String,String> resultMap = send(shopInfo,accountId,orderId);
        return new ResponseEntity<>(resultMap,HttpStatus.OK);
    }

    /**
     * 发卡
     */
    private Map<String,String> send(Goods goods,String accountId,String orderId) throws Exception{
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
                if(goods.getId().equals("8")){
                    myOilCoupon.setOpenDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    myOilCoupon.setEndDate(WsdUtils.getLastDayOfMonth());
                }
                myOilCoupons.add(myOilCoupon);
            }
        }
        myOilCouponRepository.save(myOilCoupons);
        logger.info("油卡发卡成功！");
        map.put("status","OK");
        return map;
    }

}
