package com.winstar.couponActivity.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winstar.coupon.entity.MyCoupon;
import com.winstar.coupon.repository.MyCouponRepository;
import com.winstar.coupon.service.CouponService;
import com.winstar.couponActivity.entity.WhiteList;
import com.winstar.couponActivity.repository.WhiteListRepository;
import com.winstar.couponActivity.utils.*;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;

import com.winstar.couponActivity.entity.CouponActivity;
import com.winstar.couponActivity.repository.CouponActivityRepository;
import com.winstar.user.entity.Account;
import com.winstar.user.service.AccountService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * CouponActivityController
 *
 * @author: Big BB
 * @create 2018-03-16 9:39
 * @DESCRIPTION:
 **/
@RestController
@RequestMapping("/api/v1/cbc/couponActivities")
public class CouponActivityController {

    public static final Logger logger = LoggerFactory.getLogger(CouponActivityController.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;
    @Autowired
    CouponActivityRepository couponActivityRepository;
    @Autowired
    WhiteListRepository whiteListRepository;
    @Autowired
    MyCouponRepository myCouponRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value = "/findAll",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CouponActivity> findAll() throws NotFoundException{

        List<CouponActivity> list = getCouponActivity();
        if(ObjectUtils.isEmpty(list)){
              throw new NotFoundException("couponActivity.notActivity");
        }
        return list;
    }

    private List<CouponActivity> getCouponActivity(){
        List<CouponActivity> list = (List<CouponActivity>) couponActivityRepository.findAll();
        for (CouponActivity c : list) {
            c.setSendRule("");
            c.setUseRule(0.0);
        }
        return list;
    }

    @RequestMapping(value = "/giveCoupons", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<WhiteList>   giveCoupons(
            HttpServletRequest request, String driverLicense, String phoneNumber
    ) throws NotRuleException, NotFoundException {

        Object accountId = request.getAttribute("accountId");
        Account account = accountService.findById(accountId.toString());
        logger.info("openid:"+account.getOpenid());

        //白名单验证
        List<WhiteList> whiteLists = whiteListRepository.findByDriverLicenseAndPhoneNumberAndTime(driverLicense,phoneNumber,TimeUtil.getMonth());
        if(ObjectUtils.isEmpty(whiteLists)){
            throw new NotRuleException("couponActivity.notWhiteLists");
        }

        //匹配车辆
        if(compareCars(account, driverLicense)){
            throw new NotRuleException("couponActivity.notMatchCars");
        }
        List<WhiteList> getCouponLists =new LinkedList<>();
        //领取资格&发卷
        for (WhiteList w : whiteLists) {
            List<MyCoupon> coupons = null;
            if (w.getType() == ActivityIdEnum.ACTIVITY_ID_103.getActivity()
                    ||w.getType() == ActivityIdEnum.ACTIVITY_ID_104.getActivity()
                    ||w.getType() == ActivityIdEnum.ACTIVITY_ID_105.getActivity()){
                coupons = myCouponRepository.findByAccountIdAndActivityId(accountId, w.getType()+"");
            }else{
                coupons = myCouponRepository.findByAccountIdAndActivityIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(accountId, w.getType()+"", TimeUtil.getMonthStart(),TimeUtil.getMonthLast());
            }
            Random random = null;
            if(ObjectUtils.isEmpty(coupons)){
               CouponActivity couponActivity = couponActivityRepository.findOne(String.valueOf(w.getType()));
                int size = myCouponRepository.findByActivityId(couponActivity.getId()).size();
                if(size <= Integer.parseInt(couponActivity.getSendRule())){
                    random=new Random();
                    couponService.sendCoupon_freedom(
                            accountId.toString(),couponActivity.getId(),couponActivity.getAmount(),TimeUtil.getNextYear(),couponActivity.getUseRule(), couponActivity.getDescription()+"-"+ RandomUtil.getRandomNum(8), couponActivity.getName());
                    w.setCoupon(String.valueOf(couponActivity.getAmount()));
                    w.setSendTime(TimeUtil.getCurrentDateTime(TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE));
                    whiteListRepository.save(w);
                    getCouponLists.add(w);
                }
            }
        }
        return getCouponLists;
    }

    /**
     * 优驾行添加车辆比对六合一车辆
     *
     * @param account
     * @param driverLicense
     * @return
     */
    private boolean compareCars(Account account, String driverLicense){
        boolean flag = true;
        String token =  reqAccount(account.getOpenid(),"1");//获取优驾行Token
        List<String> localCars = reqCars(token);//获取添加车辆
        if (StringUtils.isEmpty(token)||StringUtils.isEmpty(localCars)){
            return true;
        }
        List<String> sixInOneCars  = reqSixInOneCars(driverLicense);//获取本人名下车辆
        if(StringUtils.isEmpty(sixInOneCars)){
            return true;
        }
        for (String  cars : sixInOneCars) {
            if(localCars.contains(cars)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static String getTokenInfoUrl = "http://192.168.118.111:7000/api/user/account/{userId}/{type}/getTokenInfo";
    /**
     * 获取优驾行token
     */
    private String reqAccount(String openid, String type) {
        Map<String ,String> urlVariables = new HashMap<String ,String>();
        urlVariables.put("userId", openid);
        urlVariables.put("type", type);
        ResponseEntity<String> tokenBody = RequestServerUtil.getRequest(restTemplate,getTokenInfoUrl, urlVariables);
        if(tokenBody.getStatusCode().value()==200){
            String accountContent = tokenBody.getBody().toString();
            JsonNode tokenNodeContentNode = null;
            try {
                tokenNodeContentNode = objectMapper.readTree(accountContent);
            } catch (IOException e) {
                return null;
            }
            JsonNode tokenNode = tokenNodeContentNode.path("token");

            return tokenNode.textValue();
        }
        return null;
    }

    private static String getLocalCarsUrl =  "http://192.168.118.111:7000/api/user/vehicle/getUserVehicleWechat";
    /**
     * 获取添加车辆
     */
    private  List<String> reqCars(String token) {
        if (StringUtils.isEmpty(token)) {
           return null;
        }
        Map<String, Object> reqOrderMap = new HashMap<>();

        List<String> carVos = new LinkedList<>();
        ResponseEntity<String> responseCars = RequestServerUtil.getRequestToken(restTemplate,getLocalCarsUrl,reqOrderMap,token);
        try {
            if(responseCars.getStatusCode().value()==200){
                String carsContent = responseCars.getBody().toString();
                JsonNode carsContentNode = objectMapper.readTree(carsContent);
                JsonNode carNode = carsContentNode.path("objData");
                Iterator<JsonNode> iterator = carNode.elements();
                while (iterator.hasNext()) {
                    JsonNode result = iterator.next();
                    JsonNode resultNode = objectMapper.readTree(result.toString());
                    carVos.add(resultNode.path("plateNumber").textValue().toString());
                }
            }
        } catch (IOException e) {
           return null;
        }
        return carVos;
    }



    private static String getSixInOneCarsUrl =  "http://mobile.sxwinstar.net/wechat_access/api/v1/platenumbers/certificateNumberTypeSearch?certificateNumber={certificateNumber}&&certificateType={certificateType}";
    private  List<String> reqSixInOneCars(String driverLicense) {
        if (StringUtils.isEmpty(driverLicense)) {
            return null;
        }
        Map<String, String> reqOrderMap = new HashMap<>();
        reqOrderMap.put("certificateNumber",driverLicense);
        reqOrderMap.put("certificateType","A");
        String token_id = "2b254bec-dd48-11e6-81f7-9457a5545c84";
        List<String> carVos = new LinkedList<>();

        try {
            ResponseEntity<String> responseCars = RequestServerUtil.getRequestFromToken(restTemplate,getSixInOneCarsUrl,reqOrderMap,token_id);
            if(responseCars.getStatusCode().value()==200){
                String carsContent = responseCars.getBody().toString();
                JsonNode carsContentNode  = objectMapper.readTree(carsContent);
                Iterator<JsonNode> iterator = carsContentNode.elements();
                while (iterator.hasNext()) {
                    JsonNode result = iterator.next();
                    JsonNode resultNode = objectMapper.readTree(result.toString());
                    carVos.add(resultNode.path("number").textValue().toString());
                }
            }
        } catch (IOException e) {
            return null;
        }
        return carVos;
    }
}
