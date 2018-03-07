package com.winstar.invoice.controller;

import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.invoice.entity.Invoice;
import com.winstar.invoice.repository.InvoiceRepository;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
import com.winstar.order.utils.DateUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author shoo on 2017/10/23 15:16.
 *  发票
 */
@RestController
@RequestMapping("/api/v1/orders/invoice")
public class InvoiceController {
    public static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    @Autowired
    private InvoiceRepository invoiceRepository;


    /*
    * 回调通知地址
    * */
    @RequestMapping(value = "/callBack", method = RequestMethod.POST,produces = "application/json;charset=utf-8")

    public boolean callBack( @RequestParam String result,String orderNumber)
            throws MissingParameterException, NotFoundException, NotRuleException {
        if(StringUtils.isEmpty(result)){
            throw new MissingParameterException("result");
        }

        JSONObject json=JSONObject.fromObject(result);

        if(json.containsKey("Result") && "1000".equals(json.getString("Result"))){
            Invoice invoice=invoiceRepository.findByOrderNumber(orderNumber);
            if(invoice==null) invoice=new Invoice();
            invoice.setBillingType(json.getString("BillingType"));
            invoice.setFpDm(json.getString("FpDm"));
            invoice.setFpHm(json.getString("FpHm"));
            invoice.setOrderNumber(json.getString("OrderNumber"));
            invoice.setPdfUrl(json.getString("PdfUrl"));
            invoice.setResult(json.getString("Result"));
            invoice.setTime(DateUtil.StringToDate(json.getString("Time"),"yyyy-MM-dd hh:mm:ss"));
            invoice.setSendTime(DateUtil.StringToDate(json.getString("SendTime"),"yyyy-MM-dd hh:mm:ss"));
            invoiceRepository.save(invoice);

        }
        //失败
        if(json.containsKey("returnCode")){
            Invoice invoice=new Invoice();
            invoice.setOrderNumber(orderNumber);
            invoice.setResult(json.getString("returnCode"));
            invoiceRepository.save(invoice);
            return false;
        }

        return true;
    }

    /*
    * 查询发票
    * */
    @RequestMapping(method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public ResponseEntity getOne(@RequestParam String orderNumber)
            throws MissingParameterException, NotFoundException, NotRuleException {

        if(StringUtils.isEmpty(orderNumber)){
            throw new MissingParameterException("orderSerialNo.invoice");
        }

        Invoice origin = invoiceRepository.findByOrderNumber(orderNumber);
        if(ObjectUtils.isEmpty(origin)){
            throw new NotFoundException("invoice.invoice");
        }
        return  new ResponseEntity(origin, HttpStatus.OK);
    }



}
