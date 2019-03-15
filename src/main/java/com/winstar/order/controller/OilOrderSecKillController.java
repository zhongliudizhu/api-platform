package com.winstar.order.controller;

import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.CareJuanList;
import com.winstar.couponActivity.repository.CareJuanListRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.drawActivity.comm.ErrorCodeEnum;
import com.winstar.drawActivity.entity.DrawRecord;
import com.winstar.drawActivity.repository.DrawRecordRepository;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.*;
import com.winstar.redis.RedisTools;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shoo on 2017/7/7 13:52.
 * 油券订单
 */
@RestController
@RequestMapping("/api/v1/cbc/SecKillOrders")
public class OilOrderSecKillController {
    public static final Logger logger = LoggerFactory.getLogger(OilOrderSecKillController.class);
    @Autowired
    private OilOrderRepository orderRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private AccountService accountService;
    @Autowired
    CareJuanListRepository careJuanListRepository;
    @Autowired
    DrawRecordRepository drawRecordRepository;
    @Autowired
    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Autowired
    RedisTools redisTools;
    /**
     * 2019.03.31.23.59.59
     */
    private static final long END_OF_MARCH = 1554047999000L;

    @Value("${info.amount}")
    private Integer amount;

    @Value("${info.sendMsgUrl}")
    private String sendMsgUrl;

    private static final String ACTIVITY_MORNING_MARKET = "9";

