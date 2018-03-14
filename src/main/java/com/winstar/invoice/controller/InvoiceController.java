package com.winstar.invoice.controller;


import com.sun.xml.internal.ws.encoding.ImageDataContentHandler;
import com.winstar.exception.*;
import com.winstar.invoice.entity.Invoice;
import com.winstar.invoice.entity.InvoiceItem;
import com.winstar.invoice.repository.InvoiceItemRepository;
import com.winstar.invoice.repository.InvoiceRepository;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.service.MyOilCouponService;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.service.OilOrderService;
import com.winstar.order.utils.DateUtil;
import com.winstar.shop.entity.Activity;
import com.winstar.user.service.AccountService;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.Calendar;
import java.util.Date;
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
    InvoiceRepository invoiceRepository;

    @Autowired
    MyOilCouponService myOilCouponService;

    @Autowired
    AccountService accountService;

    @Autowired
    InvoiceItemRepository invoiceItemRepository;
    @Autowired
    OilOrderService oilOrderService;

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
        List<InvoiceItem> invoices=invoiceItemRepository.findByAccountId(accountId);
        List<String> ids=new ArrayList<>();
         if(invoices.size()>0){
            for(InvoiceItem invoice:invoices){
                ids.add(invoice.getOilId());
            }
        }else{
             ids.add("");
         }
        Sort sort = new Sort(Sort.Direction.DESC, "useDate");
        Pageable pageable = new PageRequest(nextPage, pageSize, sort);

        Date now=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -3);//三天前
        Date endTime=calendar.getTime();

        calendar.setTime(now);
        calendar.add(Calendar.MONTH, -3);//三月前
        Date startTime = calendar.getTime();

        Page<MyOilCoupon> page = myOilCouponService.findUsedCoupon( accountId, startTime, endTime,  ids, pageable);
//        Page<MyOilCoupon> page = myOilCouponService.findUsedCoupon(accountId,ids, pageable);
        List<MyOilCoupon> list = page.getContent();

        if (list.size() == 0) throw new NotFoundException("MyOilCoupon");
        for(MyOilCoupon coupon:list){
           coupon=this.reckon(coupon,coupon.getOrderId());
        }

        return list;
    }

    public MyOilCoupon reckon(MyOilCoupon myOilCoupon,String orderId) throws  NotFoundException {
        List<MyOilCoupon> oils = myOilCouponService.findByOrderId(orderId);
        Integer num = oils.size();
        OilOrder order = oilOrderService.getOneOrder(orderId);
        Double payprice = order.getPayPrice();
        Double payPrice = payprice / num;
        myOilCoupon.setPayPrice(payPrice);

        return  myOilCoupon;
    }

    /**
     * 审请开票
     */
    @RequestMapping(value = "/makeInvoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Invoice makeInvoice(
            HttpServletRequest request,
            String[] ids, Integer type, String name,String oilType,String email,String phone,String companyName,String
                    taxpayerNumber
    ) throws MissingParameterException, InvalidParameterException, NotRuleException, NotFoundException,
            ServiceUnavailableException {
        String accountId = accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) throw new NotFoundException("MyOilCoupon");
        if(type==null) throw new MissingParameterException("type");
        if(ids==null) throw new MissingParameterException("ids");
        if(ids.length==0) throw new MissingParameterException("ids");
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
        Double price=new Double(0);
        for(String id : ids ){
            InvoiceItem item=invoiceItemRepository.findByOilId(id);
            if(item!=null) throw new NotRuleException("this ["+id+"] has been drawn" );
            MyOilCoupon myOilCoupon= myOilCouponService.findOne(id);
            if(myOilCoupon==null ) throw new NotFoundException(id);
            myOilCoupon=this.reckon(myOilCoupon,myOilCoupon.getOrderId());
            price+=myOilCoupon.getPayPrice();
        }
        invoice.setPrice(price);
        invoice.setAccountId(accountId);
        invoice.setType(type);
        invoice.setOilType(oilType);
        invoice.setEmail(email);
        invoice.setPhone(phone);
        invoice.setStatus(0);
        invoice.setCreateDate(new Date());
        Invoice in= invoiceRepository.save(invoice);
        for(String id : ids ){
            InvoiceItem item=new InvoiceItem();
            item.setInvoiceId(in.getId());
            item.setAccountId(accountId);
            item.setOilId(id);
            item.setSalePrice(in.getPrice());
            invoiceItemRepository.save(item);
        }


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
