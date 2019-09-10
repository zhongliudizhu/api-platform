package com.winstar.oil.service;

import com.winstar.oil.entity.OilStation;
import com.winstar.oil.repository.OilStationRepository;
import com.winstar.redis.RedisTools;
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
    private RedisTools redisTools;

    public OilStation getOilStation(String id) {
        OilStation oilStation = (OilStation) redisTools.get("OilStation" + id);
        if (ObjectUtils.isEmpty(oilStation)) {
            oilStation = oilStationRepository.findOne(id);
            redisTools.set("OilStation" + id, oilStation);
        }
        return oilStation;
    }

}
