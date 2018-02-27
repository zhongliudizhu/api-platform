package com.winstar.order.controller;

import com.winstar.coupon.service.CouponService;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.entity.CouponLog;
import com.winstar.order.entity.Insurance;
import com.winstar.order.repository.CouponLogRepository;
import com.winstar.order.repository.InsuranceRepository;
import com.winstar.order.service.LoveActivityService;
import com.winstar.order.utils.DateUtil;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author shoo on 2018/2/2 17:43.
 *         -- 爱心活动
 */
@RestController
@RequestMapping(value = "/api/v1/cbc/love")
public class LoveActivityController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponLogRepository couponLogRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LoveActivityService loveActivityService;
    @Autowired
    private InsuranceRepository insuranceRepository;

    /**
     * 发优惠券
     * @return 0 or 1
     */
    @PostMapping(value = "/giveCoupon", produces = "application/json;charset=utf-8")
    public ResponseEntity giveCoupon( HttpServletRequest request ) throws NotFoundException, NotRuleException {

        String accountId = accountService.getAccountId(request);
        Account account = accountService.findById(accountId);
        List<CouponLog> couponLogs = couponLogRepository.findByAccountId(accountId);
        String result = "0";
        if(couponLogs.size()<=0){
            result = loveActivityService.giveCoupon(accountId, account.getOpenid());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 查询保险
     * @return 0 已领取   1 已领完   2 可以领取
     */
    @ResponseBody
    @GetMapping(value = "/insurance", produces = "application/json;charset=utf-8")
    public ResponseEntity getInsurance( HttpServletRequest request ) throws NotFoundException, NotRuleException {
        String accountId = accountService.getAccountId(request);
        List<Insurance> insurances = insuranceRepository.findByAccountId(accountId);
        if(insurances.size()>0){
            return new ResponseEntity<>("0",HttpStatus.OK);
        }
        List<Insurance> today = insuranceRepository.findByCreateTimeBetween(DateUtil.getDayBegin(), new Date());
        List<Insurance> total = insuranceRepository.findAll();
        int num = 200;
        Date date = new Date(1518537601000l);
        Date time = new Date();
        if(time.after(new Date(1519574401000L))&&time.before(new Date(1519831799000L))){
            num = 1000;
        }
        if(today.size()>=num){
            return new ResponseEntity<>("1",HttpStatus.OK);
        }
        if(total.size()>3000){
            return new ResponseEntity<>("3",HttpStatus.OK);
        }
        return new ResponseEntity<>("2", HttpStatus.OK);
    }

    /**
     * 保存保险
     * @param insurance 保险参数
     * @return 保险
     */
    @PostMapping(value = "/insurance", produces = "application/json;charset=utf-8")
    public ResponseEntity saveInsurance( HttpServletRequest request, @RequestBody Insurance insurance) throws MissingParameterException, NotRuleException, NotFoundException {
        if( StringUtils.isEmpty(insurance.getPersonName()) || StringUtils.isEmpty(insurance.getIdentNumber()) || StringUtils.isEmpty(insurance.getEmail()) || StringUtils.isEmpty(insurance.getPhoneNumber())){
            throw new MissingParameterException("missPara.love");
        }
        String accountId = accountService.getAccountId(request);
        List<Insurance> insurances = insuranceRepository.findByAccountId(accountId);
        if(insurances.size()>0){
            throw new NotRuleException("exists.love");
        }
        insurance.setCreateTime(new Date());
        insurance.setIsSend("0");
        insurance.setAccountId(accountId);
        Insurance ins = insuranceRepository.save(insurance);
        return new ResponseEntity<>(ins, HttpStatus.OK);
    }

}
