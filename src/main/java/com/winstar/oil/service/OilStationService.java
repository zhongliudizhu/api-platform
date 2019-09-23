package com.winstar.oil.service;

import com.winstar.oil.entity.OilStation;
import com.winstar.oil.repository.OilStationRepository;
import com.winstar.redis.CouponRedisTools;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * author: uu
 * Classname: OilStationService
 * Description: TODO
 * Date: 2019/4/22 16:48
 */
@Service
@AllArgsConstructor
public class OilStationService {
    private OilStationRepository oilStationRepository;
    private CouponRedisTools couponRedisTools;

    public OilStation getOilStation(String id) {
        OilStation oilStation = (OilStation) couponRedisTools.get("OilStation" + id);
        if (ObjectUtils.isEmpty(oilStation)) {
            oilStation = oilStationRepository.findOne(id);
            couponRedisTools.set("OilStation" + id, oilStation, null);
        }
        return oilStation;
    }

}
