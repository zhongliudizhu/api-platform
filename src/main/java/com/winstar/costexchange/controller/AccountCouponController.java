package com.winstar.costexchange.controller;

import com.winstar.costexchange.entity.AccountCoupon;
import com.winstar.costexchange.repository.AccountCouponRepository;
import com.winstar.costexchange.utils.SignUtil;
import com.winstar.costexchange.vo.AccountCouponVo;
import com.winstar.shop.entity.Goods;
import com.winstar.shop.repository.GoodsRepository;
import com.winstar.utils.WebUitl;
import com.winstar.vo.Result;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zl on 2019/5/27
 */
@RestController
@RequestMapping("/api/v1/cbc/cost")
@Slf4j
public class AccountCouponController {

    private static final Logger logger = LoggerFactory.getLogger(AccountCouponController.class);

    @Autowired
    AccountCouponRepository accountCouponRepository;

    @Autowired
    GoodsRepository goodsRepository;

    /**
     * 查询优惠券列表
     */
    @RequestMapping(value = "getCoupons", method = RequestMethod.GET)
    public Result getCoupons(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer nextPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "normal") String state){
        if(!state.equals("normal") && !state.equals("used") && !state.equals("expired")){
            logger.info("状态值错误！");
            return Result.fail("state_not_auth", "状态值错误！");
        }
        String accountId = (String) request.getAttribute("accountId");
        Pageable pageable = WebUitl.buildPageRequest(nextPage, pageSize, null);
        Page<AccountCoupon> accountCouponPage = accountCouponRepository.findByAccountIdAndShowStatusAndState(accountId, "yes", state, pageable);
        accountCouponPage.getContent().stream().filter(accountCoupon -> "normal".equals(accountCoupon.getState()) && (new Date().getTime() - accountCoupon.getEndTime().getTime()) >= 0).forEach(accountCoupon -> {
            accountCoupon.setState("expired");
            accountCouponRepository.save(accountCoupon);
        });
        return Result.success(accountCouponPage);
    }

    /**
     * 查询我的可用优惠券列表
     */
    @RequestMapping(value = "getUsableCoupons", method = RequestMethod.GET)
    public Result getMyUsableCoupons(HttpServletRequest request, @RequestParam String shopId){
        String accountId = (String) request.getAttribute("accountId");
        List<AccountCoupon> accountCoupons = accountCouponRepository.findByAccountIdAndShowStatus(accountId, "yes");
        if(ObjectUtils.isEmpty(accountCoupons)){
            logger.info("用户无优惠券！");
            return Result.fail("coupons_not_found", "用户无优惠券！");
        }
        Goods goods = goodsRepository.findOne(shopId);
        if (goods == null){
            logger.info("商品不存在！");
            return Result.fail("shop_not_found", "商品不存在！");
        }
        String couponIds = accountCoupons.stream().map(AccountCoupon::getCouponId).collect(Collectors.joining(","));
        logger.info("couponIds:" + couponIds);
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ids", couponIds);
        reqMap.put("itemAmount", goods.getPrice().toString());
        reqMap.put("merchant", SignUtil.merchant);
        ResponseEntity<Map> resp = new RestTemplate().getForEntity("http://localhost:12002/api/v1/coupon/verify/verify?" + SignUtil.getParameters(reqMap), Map.class);
        logger.info("map:" + resp.getBody().toString());
        return Result.success(getAccountCoupons(accountCoupons));
    }

    private List<AccountCouponVo> getAccountCoupons(List<AccountCoupon> accountCoupons){
        List<AccountCouponVo> accountCouponVos = new ArrayList<>();
        //封装话费兑换优惠券
        AccountCouponVo accountCouponVo_cost = new AccountCouponVo();
        accountCouponVo_cost.setType("cost_coupons");
        List<AccountCoupon> cost = accountCoupons.stream().filter(s -> s.getType().equals("moveCost")).collect(Collectors.toList());
        accountCouponVo_cost.setAccountCoupons(cost);
        accountCouponVo_cost.setNumber(cost.size());
        accountCouponVos.add(accountCouponVo_cost);
        //封装建行活动优惠券
        AccountCouponVo accountCouponVo_ccb = new AccountCouponVo();
        accountCouponVo_ccb.setType("ccb_coupons");
        List<AccountCoupon> ccb = accountCoupons.stream().filter(s -> s.getType().equals("ccb")).collect(Collectors.toList());
        accountCouponVo_ccb.setAccountCoupons(ccb);
        accountCouponVo_ccb.setNumber(ccb.size());
        accountCouponVos.add(accountCouponVo_ccb);
        //封装优驾行活动优惠券
        AccountCouponVo accountCouponVo_yjx = new AccountCouponVo();
        accountCouponVo_yjx.setType("yjx_coupons");
        List<AccountCoupon> yjx = accountCoupons.stream().filter(s -> s.getType().equals("yjx")).collect(Collectors.toList());
        accountCouponVo_yjx.setAccountCoupons(yjx);
        accountCouponVo_yjx.setNumber(yjx.size());
        accountCouponVos.add(accountCouponVo_yjx);
        return accountCouponVos;
    }

}
