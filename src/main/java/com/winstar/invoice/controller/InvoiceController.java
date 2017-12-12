package com.winstar.invoice.controller;

import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.invoice.entity.Invoice;
import com.winstar.invoice.repository.InvoiceRepository;
import com.winstar.invoice.utils.InvoiceUtil;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.repository.OilOrderRepository;
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
 * @Describe： 发票
 */
@RestController
@RequestMapping("/api/v1/orders/invoice")
public class InvoiceController {
    public static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private OilOrderRepository oilOrderRepository;

    /*
    * 保存发票信息
    * */
    @RequestMapping(method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public ResponseEntity saveInvoice(@RequestBody Invoice invoice, HttpServletRequest request, @RequestParam String orderSerialNo)
            throws MissingParameterException, NotFoundException, NotRuleException {

        if(StringUtils.isEmpty(orderSerialNo)){
            throw new MissingParameterException("orderSerialNo.invoice");
        }

        Invoice origin = invoiceRepository.findByOrderSerialNo(orderSerialNo);
        if(!ObjectUtils.isEmpty(origin)){
            throw new NotRuleException("alreadyExist.invoice");
        }

        String paramStr = InvoiceUtil.judgeParas(invoice);
        if(!paramStr.equals("ok")){
            throw new MissingParameterException(paramStr+".invoice");
        }

        OilOrder oilOrder = oilOrderRepository.findBySerialNo(orderSerialNo);
        if(ObjectUtils.isEmpty(oilOrder)){
            throw new NotFoundException("oilOrder.invoice");
        }
        if(oilOrder.getPayStatus()!=1){
                throw new NotRuleException("notPay.invoice");
        }
        invoice = InvoiceUtil.initInvoice(invoice,oilOrder);
        invoice = invoiceRepository.save(invoice);
        return  new ResponseEntity(invoice, HttpStatus.OK);
    }

    /*
    * 查询发票
    * */
    @RequestMapping(method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public ResponseEntity getOne(@RequestParam String orderSerialNo)
            throws MissingParameterException, NotFoundException, NotRuleException {

        if(StringUtils.isEmpty(orderSerialNo)){
            throw new MissingParameterException("orderSerialNo.invoice");
        }

        Invoice origin = invoiceRepository.findByOrderSerialNo(orderSerialNo);
        if(ObjectUtils.isEmpty(origin)){
            throw new NotFoundException("invoice.invoice");
        }
        return  new ResponseEntity(origin, HttpStatus.OK);
    }

    /***
     *修改发票信息
     */
    @RequestMapping(value = "" ,method = RequestMethod.PUT,produces = "application/json;charset=utf-8")
    public ResponseEntity updateInvoice(@RequestBody Invoice invoice, HttpServletRequest request, @RequestParam String orderSerialNo)
            throws MissingParameterException, NotFoundException, NotRuleException {

        if(StringUtils.isEmpty(orderSerialNo)){
            throw new MissingParameterException("orderSerialNo.invoice");
        }
        Invoice origin = invoiceRepository.findByOrderSerialNo(orderSerialNo);
        if(ObjectUtils.isEmpty(origin)){
           throw new NotFoundException("invoice.invoice");
        }
        if(origin.getStatus()!= 0){
            throw new NotRuleException("cannotEdit.invoice");
        }
        BeanUtils.copyProperties(invoice,origin,new String[]{"id","oilTotalValue","payPrice","orderSerialNo","createTime","status"});
        String paramStr = InvoiceUtil.judgeParas(invoice);
        if(!paramStr.equals("ok")){
            throw new MissingParameterException(paramStr+".invoice");
        }
        origin.setUpdateTime(new Date());
        origin.setIsDel("0");
        invoice = invoiceRepository.save(origin);
        return  new ResponseEntity(invoice, HttpStatus.OK);
    }

    /***
     *重开发票
     */
    @RequestMapping(value = "/reInvoice" ,method = RequestMethod.PUT,produces = "application/json;charset=utf-8")
    public ResponseEntity reInvoice(@RequestBody Invoice invoice, HttpServletRequest request, @RequestParam String orderSerialNo)
            throws MissingParameterException, NotFoundException, NotRuleException {

        if(StringUtils.isEmpty(orderSerialNo)){
            throw new MissingParameterException("orderSerialNo.invoice");
        }
        Invoice origin = invoiceRepository.findByOrderSerialNo(orderSerialNo);
        if(ObjectUtils.isEmpty(origin)){
            throw new NotFoundException("invoice.invoice");
        }
        if(origin.getStatus()!=1){
            throw new NotRuleException("notFinish.invoice");
        }
        if(invoice.getType()!=origin.getType()){
            throw new NotRuleException("typeCannotEdit.invoice");
        }
        String paramStr = InvoiceUtil.judgeParas(invoice);
        if(!paramStr.equals("ok")){
            throw new MissingParameterException(paramStr+".invoice");
        }
        if(!StringUtils.isEmpty(invoice.getPersonName())&&!invoice.getPersonName().equals(origin.getPersonName())){
            throw new NotRuleException("personNameCannotEdit.invoice");
        }
        //备份原始发票:即设置为已删除
        Invoice newInvoice = new Invoice();
        BeanUtils.copyProperties(origin,newInvoice,new String[]{"id"});
        newInvoice.setIsDel("1");
        invoiceRepository.save(newInvoice);

        BeanUtils.copyProperties(invoice,origin,new String[]{"id","oilTotalValue","payPrice","orderSerialNo","createTime"});
        origin.setUpdateTime(new Date());
        origin.setStatus(3);//申请重开
        origin.setIsDel("0");
        invoice = invoiceRepository.save(origin);
        return  new ResponseEntity(invoice, HttpStatus.OK);
    }

}
