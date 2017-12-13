package com.winstar.oil.service;

import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.entity.OilCoupon;
import com.winstar.oil.repository.MyOilCouponRepository;
import com.winstar.oil.repository.OilCouponRepository;
import com.winstar.utils.WsdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zl on 2017/10/18
 */
@Service
public class OilCouponUpdateService {

    @Autowired
    MyOilCouponRepository myOilCouponRepository;

    @Autowired
    OilCouponRepository oilCouponRepository;

    @Transactional
    public boolean updateOilCoupon(MyOilCoupon myOilCoupon, OilCoupon oilCoupon){
        try{
            if(WsdUtils.isNotEmpty(oilCoupon)){
                oilCoupon.setOilState("1");
                oilCoupon.setDistributionTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                oilCouponRepository.save(oilCoupon);
            }
            if(WsdUtils.isNotEmpty(myOilCoupon)){
                myOilCoupon.setPan(oilCoupon.getPan());
                myOilCouponRepository.save(myOilCoupon);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
