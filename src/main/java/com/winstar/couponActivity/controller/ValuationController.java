package com.winstar.couponActivity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winstar.couponActivity.entity.SaleVehicleRecord;
import com.winstar.couponActivity.entity.VehicleValue;
import com.winstar.couponActivity.repository.SaleVehicleRecordRepository;
import com.winstar.couponActivity.repository.VehicleValueRepository;
import com.winstar.couponActivity.utils.CouponActivityUtil;
import com.winstar.couponActivity.utils.GrabUtils;
import com.winstar.couponActivity.utils.ParamJsonUtil;
import com.winstar.couponActivity.utils.ValuationReport;
import com.winstar.couponActivity.vo.SaleVehicleRecordParam;
import com.winstar.couponActivity.vo.ValuationParam;
import com.winstar.couponActivity.vo.VehicleDetail;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.order.utils.DateUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @Autowired
    SaleVehicleRecordRepository saleVehicleRecordRepository;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;


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
//        vehicleValue.setUpdateTime(DateUtil.getNowDate());
        vehicleValue.setValue(valuationParam.getPrice());
        vehicleValue = vehicleValueRepository.save(vehicleValue);

        return vehicleValue;
    }

    /**
     * 获取未来价格
     * @param request
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     */
    @RequestMapping(value = "priceFutureRecord", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
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

        //获取汽车未来价格趋势预测  json  数据
        String carFuturePrice=ValuationReport.getCarFuturePrice(modelId,zone,regDate,mile,restTemplate);
        if(StringUtils.isEmpty(carFuturePrice)){
            throw new NotFoundException("priceFutureRecord.not");
        }
       return carFuturePrice;
    }

    /**
     * 获取历史价格
     * @param request
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     */
    @RequestMapping(value = "priceHistoricalRecord", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPriceHistoricalRecord(HttpServletRequest request,String modelId,String regDate,String mile,String zone)
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

        //获取汽车历史价格趋势 json  数据
        String carHistoricalPrice = "";
        try {
            carHistoricalPrice= ValuationReport.getCarHistoricalPrice(modelId,zone,regDate,mile,restTemplate);
        } catch (Exception e) {
            carHistoricalPrice ="";
        }
        if(StringUtils.isEmpty(carHistoricalPrice)){
            throw new NotFoundException("priceHistoricalRecord.not");
        }
        return carHistoricalPrice;
    }

    /**
     * 出售汽车
     * @param request
     * @param valuationParam
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     */
    @RequestMapping(value = "saleVehicle", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
     public SaleVehicleRecord SaveSaleVehicle(HttpServletRequest request,@RequestBody SaleVehicleRecordParam valuationParam)throws NotFoundException, MissingParameterException{
        Object accountId = request.getAttribute("accountId");

        if (ObjectUtils.isEmpty(valuationParam)) {
            throw new MissingParameterException("SaleVehicleRecordParam.valuations");
        }
        if (ObjectUtils.isEmpty(valuationParam.getPhoneNumber())) {
            throw new MissingParameterException("valuations.getPhoneNumber");
        }
        if (StringUtils.isEmpty(valuationParam.getPrice())) {
            throw new MissingParameterException("valuations.getPrice");
        }
        if (StringUtils.isEmpty(valuationParam.getSaleTime())) {
            throw new MissingParameterException("valuations.getSaleTime");
        }
        if (StringUtils.isEmpty(valuationParam.getPlateNumber())) {
            throw new MissingParameterException("valuations.getPlateNumber");
        }

        SaleVehicleRecord saleVehicleRecord = saleVehicleRecordRepository.findByAccountId(accountId.toString());
        if(ObjectUtils.isEmpty(saleVehicleRecord)){
            saleVehicleRecord = new SaleVehicleRecord();
        }
        saleVehicleRecord.setAccountId(accountId.toString());
        saleVehicleRecord.setCreateTime(new Date());
        saleVehicleRecord.setPhoneNumber(valuationParam.getPhoneNumber());
        saleVehicleRecord.setPrice(valuationParam.getPrice());
        saleVehicleRecord.setSaleTime(valuationParam.getSaleTime());
        saleVehicleRecord.setPlateNumber(valuationParam.getPlateNumber());
        saleVehicleRecordRepository.save(saleVehicleRecord);
        return saleVehicleRecord;
    }

    /**
     * 查询车辆详情
     * @param request
     * @param modelId
     * @param plateNumber
     * @param engineNumber
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     * @throws NotRuleException
     */
    @RequestMapping(value = "getVehicleDetail", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDetail getVehicleDetail(HttpServletRequest request,
                                          String modelId, String plateNumber, String engineNumber
                                          )throws NotFoundException, MissingParameterException,NotRuleException{

        if (ObjectUtils.isEmpty(modelId)) {
            throw new MissingParameterException("valuations.modelId");
        }
        if (StringUtils.isEmpty(plateNumber)) {
            throw new MissingParameterException("valuations.plateNumber");
        }
        if (StringUtils.isEmpty(engineNumber)) {
            throw new MissingParameterException("valuations.engineNumber");
        }

        if(CouponActivityUtil.testVehicle(plateNumber,engineNumber,restTemplate,objectMapper)){
            throw new NotRuleException("notMatchEngineNumber");
        }
        VehicleDetail vehicleDetail = new VehicleDetail();
        String getModelParameters = ValuationReport.getModelParameters(modelId,restTemplate);
        Map<String,String> map = ParamJsonUtil.AnalysisParamJsonUtil(getModelParameters);
        vehicleDetail.setBrand(map.get("brand"));
        vehicleDetail.setEnvironmental(map.get("environmental"));
        vehicleDetail.setDisplacement(map.get("displacement"));
        vehicleDetail.setPower(map.get("power"));
        return vehicleDetail;
    }

    /**
     * 获取汽车所有指导价
     * @param request
     * @return
     * @throws NotFoundException
     * @throws MissingParameterException
     */
    @RequestMapping(value = "getUsedCarPrice", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsedCarPrice(HttpServletRequest request,String modelId,String regDate,String mile,String zone)
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

        //获取汽车所有指导价
        String getUsedCarPrice = "";
        try {
            getUsedCarPrice= ValuationReport.getUsedCarPrice(modelId,zone,regDate,mile,restTemplate);
        } catch (Exception e) {
            getUsedCarPrice ="";
        }
        if(StringUtils.isEmpty(getUsedCarPrice)){
            throw new NotFoundException("priceHistoricalRecord.not");
        }
        return getUsedCarPrice;
    }
}
