package com.winstar.order.controller;

import com.winstar.carLifeMall.service.EarlyAndEveningMarketConfigService;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.NineWhiteList;
import com.winstar.couponActivity.repository.NineWhiteListRepository;
import com.winstar.couponActivity.utils.ActivityIdEnum;
import com.winstar.drawActivity.comm.ErrorCodeEnum;
import com.winstar.drawActivity.entity.DrawRecord;
import com.winstar.drawActivity.repository.DrawRecordRepository;
import com.winstar.exception.InvalidParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.Constant;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.OilOrderUtil;
import com.winstar.redis.RedisTools;
import com.winstar.shop.entity.Activity;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.service.ShopService;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import com.winstar.user.utils.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    private AccountService accountService;
    @Autowired
    DrawRecordRepository drawRecordRepository;
    @Autowired
    EarlyAndEveningMarketConfigService earlyAndEveningMarketConfigService;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Autowired
    RedisTools redisTools;
    @Autowired
    NineWhiteListRepository nineWhiteListRepository;
    /**
     * 2019.03.31.23.59.59
     */
    private static final long END_OF_MARCH = 1554047999000L;

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
        //建行第二季度优惠包活动
        if (activityId.equals(String.valueOf(ActivityIdEnum.ACTIVITY_ID_109.getActivity()))) {
            NineWhiteList nineWhiteList = nineWhiteListRepository.findByAccountIdAndSendTime(accountId);
            if (!ObjectUtils.isEmpty(nineWhiteList)) {
                if (itemId.equals("1091")) {
                    String canBuy = OilOrderUtil.couponsPackage100(accountId, activityId);
                    if (canBuy.equals("1")) {
                        logger.error("建行二期优惠包活动，用户只能买二次(100元)");
                        throw new NotRuleException("onlyBuyOnce100.order");
                    } else if (canBuy.equals("2")) {
                        logger.error("建行二期优惠包活动，有未关闭订单(100元)");
                        throw new NotRuleException("haveNotPay100.order");
                    }
                } else if (itemId.equals("1092")) {
                    String can = OilOrderUtil.couponsPackage300(accountId, activityId);
                    if (can.equals("1")) {
                        logger.error("建行二期优惠包活动，用户只能买一次(300元)");
                        throw new NotRuleException("onlyBuyOnce300.order");
                    } else if (can.equals("2")) {
                        logger.error("建行二期优惠包活动，有未关闭订单(300元)");
                        throw new NotRuleException("haveNotPay300.order");
                    }
                }
            } else {
                logger.error("活动时间已过期");
                throw new NotRuleException("discountPackageActivities.expired");
            }

        }
        //锦鲤活动第2季度到3月31日(后面用记得改时间戳END_OF_MARCH)
        if (activityId.equals(String.valueOf(ActivityIdEnum.ACTIVITY_ID_204.getActivity()))) {
            if (System.currentTimeMillis() < END_OF_MARCH) {
                if (!redisTools.setIfAbsent(accountId + "_" + ActivityIdEnum.ACTIVITY_ID_204.getActivity(), 5)) {
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
                        logger.error("请用户购买正确活动商品");
                        throw new NotRuleException(" notWinning.drawActivity");
                    }
                } else {
                    logger.error("用户未中奖");
                    throw new NotRuleException(" notWinning.drawActivity");
                }
            } else {
                logger.error("活动时间已过期");
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
        if (activityId.equals("106") || activityId.equals("109") || activityId.equals("204") || activityId.equals("201") || activityId.equals("202") || activityId.equals("9") || activityId.equals("10")) {
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

}
