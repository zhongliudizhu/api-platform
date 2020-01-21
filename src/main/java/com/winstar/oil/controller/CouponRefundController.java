package com.winstar.oil.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.SearchOilCoupon;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.entity.OilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.oil.service.MyOilCouponService;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.utils.AESUtil;
import com.winstar.vo.RefundOpenCoupon;
import com.winstar.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/noAuth")
@Slf4j
public class CouponRefundController {

    @Autowired
    private MyOilCouponRepository myOilCouponRepository;
    @Autowired
    private OilOrderRepository oilOrderRepository;
    @Autowired
    private MyOilCouponService myOilCouponService;
    @Autowired
    private OilCouponRepository oilCouponRepository;

    @Value("${info.cardUrl}")
    private String oilSendUrl;

    @Value("${info.cardUrl_new}")
    private String oilSendNewUrl;

    @RequestMapping(value = "/refundCoupon", method = RequestMethod.POST)
    public Result refundCoupon(@RequestParam String serialNumber, @RequestParam String mark) {
        log.info("入参信息为, serialNumber is {},mark is {}", serialNumber, mark);
        OilOrder oilOrder = oilOrderRepository.findBySerialNumber(serialNumber);
        if (ObjectUtils.isEmpty(oilOrder)) {
            log.info("订单号有误,无相应订单信息");
            return Result.fail("order_id_error", "订单号有误");
        }
        List<MyOilCoupon> myOilCoupons = myOilCouponRepository.findByOrderIdAndUseStateAndTIdIsNull(serialNumber, "0");
        if (CollectionUtils.isEmpty(myOilCoupons)) {
            log.info("当前用户无未使用油券,无法退款");
            return Result.fail("oilCoupon_is_used", "当前用户无未使用油券,无法退款");
        }
        Set<String> panSets = myOilCoupons.stream().map(MyOilCoupon::getPan).filter(Objects::nonNull).collect(Collectors.toSet());
        log.info("panSets is {}", panSets);
        List<RefundOpenCoupon> couponList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(panSets)) {
            //获取油券导入时间
            List<OilCoupon> oilCoupons = oilCouponRepository.findByPanIn(panSets);
            Map<String, List<OilCoupon>> listMap = oilCoupons.stream().collect(Collectors.groupingBy(OilCoupon::getPan));
            Map<String, String> map = new HashMap<>();
            listMap.entrySet().forEach(s -> {
                String createTime = s.getValue().get(0).getCreateTime();
                map.put(s.getKey(), createTime);
            });
            Map<String, String> normalMap = new HashMap<>();
            //解密油券
            map.entrySet().forEach(m -> {
                String s = null;
                try {
                    s = AESUtil.decrypt(m.getKey(), AESUtil.dekey);
                } catch (Exception e) {
                    log.error("解密错误");
                    e.printStackTrace();
                }
                normalMap.put(s, m.getValue());
            });
            log.info("明文油券信息为 {}", JSON.toJSONString(normalMap));
            //调用亿通接口查询油券使用情况
            normalMap.entrySet().forEach(n -> {
                Map tem = SearchOilCoupon.verification(n.getKey().length() == 20 ? oilSendNewUrl : oilSendUrl, n.getKey());
                if (MapUtils.getString(tem, "rc").equals("00") && MapUtils.getString(tem, "cardStatus").equals("3")) {
                    RefundOpenCoupon coupon = new RefundOpenCoupon();
                    coupon.setPan(n.getKey());
                    coupon.setCouponExportTime(n.getValue());
                    couponList.add(coupon);
                }
                if (MapUtils.getString(tem, "rc").equals("00") && MapUtils.getString(tem, "cardStatus").equals("1")) {
                    MyOilCoupon oilCoupon = null;
                    try {
                        String pan = AESUtil.encrypt(n.getKey(), AESUtil.key);
                        oilCoupon = myOilCouponRepository.findByPan(pan);
                        myOilCoupons.removeIf(s -> s.getPan().equals(pan));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String txnDate = MapUtils.getString(tem, "txnDate");
                    oilCoupon.setUseState(MapUtils.getString(tem, "cardStatus"));
                    oilCoupon.setTId(MapUtils.getString(tem, "tid"));
                    String txnTime = MapUtils.getString(tem, "txnTime");
                    oilCoupon.setUseDate(formatTxnDateAndTime(txnDate, txnTime));
                    myOilCouponRepository.save(oilCoupon);
                }
            });
        }
        log.info("已打开的油券记录为 {}", JSON.toJSONString(couponList));
        //更新油券信息及订单信息
        myOilCoupons.forEach(s -> {
            s.setAccountId("0000000065a7c3220165a85554d40366");
            s.setUseState("1");
            s.setTId("已退款");
        });
        oilOrder.setAccountId("0000000065a7c3220165a85554d40366");
        oilOrder.setMark(mark);
        myOilCouponService.saveOrderAndMyOilCoupon(myOilCoupons, oilOrder);
        return Result.success(couponList);
    }

    private String formatTxnDateAndTime(String txnDate, String txnTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = simple.parse(txnDate + txnTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }


}
