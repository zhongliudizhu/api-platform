package com.winstar.oil.controller;

import com.google.common.collect.Maps;
import com.winstar.ActiveOilCoupon;
import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.oil.entity.*;
import com.winstar.oil.repository.*;
import com.winstar.oil.service.KDTree;
import com.winstar.oil.service.OilCouponUpdateService;
import com.winstar.oil.service.OilStationService;
import com.winstar.oil.service.SendOilCouponService;
import com.winstar.oil.utils.OilCouponUseLimitUtils;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.redis.OilRedisTools;
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
import java.text.SimpleDateFormat;
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
    OilBlackListRepository oilBlackListRepository;

    @Autowired
    OilStationService oilStationService;
    @Autowired
    OilRedisTools oilRedisTools;

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    private String prefix = "oil_pan_list_";

    private String click_limit = "cbc_oil_click_limit";

    @RequestMapping(value = "/sendOilCoupon", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public ResponseEntity sendOilCoupon(
            @RequestBody Map map
    ) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        String orderId = MapUtils.getString(map, "orderId");
        if(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date())) - Integer.valueOf(orderId.substring(0,6)) > 3){
            resultMap.put("status", "FAIL");
            resultMap.put("result", "3个月之前的订单不允许手动发券！");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        OilOrder oilOrder = oilOrderRepository.findBySerialNumber(orderId);
        if (WsdUtils.isEmpty(oilOrder)) {
            resultMap.put("status", "FAIL");
            resultMap.put("result", "该订单不存在！");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        List<PayOrder> orders = payOrderRepository.findByOrderNumberAndState(orderId, EnumType.PAY_STATE_SUCCESS.valueStr());
        if (WsdUtils.isEmpty(orders) || orders.size() == 0) {
            resultMap.put("status", "FAIL");
            resultMap.put("result", "该订单未支付！");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdOrderByUseStateAsc(orderId);
        if (WsdUtils.isNotEmpty(myOilCoupons) && myOilCoupons.size() > 0) {
            double number = myOilCoupons.get(0).getShopPrice() / 100 - myOilCoupons.size();
            if (number > 0) {
                MyOilCoupon mc = myOilCoupons.get(0);
                List<MyOilCoupon> myOilCouponList = new ArrayList<>();
                for (int i = 0; i < number; i++) {
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
                resultMap.put("status", "OK");
                resultMap.put("result", "该订单已修复！");
            } else {
                resultMap.put("status", "FAIL");
                resultMap.put("result", "该订单已发券！");
            }
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        //如果订单状态是未支付，则修改为支付成功状态
        if (oilOrder.getPayStatus() == 0) {
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

    @RequestMapping(value = "/myOilSetMeal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<OilSetMealVo> findOilSetMealList(
            @RequestParam(defaultValue = "0") Integer nextPage,
            @RequestParam(defaultValue = "1000") Integer pageSize,
            HttpServletRequest request
    ) throws Exception {
        String accountId = accountService.getAccountId(request);
        if (WsdUtils.isEmpty(accountId)) {
            throw new NotFoundException("accountId");
        }
        logger.info("accountId===" + accountId);
        List<OilSetMealVo> oilSetMeals = getOilSetMeals(accountId, nextPage * pageSize, pageSize);
        if (ObjectUtils.isEmpty(oilSetMeals)) {
            throw new NotFoundException("oil.item");
        }
        return oilSetMeals;
    }

    private List<OilSetMealVo> getOilSetMeals(String accountId, Integer begin, Integer end) {
        List<OilSetMealVo> oilSetMeals = new ArrayList<>();
        List<Object[]> objects = myOilCouponRepository.findOilSetMeal(accountId, begin, end);
        for (Object[] obj : objects) {
            OilSetMealVo oilSetMealVo = new OilSetMealVo();
            oilSetMealVo.setTotalNumber(Integer.parseInt(obj[0].toString()));
            oilSetMealVo.setSurplusNumber(Integer.parseInt(obj[1].toString()));
            oilSetMealVo.setSurplusPrice((Double) obj[2]);
            oilSetMealVo.setTotalPrice((Double) obj[3]);
            oilSetMealVo.setOrderId((String) obj[4]);
            oilSetMealVo.setSendState((String) obj[5]);
            oilSetMealVo.setCreateTime((String) obj[6]);
            oilSetMeals.add(oilSetMealVo);
        }
        return oilSetMeals;
    }

    @RequestMapping(value = "/myOilSetMealInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyOilCoupon> findOilSetMealInfo(
            @RequestParam String orderId,
            HttpServletRequest request
    ) throws Exception {
        logger.info("orderId===" + orderId);
        if (StringUtils.isEmpty(orderId)) {
            throw new NotRuleException("orderId");
        }
        String accountId = accountService.getAccountId(request);
        if (WsdUtils.isEmpty(accountId)) {
            throw new NotFoundException("accountId");
        }
        logger.info("accountId===" + accountId);
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByAccountIdAndOrderIdOrderByUseStateAscPanDesc(accountId, orderId);

        if (WsdUtils.isEmpty(myOilCoupons)) {
            throw new NotFoundException("myOilCoupons");
        }
        for (MyOilCoupon myOilCoupon : myOilCoupons) {
            myOilCoupon.setShopPrice(myOilCoupon.getPanAmt());//临时赋值 （将卷价赋值给套餐价）
        }
        return getResult(myOilCoupons);
    }

    /**
     * 返回结果中不需要的字段值置空
     */
    private List<MyOilCoupon> getResult(List<MyOilCoupon> myOilCoupons) {
        for (MyOilCoupon myOilCoupon : myOilCoupons) {
            myOilCoupon.setShopId(null);
            myOilCoupon.setPan(null);
        }
        return myOilCoupons;
    }

    @RequestMapping(value = "/searchPan/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> findList(
            @PathVariable(name = "id") String id,
            @RequestParam(required = false) double[] pos,
            HttpServletRequest request
    ) throws Exception {
        String accountId = accountService.getAccountId(request);
        if (WsdUtils.isEmpty(accountId)) {
            throw new MissingParameterException("accountId");
        }
        if (!oilRedisTools.setIfAbsent(id, 300)) {
            logger.info("点击过于频繁，请稍后再试！操作Id:" + id);
            throw new NotRuleException("oilCoupon.loading");
        }
        if(OilCouponUseLimitUtils.isBlack(accountId)){
            oilRedisTools.remove(id);
            throw new NotRuleException("your_is_black");
        }
        String limitUserCode = OilCouponUseLimitUtils.isLimitUser(id, accountId);
        if(!StringUtils.isEmpty(limitUserCode)){
            logger.info("您是受限制用户，已经达到规定的限制，code：" + limitUserCode);
            oilRedisTools.remove(id);
            throw new NotRuleException(limitUserCode);
        }
        if ("yes".equals(oilRedisTools.get("check_pos_switch"))) {
            logger.info("电子围栏功能已开启");
            if (ObjectUtils.isEmpty(pos)) {
                throw new NotRuleException("pos.null");
            }
            KDTree tree = oilStationService.getOilStationTree();
            double[] res = tree.query(pos);
            double distance = oilStationService.getDistance(pos[1], pos[0], res[1], res[0]);
            if (distance > 1000) {
                logger.error("加油站离您较远，请先行驶至加油站后使用加油券 distance is {} ", distance);
                oilRedisTools.remove(id);
                throw new NotRuleException("distance.far");
            }
        }

        logger.info("时间：" + System.currentTimeMillis() + "，执行的查询id：" + id);
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (WsdUtils.isEmpty(myOilCoupon)) {
            oilRedisTools.remove(id);
            throw new NotFoundException("oilCoupon.not_found");
        }
        if (!accountId.equals(myOilCoupon.getAccountId())) {
            oilRedisTools.remove(id);
            throw new NotRuleException("oilCoupon.not_is_you");
        }
        if (WsdUtils.isNotEmpty(myOilCoupon.getPan())) {
            logger.info("已经分配过券码，直接返回，券码：" + myOilCoupon.getPan());
            Map<String, Object> map = Maps.newHashMap();
            String pan = AESUtil.decrypt(myOilCoupon.getPan(), AESUtil.dekey);
            map.put("result", AESUtil.encrypt(pan, AESUtil.key));
            saveSearchLog(accountId, WsdUtils.getIpAddress(request), myOilCoupon.getPan(), myOilCoupon.getOrderId());
            activateOilCoupon(myOilCoupon.getPan(), myOilCoupon.getPanAmt());
            oilRedisTools.remove(id);
            return map;
        }
        if(!OilCouponUseLimitUtils.isVip(accountId)){
            if (!OilCouponUseLimitUtils.getCouponSwitch()) {
                logger.info("查看油券功能已关闭！！");
                oilRedisTools.remove(id);
                throw new NotRuleException("oilCoupon.null");
            }
            Integer clickNumber = (Integer) oilRedisTools.get(click_limit);
            if(!ObjectUtils.isEmpty(clickNumber) && clickNumber > 0){
                List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdAndUseStateAndPanNotNull(myOilCoupon.getOrderId(), "0");
                if(myOilCoupons.size() >= clickNumber){
                    logger.info("点开未使用的已经达到上限！！");
                    oilRedisTools.remove(id);
                    throw new NotRuleException("oilCoupon.click.limited");
                }
            }
        }
        long beginTime = System.currentTimeMillis();
        Object popValue = null;
        try{
            logger.info("剩余油券数量：" + oilRedisTools.getSetSize(prefix + myOilCoupon.getPanAmt()));
            popValue = oilRedisTools.getRandomKeyFromSet(prefix + myOilCoupon.getPanAmt());
        }catch (Exception e){
            logger.error("redis异常！", e);
        }
        if (ObjectUtils.isEmpty(popValue)) {
            logger.info("没有该面值的券码，发券失败！");
            oilRedisTools.remove(id);
            throw new NotRuleException("oilCoupon.null");
        }
        logger.info("获取的油券编码：" + popValue);
        Result activeResult = null;
        try{
            activeResult = activateOilCoupon(popValue.toString(), myOilCoupon.getPanAmt());
        }catch (Exception e){
            logger.error("易通激活接口异常，拿出的券回归缓存中");
            oilRedisTools.addSet(prefix + myOilCoupon.getPanAmt(), popValue);
            oilRedisTools.remove(id);
        }
        if (WsdUtils.isNotEmpty(activeResult) && activeResult.getCode().equals("SUCCESS")) {
            MyOilCoupon moc = myOilCouponRepository.findOne(id);
            if (WsdUtils.isNotEmpty(moc.getPan())) {
                logger.info("已经分配过券码，直接返回，券码：" + moc.getPan());
                Map<String, Object> map = Maps.newHashMap();
                String pan = AESUtil.decrypt(moc.getPan(), AESUtil.dekey);
                map.put("result", AESUtil.encrypt(pan, AESUtil.key));
                oilRedisTools.addSet(prefix + myOilCoupon.getPanAmt(), popValue);
                saveSearchLog(accountId, WsdUtils.getIpAddress(request), moc.getPan(), moc.getOrderId());
                oilRedisTools.remove(id);
                return map;
            }
            OilCoupon oilCoupon = oilCouponRepository.findByPan(popValue.toString());
            updateService.updateOilCoupon(myOilCoupon, oilCoupon);
        }
        Map<String, Object> map = Maps.newHashMap();
        if (WsdUtils.isEmpty(myOilCoupon.getPan())) {
            logger.info("更新券码没有成功，发券失败！");
            oilRedisTools.remove(id);
            throw new NotRuleException("oilCoupon.null");
        }
        long endTime = System.currentTimeMillis();
        logger.info("执行发券成功，分配的券码为：" + myOilCoupon.getPan() + "，执行分券操作耗时时间：" + (endTime - beginTime) + "ms");
        map.put("result", AESUtil.encrypt(AESUtil.decrypt(myOilCoupon.getPan(), AESUtil.dekey), AESUtil.key));
        oilRedisTools.remove(id);
        saveSearchLog(accountId, WsdUtils.getIpAddress(request), myOilCoupon.getPan(), myOilCoupon.getOrderId());
        return map;
    }

    @Async
    private void saveSearchLog(String accountId, String ip, String pan, String orderId) {
        OilCouponSearchLog log = new OilCouponSearchLog();
        log.setAccountId(accountId);
        log.setCreateTime(new Date());
        log.setIp(ip);
        log.setPan(pan);
        log.setOrderId(orderId);
        oilCouponSearchLogRepository.save(log);
    }

    @RequestMapping(value = "activate/{id}", method = RequestMethod.GET)
    public ResponseEntity activatePan(@PathVariable String id, HttpServletRequest request) throws Exception {
        String accountId = accountService.getAccountId(request);
        if (WsdUtils.isEmpty(accountId)) {
            throw new MissingParameterException("accountId");
        }
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (WsdUtils.isEmpty(myOilCoupon)) {
            throw new NotFoundException("oilCoupon.not_found");
        }
        if (!accountId.equals(myOilCoupon.getAccountId())) {
            throw new NotRuleException("oilCoupon.not_is_you");
        }
        if (WsdUtils.isEmpty(myOilCoupon.getPan())) {
            throw new NotRuleException("oilCoupon.not_found");
        }
        return new ResponseEntity<>(activateOilCoupon(myOilCoupon.getPan(), myOilCoupon.getPanAmt()), HttpStatus.OK);
    }

    public Result activateOilCoupon(String pan, Double panAmt) throws Exception {
        Result result = new Result();
        String panText = AESUtil.decrypt(pan, AESUtil.dekey);
        long beginTime = System.currentTimeMillis();
        Map<String, String> map = ActiveOilCoupon.active(panText.length() == 20 ? oilSendNewUrl : oilSendUrl, panText, panAmt + "");
        logger.info("激活的券码：" + pan + "，明文：" + panText + "，rc:" + MapUtils.getString(map, "rc") + "，rcDetail:" + MapUtils.getString(map, "rcDetail"));
        long endTime = System.currentTimeMillis();
        logger.info("激活消耗时间：" + (endTime - beginTime) + "ms");
        if (MapUtils.getString(map, "rc").equals("00")) {
            logger.info("激活成功！");
            saveLookingUsedCoupon(pan, "1", beginTime, endTime, map);
            result.setCode("SUCCESS");
            result.setFailMessage("激活成功！");
        } else {
            logger.info("激活失败！");
            saveLookingUsedCoupon(pan, "0", beginTime, endTime, map);
            result.setCode("FAIL");
            result.setFailMessage("激活失败！");
        }
        return result;
    }

    @Async
    private void saveLookingUsedCoupon(String pan, String activateState, long beginTime, long endTime, Map<String, String> map) throws Exception {
        Date useDate = null;
        if (WsdUtils.isNotEmpty(MapUtils.getString(map, "txnDate")) && WsdUtils.isNotEmpty(MapUtils.getString(map, "txnTime"))) {
            useDate = DateUtils.parseDate(MapUtils.getString(map, "txnDate") + MapUtils.getString(map, "txnTime"), "yyyyMMddHHmmss");
        }
        LookingUsedCoupon lookingUsedCoupon = new LookingUsedCoupon();
        lookingUsedCoupon.setPan(pan);
        lookingUsedCoupon.setPanText(AESUtil.decrypt(pan, AESUtil.dekey));
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
    ) {
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (WsdUtils.isNotEmpty(myOilCoupon)) {
            if (WsdUtils.isNotEmpty(accountId)) myOilCoupon.setAccountId(accountId);
            if (WsdUtils.isNotEmpty(shopId)) myOilCoupon.setShopId(shopId);
            myOilCoupon.setPan(WsdUtils.isNotEmpty(pan) ? pan : null);
            if(oilRedisTools.setExists(prefix + myOilCoupon.getPanAmt(), pan)) oilRedisTools.removeSetMembers(prefix + myOilCoupon.getPanAmt(), pan);
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
    ) {
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (WsdUtils.isNotEmpty(myOilCoupon)) {
            myOilCouponRepository.delete(myOilCoupon);
        }
    }

    @RequestMapping(value = "/statisticalOilVolume", method = RequestMethod.POST)
    public Map statisticalOilVolume(HttpServletRequest request) throws NotRuleException {
        Map<Object, Object> map = new HashMap<>();
        String accountId = accountService.getAccountId(request);

        long num = myOilCouponRepository.findByUseState(accountId);
        Double sum = myOilCouponRepository.findByPanamt(accountId);
        if (WsdUtils.isEmpty(sum)) {
            sum = 0.00;
        }
        map.put("num", num);
        map.put("sum", sum);
        return map;
    }

    /**
     * 油券使用详情
     *
     */
    @GetMapping(value = "/mycoupon/info")
    public com.winstar.vo.Result getInfo(@RequestParam String id) throws Exception {
        Map<String, String> map = new HashMap<>();
        MyOilCoupon myOilCoupon = myOilCouponRepository.findOne(id);
        if (ObjectUtils.isEmpty(myOilCoupon)) {
            return com.winstar.vo.Result.fail("data_null", "该券不存在");
        }
        if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(myOilCoupon.getCreateTime()).getTime() < 1556640000000L) {
            return com.winstar.vo.Result.fail("time_error", "无法查看详情");
        }
        if ("0".equals(myOilCoupon.getUseState())) {
            return com.winstar.vo.Result.fail("error", "该油券未使用");
        }
        map.put("usedTime", myOilCoupon.getUseDate());
        OilStation oilStation = oilStationService.getOilStation(myOilCoupon.getTId());
        if (!ObjectUtils.isEmpty(oilStation)) {
            map.put("usedLocation", oilStation.getName());
        } else {
            map.put("usedLocation", "陕西省");
        }
        StringBuilder panCode = new StringBuilder(AESUtil.decrypt(myOilCoupon.getPan(), AESUtil.dekey));
        map.put("panCode", panCode.replace(2, 16, "** **** **** **** ").toString());
        return com.winstar.vo.Result.success(map);
    }

}
