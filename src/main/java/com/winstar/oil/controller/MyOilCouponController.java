package com.winstar.oil.controller;

import com.google.common.collect.Maps;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.entity.OilCoupon;
import com.winstar.oil.entity.OilCouponSearchLog;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.oil.repository.OilCouponSearchLogRepository;
import com.winstar.oil.service.OilCouponUpdateService;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WebUitl;
import com.winstar.utils.WsdUtils;
import com.winstar.vo.OilSetMealVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 名称： MyOilCouponController
 * 作者： sky
 * 日期： 2017-12-12 10:04
 * 描述： 我的加油券
 **/
@RestController
@RequestMapping("api/v1/cbc")
public class MyOilCouponController {

    private static final Logger logger = LoggerFactory.getLogger(MyOilCouponController.class);

    @Autowired
    MyOilCouponRepository myOilCouponRepository;

    @Autowired
    OilCouponSearchLogRepository oilCouponSearchLogRepository;

    @Autowired
    OilCouponRepository oilCouponRepository;

    @Autowired
    OilCouponUpdateService updateService;

    @RequestMapping(value = "/myOilSetMeal",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<OilSetMealVo> findOilSetMealList(
            @RequestParam(defaultValue = "0") Integer nextPage,
            @RequestParam(defaultValue = "1000") Integer pageSize,
            HttpServletRequest request
    ) throws Exception {
        String accountId = request.getHeader("accountId");
        if(WsdUtils.isEmpty(accountId)){
            throw new NotFoundException("accountId");
        }
        logger.info("accountId===" + accountId);
        List<OilSetMealVo> oilSetMeals = getOilSetMeals(accountId,nextPage * pageSize,pageSize);
        if(ObjectUtils.isEmpty(oilSetMeals)){
            throw new NotFoundException("oil.item");
        }
        return  oilSetMeals;
    }

    private  List<OilSetMealVo> getOilSetMeals(String accountId,Integer begin,Integer end){
        List<OilSetMealVo> oilSetMeals = new ArrayList<>();
        List<OilSetMealVo> oilSetMealsUsed = new ArrayList<>();
        List<Object[]> objects = myOilCouponRepository.findOilSetMeal(accountId,begin,end);
        for(Object[] obj : objects){
            OilSetMealVo oilSetMealVo = new OilSetMealVo();
            oilSetMealVo.setTotalNumber(Integer.parseInt(obj[0].toString()));
            oilSetMealVo.setSurplusNumber(Integer.parseInt(obj[1].toString()));
            oilSetMealVo.setSurplusPrice((Double)obj[2]);
            oilSetMealVo.setTotalPrice((Double) obj[3]);
            oilSetMealVo.setOrderId((String) obj[4]);
            oilSetMealVo.setSendState((String) obj[5]);
            oilSetMealVo.setShopName((String) obj[6]);
            //判断此套餐是否用完
            if(Integer.parseInt(obj[1].toString())==0){
                oilSetMealsUsed.add(oilSetMealVo);
            }else{
                oilSetMeals.add(oilSetMealVo);
            }
        }
        oilSetMeals.addAll(oilSetMealsUsed);
        return oilSetMeals;
    }

    @RequestMapping(value = "/myOilSetMealInfo",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyOilCoupon> findOilSetMealInfo(
            @RequestParam String orderId,
            HttpServletRequest request
    ) throws Exception {
        logger.info("orderId===" + orderId);
        if(StringUtils.isEmpty(orderId)){
            throw new NotRuleException("orderId");
        }
        String accountId = request.getHeader("accountId");
        if(WsdUtils.isEmpty(accountId)){
            throw new NotFoundException("accountId");
        }
        logger.info("accountId===" + accountId);
        List<MyOilCoupon> myOilCoupons=myOilCouponRepository.findByAccountIdAndOrderIdOrderByUseStateAscPanDesc(accountId,orderId);

        if(WsdUtils.isEmpty(myOilCoupons)){
            throw new NotFoundException("myOilCoupons");
        }
        for (MyOilCoupon myOilCoupon : myOilCoupons) {
            myOilCoupon.setShopPrice(myOilCoupon.getPanAmt());//临时赋值 （将卷价赋值给套餐价）
        }
        return getResult(myOilCoupons);
    }

    /**
     *  返回结果中不需要的字段值置空
     */
    private List<MyOilCoupon> getResult(List<MyOilCoupon> myOilCoupons) throws Exception{
        for(MyOilCoupon myOilCoupon : myOilCoupons){
            myOilCoupon.setShopId(null);
            myOilCoupon.setPan(null);
        }
        return myOilCoupons;
    }

    @RequestMapping(value = "/searchPan/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public synchronized Map<String,Object> findList(
            @PathVariable(name = "id") String id,
            HttpServletRequest request
    ) throws Exception {
        String accountId = request.getHeader("accountId");
        if(WsdUtils.isEmpty(accountId)){
            throw new MissingParameterException("accountId");
        }
        logger.info("时间：" + System.currentTimeMillis() + "，执行的查询id：" + id);
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if(WsdUtils.isEmpty(myOilCoupon)){
            throw new NotFoundException("oilCoupon.not_found");
        }
        if(!accountId.equals(myOilCoupon.getAccountId())){
            throw new NotRuleException("oilCoupon.not_is_you");
        }
        if(WsdUtils.isNotEmpty(myOilCoupon.getPan())){
            logger.info("已经分配过券码，直接返回，券码：" + myOilCoupon.getPan());
            Map<String,Object> map = Maps.newHashMap();
            String pan = AESUtil.decrypt(myOilCoupon.getPan(),AESUtil.dekey);
            map.put("result", AESUtil.encrypt(pan,AESUtil.key));
            map.put("isShowQRCode", true);
            saveSearchLog(accountId,WsdUtils.getIpAddress(request),myOilCoupon.getPan(),myOilCoupon.getOrderId());
            return map;
        }
        String sortStr = "[{property:'createTime',direction:'asc'}]";
        Pageable pageable = WebUitl.buildPageRequest(0, 50, sortStr);
        long beginTime = System.currentTimeMillis();
        Page<OilCoupon> oilCoupons = oilCouponRepository.findByPanAmtAndOilState(myOilCoupon.getPanAmt(),"0",pageable);
        if(WsdUtils.isEmpty(oilCoupons) || oilCoupons.getContent().size() == 0){
            logger.info("没有该面值的券码，发券失败！");
            throw new NotRuleException("oilCoupon.null");
        }
        OilCoupon oilCoupon = oilCoupons.getContent().get(new Random().nextInt(oilCoupons.getContent().size()));
        logger.info(oilCoupon.getPan());
        boolean result = updateService.updateOilCoupon(myOilCoupon,oilCoupon);
        //更新商品库存
        if(result){
            /*ShopInfo shopInfo = shopInfoRepository.findByShopPriceAndShopType(myOilCoupon.getPanAmt(),"0");
            if(WsdUtils.isNotEmpty(shopInfo)){
                shopInfo.setShopStock(shopInfo.getShopStock() - 1);
                shopInfoRepository.save(shopInfo);
            }*/
        }
        Map<String,Object> map = Maps.newHashMap();
        if(WsdUtils.isEmpty(myOilCoupon.getPan())){
            logger.info("更新券码没有成功，发券失败！");
            throw new NotRuleException("oilCoupon.null");
        }
        long endTime = System.currentTimeMillis();
        logger.info("执行发券成功，分配的券码为：" + myOilCoupon.getPan() + "，执行分券操作耗时时间：" + (endTime - beginTime) + "ms");
        map.put("result", AESUtil.encrypt(AESUtil.decrypt(myOilCoupon.getPan(),AESUtil.dekey),AESUtil.key));
        map.put("isShowQRCode", true);
        saveSearchLog(accountId,WsdUtils.getIpAddress(request),myOilCoupon.getPan(),myOilCoupon.getOrderId());
        return map;
    }

    @Async
    private void saveSearchLog(String accountId,String ip,String pan,String orderId){
        OilCouponSearchLog log = new OilCouponSearchLog();
        log.setAccountId(accountId);
        log.setCreateTime(new Date());
        log.setIp(ip);
        log.setPan(pan);
        log.setOrderId(orderId);
        oilCouponSearchLogRepository.save(log);
    }

}
