package com.winstar.communalCoupon.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.communalCoupon.entity.AccountCoupon;
import com.winstar.communalCoupon.entity.CouponSendRecord;
import com.winstar.communalCoupon.repository.AccountCouponRepository;
import com.winstar.communalCoupon.repository.CouponSendRecordRepository;
import com.winstar.communalCoupon.service.AccountCouponService;
import com.winstar.communalCoupon.vo.SendCouponDomain;
import com.winstar.costexchange.vo.AccountCouponVo;
import com.winstar.order.utils.DateUtil;
import com.winstar.order.utils.Week;
import com.winstar.redis.CouponRedisTools;
import com.winstar.redis.RedisTools;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.repository.AccountRepository;
import com.winstar.utils.WebUitl;
import com.winstar.vo.Result;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zl on 2019/5/27
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@AllArgsConstructor
public class AccountCouponController {

    private static final Logger logger = LoggerFactory.getLogger(AccountCouponController.class);

    AccountCouponRepository accountCouponRepository;

    GoodsRepository goodsRepository;

    AccountCouponService accountCouponService;

    CouponSendRecordRepository sendRecordRepository;

    AccountRepository accountRepository;

    RedisTools redisTools;

    CouponRedisTools couponRedisTools;


    /**
     * 查询优惠券列表
     */
    @RequestMapping(value = "getCoupons", method = RequestMethod.GET)
    public Result getCoupons(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer nextPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = AccountCouponService.NORMAL) String state) {
        if (!state.equals(AccountCouponService.NORMAL) && !state.equals(AccountCouponService.USED) && !state.equals(AccountCouponService.EXPIRED) && !state.equals(AccountCouponService.LOCKED)) {
            logger.info("状态值错误！");
            return Result.fail("state_not_auth", "状态值错误！");
        }
        String accountId = (String) request.getAttribute("accountId");
        String key = "coupon_checking_" + accountId;
        if (redisTools.setIfAbsent(key, 1800)) {
            logger.info("检查优惠券状态，如已过期或赠送超时未领取的券更新数据，用户id is {}", accountId);
            List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountId(accountId);
            Week week = DateUtil.getWeek(new Date());
            int hour = DateUtil.getHour(new Date());
            //不在周四权益时高峰时段时再检测是否有赠送超时未领取的优惠券
            if (!week.equals(Week.THURSDAY) || (week.equals(Week.THURSDAY) && hour > 14)) {
                logger.info("检查超时未领取的优惠券状态");
                accountCouponService.backSendingTimeOutCoupon(accountCoupons);
            }
            logger.info("检查过期优惠券状态");
            accountCoupons.stream().filter(accountCoupon -> AccountCouponService.NORMAL.equals(accountCoupon.getState()) && (new Date().getTime() - accountCoupon.getEndTime().getTime()) >= 0).forEach(accountCoupon -> {
                accountCoupon.setState(AccountCouponService.EXPIRED);
                accountCouponRepository.save(accountCoupon);
            });
        }
        accountCouponService.getRedisCoupon(accountId);
        Pageable pageable = WebUitl.buildPageRequest(nextPage, pageSize, null);
        Page<AccountCoupon> accountCouponPage = accountCouponRepository.findByAccountIdAndShowStatusAndState(accountId, "yes", state, pageable);
        return Result.success(accountCouponPage);
    }

    @RequestMapping("sendCou")
    public List<AccountCoupon> send(String accountId, String templateId, String num, String type) {
        SendCouponDomain domain = new SendCouponDomain();
        domain.setAccountId(accountId);
        domain.setNum(num);
        domain.setTemplateId(templateId);
        domain.setType(type);
        return accountCouponService.sendCoupon(domain, null);
    }

    /**
     * 查询我的可用优惠券列表
     */
    @RequestMapping(value = "getUsableCoupons", method = RequestMethod.GET)
    public Result getMyUsableCoupons(HttpServletRequest request, @RequestParam String shopId) {
        String accountId = (String) request.getAttribute("accountId");
        accountCouponService.getRedisCoupon(accountId);
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndShowStatusAndState(accountId, "yes", AccountCouponService.NORMAL);
        if (ObjectUtils.isEmpty(accountCoupons)) {
            logger.info("用户无优惠券！");
            return Result.fail("coupons_not_found", "用户无优惠券！");
        }
        Goods goods = goodsRepository.findOne(shopId);
        if (goods == null) {
            logger.info("商品不存在！");
            return Result.fail("shop_not_found", "商品不存在！");
        }
        List<AccountCoupon> couponList = accountCouponService.getAvailableCoupons(accountCoupons, goods.getPrice(), goods.getTags());
        return Result.success(getAccountCoupons(couponList));
    }

    private List<AccountCouponVo> getAccountCoupons(List<AccountCoupon> accountCoupons) {
        List<AccountCouponVo> accountCouponVos = new ArrayList<>();
        //封装话费兑换优惠券
        AccountCouponVo accountCouponVo_cost = new AccountCouponVo();
        accountCouponVo_cost.setType("cost_coupons");
        List<AccountCoupon> cost = accountCoupons.stream().filter(s -> s.getType().equals(AccountCoupon.TYPE_MOVE_COST)).collect(Collectors.toList());
        accountCouponVo_cost.setAccountCoupons(cost);
        accountCouponVo_cost.setNumber(cost.size());
        accountCouponVos.add(accountCouponVo_cost);
        //封装建行活动优惠券
        AccountCouponVo accountCouponVo_ccb = new AccountCouponVo();
        accountCouponVo_ccb.setType("ccb_coupons");
        List<AccountCoupon> ccb = accountCoupons.stream().filter(s -> s.getType().equals(AccountCoupon.TYPE_CCB)).collect(Collectors.toList());
        accountCouponVo_ccb.setAccountCoupons(ccb);
        accountCouponVo_ccb.setNumber(ccb.size());
        accountCouponVos.add(accountCouponVo_ccb);
        //封装优驾行活动优惠券
        AccountCouponVo accountCouponVo_yjx = new AccountCouponVo();
        accountCouponVo_yjx.setType("yjx_coupons");
        List<AccountCoupon> yjx = accountCoupons.stream().filter(s -> s.getType().equals(AccountCoupon.TYPE_YJX)).collect(Collectors.toList());
        accountCouponVo_yjx.setAccountCoupons(yjx);
        accountCouponVo_yjx.setNumber(yjx.size());
        accountCouponVos.add(accountCouponVo_yjx);
        //封装壳牌补贴优惠券
        AccountCouponVo accountCouponVo_shell = new AccountCouponVo();
        accountCouponVo_shell.setType("shell_coupons");
        List<AccountCoupon> shell = accountCoupons.stream().filter(s -> s.getType().equals(AccountCoupon.TYPE_SHELL)).collect(Collectors.toList());
        accountCouponVo_shell.setAccountCoupons(shell);
        accountCouponVo_shell.setNumber(shell.size());
        accountCouponVos.add(accountCouponVo_shell);
        return accountCouponVos;
    }

    /**
     * 查询赠送优惠券详情
     */
    @GetMapping("/getCouponInfo")
    public Result getCouponInfo(HttpServletRequest request, @RequestParam(value = "recordId") String recordId) {
        String accountId = (String) request.getAttribute("accountId");
        if (ObjectUtils.isEmpty(accountId) || ObjectUtils.isEmpty(recordId)) {
            return Result.fail("param_missing", "参数缺失");
        }
        //根据记录Id查询记录信息，获取优惠券id
        CouponSendRecord couponSendRecord = sendRecordRepository.findCouponSendRecordById(recordId);
        if (ObjectUtils.isEmpty(couponSendRecord)) {
            return Result.fail("sendRecord_not_found", "无赠送记录");
        }
        //根据优惠券id查询优惠券信息
        AccountCoupon accountCoupon = accountCouponRepository.findAccountCouponByCouponId(couponSendRecord.getCouponId());
        if (ObjectUtils.isEmpty(accountCoupon)) {
            logger.info("用户无优惠券");
            return Result.fail("coupon_not_found", "用户无优惠券！");
        }
        logger.info("====找到优惠券信息====");
        String sendAccountId = couponSendRecord.getSendAccountId();
        String receiveAccountId = couponSendRecord.getReceiveAccountId();
        //获取赠送人信息
        Account sendAccount = accountRepository.findAccountById(sendAccountId);
        logger.info("sendAccount==" + JSON.toJSONString(sendAccount));
        Account receiveAccount = null;
        if (!ObjectUtils.isEmpty(receiveAccountId)) {
            receiveAccount = accountRepository.findAccountById(receiveAccountId);
        }
        if (!ObjectUtils.isEmpty(receiveAccount)) {
            couponSendRecord.setReceiveName(receiveAccount.getNickName());
        }
        couponSendRecord.setAccountCoupon(accountCoupon);
        couponSendRecord.setSendName(sendAccount.getNickName());
        //判断赠送状态
        if (StringUtils.isEmpty(couponSendRecord.getReceiveAccountId()) && (new Date().getTime() - couponSendRecord.getSendTime().getTime()) >= 24 * 60 * 60 * 1000) {
            couponSendRecord.setStatus("back");
        } else if (!StringUtils.isEmpty(couponSendRecord.getReceiveAccountId())) {
            couponSendRecord.setStatus("received");
        } else if ((new Date().getTime() - accountCoupon.getEndTime().getTime()) >= 0) {
            couponSendRecord.setStatus("expired");
        } else if (StringUtils.isEmpty(couponSendRecord.getReceiveAccountId())) {
            couponSendRecord.setStatus(couponSendRecord.getSendAccountId().equals(accountId) ? "sending" : "normal");
        }
        return Result.success(couponSendRecord);
    }


}