    /**
     * 添加油券订单
     *
     * @param itemId     商品id
     * @param activityId 活动id
     */
    @PostMapping(produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity addOrder(@RequestParam String itemId, @RequestParam String activityId
            , HttpServletRequest request) throws NotFoundException, NotRuleException, InvalidParameterException {
        logger.info("itemId is {} and activityId is {}", itemId, activityId);
        String accountId = accountService.getAccountId(request);
        Account account = accountService.findOne(accountId);
        String serialNumber = OilOrderUtil.getSerialNumber();
        long startTime = System.currentTimeMillis();

        //2.根据商品id 查询商品
        Goods goods = shopService.findByGoodsId(itemId);
        logger.info("开始添加订单，goodsId：" + goods.getId());
        if (ObjectUtils.isEmpty(goods)) {
            logger.error("查询商品失败，itemId：" + itemId);
            throw new NotFoundException("goods.order");
        }

        //3.根据活动id查询活动
        Activity activity = shopService.findByActivityId(activityId);
        if (ObjectUtils.isEmpty(activity)) {
            logger.error("查询活动失败，activityId：" + activityId);
            throw new NotFoundException("activity.order");
        }
        if (goods.getIsSale() == 1) {
            logger.error("商品" + goods.getId() + "已售罄！");
            throw new NotRuleException("isSale.order");
        }
        Integer soldAmount = 0;
        if (activity.getType() == ActivityIdEnum.ACTIVITY_ID_202.getActivity()) {
            soldAmount = OilOrderUtil.getSoldAmount(itemId, DateUtil.getDayBegin(), DateUtil.getDayEnd());
        } else {
            soldAmount = OilOrderUtil.getSoldAmount(itemId, DateUtil.getDayBegin(), DateUtil.getDayEnd());
        }
        //判断是否超过限售数量
        if (goods.getLimitAmount() != null && goods.getLimitAmount() != 0 && (soldAmount >= goods.getLimitAmount())) {
            logger.error("商品" + goods.getId() + "已超售！");
            throw new NotRuleException("soldOut.order");
        }

        checkEarlyAndEveningMarket(activityId, accountId);
        if (activityId.equals(String.valueOf(ActivityIdEnum.ACTIVITY_ID_201.getActivity()))) {
            if (StringUtils.isEmpty(account.getAuthInfoCard())) {
                throw new NotRuleException("notBindInfoCard.order");
            }
            String canBuy = OilOrderUtil.judgeActivitySecKill(accountId, activityId);
            if (canBuy.equals("1")) {
                logger.error("活动201，每用户每周只能买一次");
                throw new NotRuleException("oneMonthOnce.order");
            } else if (canBuy.equals("2")) {
                logger.error("活动201，有未关闭订单");
                throw new NotRuleException("haveNotPay.order");
            }

        }
        if (activityId.equals(String.valueOf(ActivityIdEnum.ACTIVITY_ID_202.getActivity()))) {
            String canBuy = OilOrderUtil.judgeActivitySecKill(accountId, activityId);
            if (canBuy.equals("1")) {
                logger.error("活动202，每用户每周只能买一次");
                throw new NotRuleException("oneMonthOnce.order");
            } else if (canBuy.equals("2")) {
                logger.error("活动202，有未关闭订单");
                throw new NotRuleException("haveNotPay.order");
            }
        }
        //锦鲤活动第2季度到3月31日(后面用记得该时间)
        if (activityId.equals(String.valueOf(ActivityIdEnum.ACTIVITY_ID_204.getActivity()))) {
            if (System.currentTimeMillis() < END_OF_MARCH) {
                if(!redisTools.setIfAbsent(accountId + "_" + ActivityIdEnum.ACTIVITY_ID_204.getActivity(), 5)){
                    logger.info(ErrorCodeEnum.ERROR_CODE_ACTIVITY_ONLY_ONE.description());
                    throw new NotRuleException("haveNotPay.order");
                }
                DrawRecord drawRecord = drawRecordRepository.findByAccountId(accountId);
                if (!StringUtils.isEmpty(drawRecord)) {
                    if ((itemId.equals("2041") && drawRecord.getPrizeType().equals("1")) || (itemId.equals("2042") && drawRecord.getPrizeType().equals("2"))) {
                        String canBuy = OilOrderUtil.BrocadeCarp(accountId, activityId);
                        if (canBuy.equals("1")) {
                            logger.error("锦鲤活动，用户只能买一次(204)");
                            throw new NotRuleException("purchaseOnce.order");
                        } else if (canBuy.equals("2")) {
                            logger.error("锦鲤活动，有未关闭订单(204)");
                            throw new NotRuleException("haveNotPay.order");
                        }
                    } else {
                        throw new NotRuleException(" notWinning.drawActivity");
                    }
                } else {
                    throw new NotRuleException(" notWinning.drawActivity");
                }
            } else {
                throw new NotRuleException("activityOverdue.drawActivity");
            }
        }

        if (activityId.equals(String.valueOf(ActivityIdEnum.ACTIVITY_ID_106.getActivity()))) {
            //查询该用户是否 买过105
            MyCoupon myCoupon = myCouponRepository.findByAccountIdAndActivityIdAndStatus(accountId, "105", 1);
            if (ObjectUtils.isEmpty(myCoupon)) {
                String canBuy = OilOrderUtil.judgeActivity2(accountId, activityId);
                if (canBuy.equals("1")) {
                    logger.error("活动106，活动内每用户只能买一次");
                    throw new NotRuleException("oneMonthOnce.order");
                } else if (canBuy.equals("2")) {
                    logger.error("活动106，有未关闭订单");
                    throw new NotRuleException("haveNotPay.order");
                }
            } else if (!ObjectUtils.isEmpty(myCoupon)) {
                logger.error("已经购买过105活动");
                throw new NotRuleException("ParticipatedInActivities.order");
            }
        }
        //5.初始化订单及订单项
        OilOrder oilOrder = new OilOrder(accountId, serialNumber, Constant.ORDER_STATUS_CREATE, Constant.PAY_STATUS_NOT_PAID, new Date(), Constant.REFUND_STATUS_ORIGINAL, itemId, activityId);
        if (activityId.equals("106") || activityId.equals("204") || activityId.equals("201") || activityId.equals("202") || activityId.equals("9") || activityId.equals("10")) {
            oilOrder = OilOrderUtil.initOrderSecKill(oilOrder, goods, activity.getType());
            //6.生成订单
            oilOrder = orderRepository.save(oilOrder);
        }

        long endTime = System.currentTimeMillis();
        logger.info("添加订单成功，goodsId：" + goods.getId() + "|总用时: " + (endTime - startTime));
        return new ResponseEntity<>(oilOrder, HttpStatus.OK);
    }

    void checkEarlyAndEveningMarket(String activityId, String accountId) throws NotRuleException, InvalidParameterException {

        if (StringUtils.isEmpty(activityId) || !activityId.equals(ACTIVITY_MORNING_MARKET)) {
            return;
        }
        earlyAndEveningMarketConfigService.checkEarlyAndEveningMarketIsOk(Integer.valueOf(activityId));
        long times = ServiceManager.oilOrderRepository.countValidOrderByActivityIdAndCreateTimeAndAccountId(activityId, accountId);

        if (times > 0) {
            throw new NotRuleException("justOnce.earlyAndEveningMarket");
        }
    }

    /**
     * 是否有资格领取免费洗车$消毒
     *
     * @param request
     * @param activityId
     * @return
     * @throws NotRuleException
     */
    @RequestMapping(value = "/getCareCoupons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getCareCoupons(
            HttpServletRequest request,
            @RequestParam String activityId,
            @RequestParam(defaultValue = "0.0") Double salePrice) throws NotRuleException {
        String accountId = accountService.getAccountId(request);
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(salePrice)) {
            throw new NotRuleException("getCareCoupons.params");
        }
        List<CareJuanList> careJuanList = careJuanListRepository.findByAccountIdandJoinTypeAndTypeAndTime(accountId, DateUtil.getWeekBegin(), DateUtil.getWeekEnd());
        Map<String, String> map = new HashMap<>();
        if (!ObjectUtils.isEmpty(careJuanList)) {
            map.put("result", "0");
            return map;
        }
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByActivityId(activityId, DateUtil.getWeekBegin(), DateUtil.getWeekEnd());

        if (oilOrders.size() <= 200) {
            if (salePrice == 1000) {
                map.put("result", "1");//领取免费洗车卷
            } else if (salePrice == 2000) {
                map.put("result", "2");//领取免费洗车卷&消毒
            } else {
                map.put("result", "0");
            }
        } else {
            map.put("result", "0");//没资格
        }
        return map;
    }

