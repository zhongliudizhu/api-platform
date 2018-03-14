package com.winstar.invoice.controller;


import com.sun.xml.internal.ws.encoding.ImageDataContentHandler;
import com.winstar.exception.*;
import com.winstar.invoice.entity.Invoice;
import com.winstar.invoice.repository.InvoiceRepository;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.service.MyOilCouponService;
import com.winstar.shop.entity.Activity;
import com.winstar.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shoo on 2017/10/23 15:16.
 * 发票
 */
@RestController
@RequestMapping("/api/v1/orders/invoice")
public class InvoiceController {
    public static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    MyOilCouponService myOilCouponService;

    @Autowired
    AccountService accountService;

    /**
     * 未开发票的油卷
     * @return
     * @throws MissingParameterException
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyOilCoupon> query(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer nextPage,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException,
            ServiceUnavailableException {
        String accountId = accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) throw new NotFoundException("MyOilCoupon");
        List<Invoice> invoices=invoiceRepository.findByAccountId(accountId);
        List<String> ids=new ArrayList<>();
         if(invoices.size()>0){
            for(Invoice invoice:invoices){
                ids.add(invoice.getPan());
            }
        }else{
             ids.add("");
         }
        Sort sort = new Sort(Sort.Direction.DESC, "useDate");
        Pageable pageable = new PageRequest(nextPage, pageSize, sort);

        Page<MyOilCoupon> page = myOilCouponService.findUsedCoupon(accountId,ids, pageable);
        List<MyOilCoupon> list = page.getContent();

        if (list.size() == 0) throw new NotFoundException("MyOilCoupon");

        return list;
    }

    /**
     * 审请开票
     */
    @RequestMapping(value = "/makeInvoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Invoice makeInvoice(
            HttpServletRequest request,
            String pan,
            Integer type,
            String name,String oilType,String email,String phone,String companyName,String taxpayerNumber
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException,
            ServiceUnavailableException {
        String accountId = accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) throw new NotFoundException("MyOilCoupon");
        if(type==null) throw new MissingParameterException("type");
        if(pan==null) throw new MissingParameterException("pan");
        if(oilType==null) throw new MissingParameterException("oilType");
        if(email==null) throw new MissingParameterException("email");
        if(phone==null) throw new MissingParameterException("phone");
        Invoice invoice=new Invoice();
        if(type==1){
            if(name==null) throw new MissingParameterException("name");
            invoice.setName(name);
        }

        if(type==2){
            if(companyName==null) throw new MissingParameterException("companyName");
            if(taxpayerNumber==null) throw new MissingParameterException("taxpayerNumber");
            invoice.setCompanyName(companyName);
            invoice.setTaxpayerNumber(taxpayerNumber);
        }

        invoice.setAccountId(accountId);
        invoice.setType(type);
        invoice.setPan(pan);
        invoice.setOilType(oilType);
        invoice.setEmail(email);
        invoice.setPhone(phone);
        invoice.setStatus(0);
       Invoice in= invoiceRepository.save(invoice);

       return in;

    }

    /**
     * 查询我开票历史
     * @param request
     * @param nextPage
     * @param pageSize
     * @return
     * @throws MissingParameterException
     * @throws InvalidParameterException
     * @throws NotRuleException
     * @throws NotFoundException
     * @throws ServiceUnavailableException
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> history(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer nextPage,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException,
            ServiceUnavailableException {
        String accountId = accountService.getAccountId(request);

        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable pageable = new PageRequest(nextPage, pageSize, sort);

        Page<Invoice> page = invoiceRepository.findByAccountId(accountId, pageable);
        if (page.getContent().size() == 0) throw new NotFoundException("Invoice");


        return page.getContent();
    }

}
