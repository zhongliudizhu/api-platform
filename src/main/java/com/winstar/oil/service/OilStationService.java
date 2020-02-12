package com.winstar.oil.service;

import com.alibaba.fastjson.JSON;
import com.winstar.oil.entity.OilStation;
import com.winstar.oil.repository.OilStationRepository;
import com.winstar.redis.OilRedisTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * author: uu
 * Classname: OilStationService
 * Description: TODO
 * Date: 2019/4/22 16:48
 */
@Service
public class OilStationService {

    private final OilStationRepository oilStationRepository;
    private final OilRedisTools oilRedisTools;

    /**
     * 赤道半径(单位m)
     */
    private static final double EARTH_RADIUS = 6371000;

    @Autowired
    public OilStationService(OilStationRepository oilStationRepository, OilRedisTools oilRedisTools) {
        this.oilStationRepository = oilStationRepository;
        this.oilRedisTools = oilRedisTools;
    }

    /**
     * 获取油站信息
     *
     * @param id 油站id
     * @return 油站
     */
    public OilStation getOilStation(String id) {
        OilStation oilStation = (OilStation) oilRedisTools.get("OilStation" + id);
        if (ObjectUtils.isEmpty(oilStation)) {
            oilStation = oilStationRepository.findOne(id);
            oilRedisTools.set("OilStation" + id, oilStation, null);
        }
        return oilStation;
    }


    /**
     * * 转化为弧度(rad)
     * *
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     *
     * @param lon1 第一点的经度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的经度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 获取
     */
    public KDTree getOilStationTree() {
        String stations = (String) oilRedisTools.get("oil_station_positions");
        double[][] arr = JSON.parseObject(stations, double[][].class);
        return KDTree.build(arr);
    }



}
