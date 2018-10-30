package com.winstar.oil.controller;

import com.google.common.collect.Maps;
import com.winstar.ActiveOilCoupon;
import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.construction.utils.Arith;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.entity.LookingUsedCoupon;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.entity.OilCoupon;
import com.winstar.oil.entity.OilCouponSearchLog;
import com.winstar.oil.repository.LookingUsedCouponRepository;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.oil.repository.OilCouponSearchLogRepository;
import com.winstar.oil.service.OilCouponUpdateService;
import com.winstar.oil.service.SendOilCouponService;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.redis.RedisTools;
import com.winstar.user.service.AccountService;
import com.winstar.utils.AESUtil;
import com.winstar.utils.WsdUtils;
import com.winstar.vo.OilSetMealVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ws.result.Result;

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

    @Autowired
    SendOilCouponService oilCouponService;

    @Autowired
    AccountService accountService;

    @Autowired
    PayOrderRepository payOrderRepository;

    @Autowired
    OilOrderRepository oilOrderRepository;

    @Autowired
    LookingUsedCouponRepository lookingUsedCouponRepository;

    @Autowired
    RedisTools redisTools;

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    @RequestMapping(value = "/sendOilCoupon",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public ResponseEntity sendOilCoupon(
        @RequestBody Map map
    ) throws Exception {
        Map<String,String> resultMap = new HashMap<>();
        String orderId = MapUtils.getString(map,"orderId");
        OilOrder oilOrder = oilOrderRepository.findBySerialNumber(orderId);
        if(WsdUtils.isEmpty(oilOrder)){
            resultMap.put("status","FAIL");
            resultMap.put("result", "该订单不存在！");
            return new ResponseEntity<>(resultMap,HttpStatus.OK);
        }
        List<PayOrder> orders = payOrderRepository.findByOrderNumberAndState(orderId, EnumType.PAY_STATE_SUCCESS.valueStr());
        if(WsdUtils.isEmpty(orders) || orders.size() == 0){
            resultMap.put("status","FAIL");
            resultMap.put("result", "该订单未支付！");
            return new ResponseEntity<>(resultMap,HttpStatus.OK);
        }
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdOrderByUseStateAsc(orderId);
        if(WsdUtils.isNotEmpty(myOilCoupons) && myOilCoupons.size() > 0){
            double number = myOilCoupons.get(0).getShopPrice()/100 - myOilCoupons.size();
            if(number > 0){
                MyOilCoupon mc = myOilCoupons.get(0);
                List<MyOilCoupon> myOilCouponList = new ArrayList<>();
                for (int i=0;i<number;i++){
                    MyOilCoupon myOilCoupon = new MyOilCoupon();
                    myOilCoupon.setAccountId(mc.getAccountId());
                    myOilCoupon.setCreateTime(mc.getCreateTime());
                    myOilCoupon.setOrderId(mc.getOrderId());
                    myOilCoupon.setPanAmt(mc.getPanAmt());
                    myOilCoupon.setSendState(mc.getSendState());
                    myOilCoupon.setUseState("0");
                    myOilCoupon.setShopId(mc.getShopId());
                    myOilCoupon.setShopPrice(mc.getShopPrice());
                    myOilCouponList.add(myOilCoupon);
                }
                myOilCouponRepository.save(myOilCouponList);
                resultMap.put("status","OK");
                resultMap.put("result", "该订单已修复！");
            }else{
                resultMap.put("status","FAIL");
                resultMap.put("result", "该订单已发券！");
            }
            return new ResponseEntity<>(resultMap,HttpStatus.OK);
        }
        //如果订单状态是未支付，则修改为支付成功状态
        if(oilOrder.getPayStatus()==0){
            oilOrder.setBankSerialNo(orders.get(0).getQid());
            oilOrder.setPayTime(orders.get(0).getUpdaedAt());
            oilOrder.setPayType(orders.get(0).getPayWay());
            oilOrder.setPayStatus(Integer.valueOf(orders.get(0).getState()));
            oilOrder.setSendStatus(3);
            oilOrder.setStatus(3);
            /******************/
            oilOrder.setIsAvailable("0");
            oilOrder.setUpdateTime(orders.get(0).getUpdaedAt());
            oilOrder.setFinishTime(orders.get(0).getUpdaedAt());
            oilOrderRepository.save(oilOrder);
        }
        return oilCouponService.handlerSendOilCoupon(orderId, oilOrder.getItemId(), oilOrder.getAccountId());
    }

    @RequestMapping(value = "/myOilSetMeal",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<OilSetMealVo> findOilSetMealList(
        @RequestParam(defaultValue = "0") Integer nextPage,
        @RequestParam(defaultValue = "1000") Integer pageSize,
        HttpServletRequest request
    ) throws Exception {
        String accountId = accountService.getAccountId(request);
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
        List<Object[]> objects = myOilCouponRepository.findOilSetMeal(accountId,begin,end);
        for(Object[] obj : objects){
            OilSetMealVo oilSetMealVo = new OilSetMealVo();
            oilSetMealVo.setTotalNumber(Integer.parseInt(obj[0].toString()));
            oilSetMealVo.setSurplusNumber(Integer.parseInt(obj[1].toString()));
            oilSetMealVo.setSurplusPrice((Double)obj[2]);
            oilSetMealVo.setTotalPrice((Double) obj[3]);
            oilSetMealVo.setOrderId((String) obj[4]);
            oilSetMealVo.setSendState((String) obj[5]);
            oilSetMealVo.setCreateTime((String) obj[6]);
            oilSetMeals.add(oilSetMealVo);
        }
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
        String accountId = accountService.getAccountId(request);
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
    public Map<String,Object> findList(
        @PathVariable(name = "id") String id,
        HttpServletRequest request
    ) throws Exception {
        String accountId = accountService.getAccountId(request);
        if(WsdUtils.isEmpty(accountId)){
            throw new MissingParameterException("accountId");
        }
        if(!redisTools.setIfAbsent(id, 10)){
            logger.info("点击过于频繁，请稍后再试！操作Id:" + id);
            //String message = "正在加载油券，请勿重复操作！";
            throw new NotRuleException("oilCoupon.loading");
        }
        logger.info("时间：" + System.currentTimeMillis() + "，执行的查询id：" + id);
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if(WsdUtils.isEmpty(myOilCoupon)){
            throw new NotFoundException("oilCoupon.not_found");
        }
        if(!accountId.equals(myOilCoupon.getAccountId())){
            throw new NotRuleException("oilCoupon.not_is_you");
        }
        if(myOilCoupon.getShopId().equals("8")){
            logger.info("0.01抢购券，判断有效期");
            if(!WsdUtils.validatorDate(myOilCoupon.getOpenDate(),myOilCoupon.getEndDate())){
                logger.info("券码已失效！");
                throw new NotRuleException("oilCoupon.expired");
            }
        }
        if(WsdUtils.isNotEmpty(myOilCoupon.getPan())){
            logger.info("已经分配过券码，直接返回，券码：" + myOilCoupon.getPan());
            Map<String,Object> map = Maps.newHashMap();
            String pan = AESUtil.decrypt(myOilCoupon.getPan(),AESUtil.dekey);
            map.put("result", AESUtil.encrypt(pan,AESUtil.key));
            saveSearchLog(accountId,WsdUtils.getIpAddress(request),myOilCoupon.getPan(),myOilCoupon.getOrderId());
            activateOilCoupon(myOilCoupon.getPan(),myOilCoupon.getPanAmt());
            return map;
        }
        long beginTime = System.currentTimeMillis();
        List<OilCoupon> oilCoupons = oilCouponRepository.findRandomOilCoupons(myOilCoupon.getPanAmt());
        if(WsdUtils.isEmpty(oilCoupons) || oilCoupons.size() == 0){
            logger.info("没有该面值的券码，发券失败！");
            throw new NotRuleException("oilCoupon.null");
        }
        OilCoupon oilCoupon = oilCoupons.get(new Random().nextInt(oilCoupons.size()));
        if(!redisTools.setIfAbsent(oilCoupon.getPan(), 60)){
            oilCoupon = getOilCoupon(oilCoupons);
        }
        if(ObjectUtils.isEmpty(oilCoupon)){
            logger.info("库存不足，分券失败！");
            throw new NotRuleException("oilCoupon.sale_over");
        }
        Result activeResult = activateOilCoupon(oilCoupon.getPan(), oilCoupon.getPanAmt());
        if(WsdUtils.isNotEmpty(activeResult) && activeResult.getCode().equals("SUCCESS")){
            MyOilCoupon moc = myOilCouponRepository.findOne(id);
            if (WsdUtils.isNotEmpty(moc.getPan())) {
                logger.info("已经分配过券码，直接返回，券码：" + moc.getPan());
                Map<String, Object> map = Maps.newHashMap();
                String pan = AESUtil.decrypt(moc.getPan(), AESUtil.dekey);
                map.put("result", AESUtil.encrypt(pan, AESUtil.key));
                saveSearchLog(accountId, WsdUtils.getIpAddress(request), moc.getPan(), moc.getOrderId());
                return map;
            }
            updateService.updateOilCoupon(myOilCoupon,oilCoupon);
        }
        Map<String,Object> map = Maps.newHashMap();
        if(WsdUtils.isEmpty(myOilCoupon.getPan())){
            logger.info("更新券码没有成功，发券失败！");
            throw new NotRuleException("oilCoupon.null");
        }
        long endTime = System.currentTimeMillis();
        logger.info("执行发券成功，分配的券码为：" + myOilCoupon.getPan() + "，执行分券操作耗时时间：" + (endTime - beginTime) + "ms");
        map.put("result", AESUtil.encrypt(AESUtil.decrypt(myOilCoupon.getPan(),AESUtil.dekey),AESUtil.key));
        saveSearchLog(accountId,WsdUtils.getIpAddress(request),myOilCoupon.getPan(),myOilCoupon.getOrderId());
        return map;
    }

    private OilCoupon getOilCoupon(List<OilCoupon> oilCoupons){
        for(OilCoupon oilCoupon : oilCoupons){
            if(redisTools.setIfAbsent(oilCoupon.getPan(), 60)){
                return oilCoupon;
            }
            logger.info(oilCoupon.getPan() + "油券正在进行分配，不能分配给其他用户");
        }
        return null;
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

    @RequestMapping(value = "activate/{id}",method = RequestMethod.GET)
    public ResponseEntity activatePan(@PathVariable String id, HttpServletRequest request) throws Exception {
        String accountId = accountService.getAccountId(request);
        if(WsdUtils.isEmpty(accountId)){
            throw new MissingParameterException("accountId");
        }
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if(WsdUtils.isEmpty(myOilCoupon)){
            throw new NotFoundException("oilCoupon.not_found");
        }
        if(!accountId.equals(myOilCoupon.getAccountId())){
            throw new NotRuleException("oilCoupon.not_is_you");
        }
        if(WsdUtils.isEmpty(myOilCoupon.getPan())){
            throw new NotRuleException("oilCoupon.not_found");
        }
        return new ResponseEntity<>(activateOilCoupon(myOilCoupon.getPan(), myOilCoupon.getPanAmt()), HttpStatus.OK);
    }

    private Result activateOilCoupon(String pan, Double panAmt) throws Exception {
        Result result = new Result();
        String panText = AESUtil.decrypt(pan, AESUtil.dekey);
        long beginTime = System.currentTimeMillis();
        Map<String, String> map = ActiveOilCoupon.active(panText.length() == 20 ? oilSendNewUrl : oilSendUrl, panText, panAmt + "");
        logger.info("激活的券码：" + pan + "，明文：" + panText + "，rc:" + MapUtils.getString(map, "rc") + "，rcDetail:" + MapUtils.getString(map, "rcDetail"));
        long endTime = System.currentTimeMillis();
        logger.info("激活消耗时间：" + (endTime - beginTime) + "ms");
        if(MapUtils.getString(map, "rc").equals("00")){
            logger.info("激活成功！");
            saveLookingUsedCoupon(pan,"1",beginTime,endTime,map);
            result.setCode("SUCCESS");
            result.setFailMessage("激活成功！");
        }else{
            logger.info("激活失败！");
            saveLookingUsedCoupon(pan,"0",beginTime,endTime,map);
            result.setCode("FAIL");
            result.setFailMessage("激活失败！");
        }
        return result;
    }

    @Async
    private void saveLookingUsedCoupon(String pan,String activateState, long beginTime, long endTime, Map<String, String> map) throws Exception {
        Date useDate = null;
        if (WsdUtils.isNotEmpty(MapUtils.getString(map, "txnDate")) && WsdUtils.isNotEmpty(MapUtils.getString(map, "txnTime"))) {
            useDate = DateUtils.parseDate(MapUtils.getString(map, "txnDate") + MapUtils.getString(map, "txnTime"), "yyyyMMddHHmmss");
        }
        LookingUsedCoupon lookingUsedCoupon = new LookingUsedCoupon();
        lookingUsedCoupon.setPan(pan);
        lookingUsedCoupon.setPanText(AESUtil.decrypt(pan,AESUtil.dekey));
        lookingUsedCoupon.setLookDate(new Date());
        lookingUsedCoupon.setUseDate(useDate);
        lookingUsedCoupon.setTId(MapUtils.getString(map, "tid"));
        lookingUsedCoupon.setActivateState(activateState);
        lookingUsedCoupon.setBeginTime(beginTime);
        lookingUsedCoupon.setEndTime(endTime);
        lookingUsedCoupon.setTimeConsuming(endTime - beginTime);
        lookingUsedCoupon.setRc(MapUtils.getString(map, "rc"));
        lookingUsedCoupon.setRcDetail(MapUtils.getString(map, "rcDetail"));
        lookingUsedCouponRepository.save(lookingUsedCoupon);
    }

    @RequestMapping(value = "/modifyOilCoupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void findOilSetMealInfo(
        @RequestParam(required = false) String id,
        @RequestParam(required = false) String accountId,
        @RequestParam(required = false) String shopId,
        @RequestParam(required = false) String pan,
        @RequestParam(required = false) String useDate,
        @RequestParam(required = false) String useState,
        @RequestParam(required = false) String tid
    ) throws Exception {
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (WsdUtils.isNotEmpty(myOilCoupon)) {
            if (WsdUtils.isNotEmpty(accountId)) myOilCoupon.setAccountId(accountId);
            if (WsdUtils.isNotEmpty(shopId)) myOilCoupon.setShopId(shopId);
            myOilCoupon.setPan(WsdUtils.isNotEmpty(pan) ? pan : null);
            myOilCoupon.setUseDate(WsdUtils.isNotEmpty(useDate) ? useDate : null);
            myOilCoupon.setUseState(WsdUtils.isNotEmpty(useState) ? useState : "0");
            myOilCoupon.setTId(WsdUtils.isNotEmpty(tid) ? tid : null);
            myOilCouponRepository.save(myOilCoupon);
        }
    }

    @RequestMapping(value = "/deleteOilCoupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public void deleteOilCoupon(
        @RequestParam String id
    ) throws Exception {
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (WsdUtils.isNotEmpty(myOilCoupon)) {
            myOilCouponRepository.delete(myOilCoupon);
        }
    }

}