    /**
     * 领取汽车养护券
     *
     * @param request
     * @param name
     * @param phoneNumber
     * @param plateNumber
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/giveCareCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CareJuanList giveCareCoupons(
            HttpServletRequest request, String name, String phoneNumber, String plateNumber, String activityId, String type
    ) throws NotRuleException, NotFoundException {
        Object accountId = request.getAttribute("accountId");
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(plateNumber) || StringUtils.isEmpty(type)) {
            throw new NotRuleException("NotRuleException.params");
        }
        List<CareJuanList> careJuanLists = careJuanListRepository.findByAccountIdandJoinTypeAndTypeAndTime(accountId.toString(), DateUtil.getWeekBegin(), DateUtil.getWeekEnd());
        if (!ObjectUtils.isEmpty(careJuanLists)) {
            throw new NotFoundException("getCareCoupons.isGet");
        }
        List<OilOrder> oilOrders = ServiceManager.oilOrderRepository.findByActivityId(activityId, DateUtil.getWeekBegin(), DateUtil.getWeekEnd());
        if (oilOrders.size() >= 200) {
            throw new NotRuleException("getCareCoupons.isOut");
        }
        CareJuanList careJuanList = new CareJuanList();
        careJuanList.setAccountId(accountId.toString());
        careJuanList.setName(name);
        careJuanList.setPhoneNumber(phoneNumber);
        careJuanList.setPlateNumber(plateNumber);
        careJuanList.setCreatTime(new Date());
        careJuanList.setType(Integer.parseInt(type));
        if (activityId.equals("201")) {
            careJuanList.setJoinType(1);
        } else {
            careJuanList.setJoinType(2);
        }
        CareJuanList getCareJuanList = careJuanListRepository.save(careJuanList);
        if (ObjectUtils.isEmpty(getCareJuanList)) {
            throw new NotFoundException("getCareCoupons.error");
        }
        String content = "";
        if (type.equals("2")) {
            content = "您已成功在优驾行微信平台领取到免费洗车与车内臭氧消毒权益，请您在3个工作日内前往西安精典汽车服务有限公司体验。地址：西安市雁翔路与西影路十字向南100米路西；联系电：029—89293377。每人仅限一次。";
        } else {
            content = "您已成功在优驾行微信平台领取到免费洗车权益，请您在3个工作日内前往西安精典汽车服务有限公司体验。地址：西安市雁翔路与西影路十字向南100米路西；联系电：029—89293377。每人仅限一次。";
        }

        sendMsg(sendMsgUrl, phoneNumber, content);
        return getCareJuanList;
    }

    @Async
    public void sendMsg(String sendMsgUrl, String phoneNo, String content) {

        SmsResult text = sendNotice(sendMsgUrl, content, phoneNo);
        logger.info(text.getStatus());

    }

    /**
     * 发送短信
     *
     * @param msgContent  短信内容
     * @param phoneNumber 手机号码
     * @return result
     */
    public static SmsResult sendNotice(String sendMsgUrl, String msgContent, String phoneNumber) {
        SmsResult result = new SmsResult();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        SendMsg msg = new SendMsg();
        msg.setAutograph(1);
        msg.setMsgContent(msgContent);
        msg.setPhoneNumber(phoneNumber);
        JSONObject jsonObject = JSONObject.fromObject(msg);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonObject.toString(), headers);
        try {
            result = restTemplate.postForObject(sendMsgUrl, formEntity, SmsResult.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus("失败");
            result.setText("发送短信失败:" + ex);
        }
        return result;
    }

}
