package com.winstar.couponActivity.controller;

import com.winstar.couponActivity.entity.VehicleValue;
import com.winstar.couponActivity.repository.VehicleValueRepository;
import com.winstar.couponActivity.utils.GrabUtils;
import com.winstar.couponActivity.utils.ValuationReport;
import com.winstar.couponActivity.vo.ValuationParam;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * valuation controller
 *
 * @author gradle
 */
@RestController
@RequestMapping("/api/v1/cbc/valuations")
public class ValuationController {

    @Autowired
    VehicleValueRepository vehicleValueRepository;
    /**
     * 车辆估值计算
     *
     * @param valuationParam 　估值参数
     * @return 估值信息
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public VehicleValue valuation(@RequestBody ValuationParam valuationParam) throws NotFoundException, MissingParameterException {
        if (ObjectUtils.isEmpty(valuationParam)) {
            throw new MissingParameterException("valuationParam.valuations");
        }
        if (ObjectUtils.isEmpty(valuationParam.getPlateNumber())) {
            throw new MissingParameterException("plateNumber.valuations");
        }
        if (StringUtils.isEmpty(valuationParam.getModelId())) {
            throw new MissingParameterException("modelId.valuations");
        }
        if (StringUtils.isEmpty(valuationParam.getRegDate())) {
            throw new MissingParameterException("regDate.valuations");
        }
        if (StringUtils.isEmpty(valuationParam.getMile())) {
            throw new MissingParameterException("mile.valuations");
        }
        if (StringUtils.isEmpty(valuationParam.getZone())) {
            throw new MissingParameterException("zone.valuations");
        }
        if (StringUtils.isEmpty(valuationParam.getPrice())) {
            throw new MissingParameterException("price.valuations");
        }
//        Map map = GrabUtils.valuation(valuationParam.getModelId(), valuationParam.getRegDate(), valuationParam.getMile(), valuationParam.getZone());
//        if (ObjectUtils.isEmpty(map)) {
//            throw new NotFoundException("valuation.item");
//        }
//        String status = MapUtils.getString(map, "status");
        VehicleValue vehicleValue =  vehicleValueRepository.findByHphm(valuationParam.getPlateNumber());
        if(ObjectUtils.isEmpty(vehicleValue)){
           throw new NotFoundException("vehicleValue.notFound");
        }
        vehicleValue.setValue(valuationParam.getPrice());
        vehicleValue = vehicleValueRepository.save(vehicleValue);

        return vehicleValue;
    }

    /**
     * 获取历史价格
     * @param request
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     */
    @RequestMapping(value = "priceRecord", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPriceRecord(HttpServletRequest request,String modelId,String regDate,String mile,String zone)
                  throws NotFoundException, MissingParameterException{
        if (StringUtils.isEmpty(modelId)) {
            throw new MissingParameterException("modelId.valuations");
        }
        if (StringUtils.isEmpty(regDate)) {
            throw new MissingParameterException("regDate.valuations");
        }
        if (StringUtils.isEmpty(mile)) {
            throw new MissingParameterException("mile.valuations");
        }
        if (StringUtils.isEmpty(zone)) {
            throw new MissingParameterException("zone.valuations");
        }

        ValuationReport c= new ValuationReport();
        //获取汽车历史价格趋势 json  数据
        String carHistoricalPrice = "";
        try {
            carHistoricalPrice= c.getCarHistoricalPrice(modelId,zone,regDate,mile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取汽车未来价格趋势预测  json  数据
        String carFuturePrice=c.getCarFuturePrice(modelId,zone,regDate,mile);
       return carFuturePrice;
    }
}
