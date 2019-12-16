package com.winstar.invoice.controller;

import com.winstar.cashier.comm.EnumType;
import com.winstar.cashier.entity.PayOrder;
import com.winstar.cashier.repository.PayOrderRepository;
import com.winstar.exception.MissingParameterException;
import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.invoice.entity.Invoice;
import com.winstar.invoice.entity.InvoiceItem;
import com.winstar.invoice.entity.InvoiceStockSwitch;
import com.winstar.invoice.repository.InvoiceItemRepository;
import com.winstar.invoice.repository.InvoiceRepository;
import com.winstar.invoice.repository.InvoiceStockSwitchRepository;
import com.winstar.oil.entity.MyOilCoupon;
import com.winstar.oil.service.MyOilCouponService;
import com.winstar.order.entity.OilOrder;
import com.winstar.order.service.OilOrderService;
import com.winstar.user.service.AccountService;
import com.winstar.utils.WsdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

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
    PayOrderRepository payOrderRepository;
    @Autowired
    InvoiceItemRepository invoiceItemRepository;
    @Autowired
    OilOrderService oilOrderService;
    @Autowired
    InvoiceStockSwitchRepository invoiceStockSwitchRepository;

    /**
     * 校验可用发票库存
     *
     * @return
     */
    @GetMapping("checkStock")
    public InvoiceStockSwitch checkStock() throws NotRuleException {
        return checkInvoiceStock();
    }

    private InvoiceStockSwitch checkInvoiceStock() throws NotRuleException {
        InvoiceStockSwitch invoiceStockSwitch = invoiceStockSwitchRepository.findByType(InvoiceStockSwitch.STATUS_ON);
        if (null == invoiceStockSwitch)
            throw new NotRuleException("noStock.makeInvoice");
        return invoiceStockSwitch;
    }

    /**
     * 未开发票的油卷
     *
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MyOilCoupon> query(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer nextPage,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) throws NotRuleException, NotFoundException {
        String accountId = accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) throw new NotFoundException("MyOilCoupon");
        List<InvoiceItem> invoices = invoiceItemRepository.findByAccountId(accountId);
        List<String> ids = new ArrayList<>();
        if (invoices.size() > 0) {
            for (InvoiceItem invoice : invoices) {
                ids.add(invoice.getOilId());
            }
        } else {
            ids.add("");
        }
        Sort sort = new Sort(Sort.Direction.DESC, "useDate");
        Pageable pageable = new PageRequest(nextPage, pageSize, sort);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -3);//三天前
        Date endTime = calendar.getTime();

//        calendar.setTime(now);
//        calendar.add(Calendar.MONTH, -3);//三月前
//        Date startTime = calendar.getTime();

        Page<MyOilCoupon> page = myOilCouponService.findUsedCoupon(accountId, endTime, ids, pageable);
        List<MyOilCoupon> list = page.getContent();

        if (list.size() == 0) throw new NotFoundException("MyOilCoupon");
        for (MyOilCoupon coupon : list) {
            coupon = this.reckon(coupon, coupon.getOrderId());
        }

        return list;
    }

    public MyOilCoupon reckon(MyOilCoupon myOilCoupon, String orderId) throws NotFoundException {
//        List<MyOilCoupon> oils = myOilCouponService.findByOrderId(orderId);
//        OilOrder order = oilOrderService.getOneOrder(orderId);

        OilOrder order = oilOrderService.getOneOrder(orderId);
        double num = order.getItemTotalValue() / myOilCoupon.getPanAmt();
        Double knockGold = 0.0;
        if (order.getPayType() == 2) {
            List<PayOrder> payOrders = payOrderRepository.findByOrderNumberAndState(order.getSerialNumber(), EnumType.PAY_STATE_SUCCESS.valueStr());
            if (WsdUtils.isNotEmpty(payOrders.get(0).getCouponFee())) {
                knockGold = Double.parseDouble(payOrders.get(0).getCouponFee()) / 100;
            }
        }
        BigDecimal payprice = new BigDecimal(order.getPayPrice() - knockGold);
        BigDecimal p = new BigDecimal(num);
        BigDecimal payPrice = payprice.divide(p, 2, BigDecimal.ROUND_HALF_UP);
        myOilCoupon.setPayPrice(payPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        return myOilCoupon;
    }

    /**
     * 审请开票
     */
    @RequestMapping(value = "/makeInvoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Invoice makeInvoice(
            HttpServletRequest request,
            String[] ids, Integer type, String name, String oilType, String email, String phone, String companyName,
            String taxpayerNumber, String companyAddress, String telephone, String depositBank, String bankAccount,
            Integer invoiceType, String recipients, String consigneePhone, String consigneeAddress, String detailedAddress
    ) throws MissingParameterException, NotRuleException, NotFoundException {
        checkInvoiceStock();
        String accountId = accountService.getAccountId(request);
        if (StringUtils.isEmpty(accountId)) throw new NotFoundException("MyOilCoupon");
        if (type == null) throw new MissingParameterException("type");
        if (ids == null) throw new MissingParameterException("ids");
        if (ids.length == 0) throw new MissingParameterException("ids");
        if (oilType == null) throw new MissingParameterException("oilType");
        if (email == null) throw new MissingParameterException("email");
        if (phone == null) throw new MissingParameterException("phone");
        Invoice invoice = new Invoice();
        if (invoiceType == null) {
            invoiceType = 1;
        }
        if (invoiceType == 2) {
            if (recipients == null) throw new MissingParameterException("recipients");
            if (consigneePhone == null) throw new MissingParameterException("consigneePhone");
            if (consigneeAddress == null) throw new MissingParameterException("consigneeAddress");
            if (detailedAddress == null) throw new MissingParameterException("detailedAddress");
        }
        if (type == 1) {
            if (name == null) throw new MissingParameterException("name");
            invoice.setName(name);
        }

        if (type == 2) {
            if (companyName == null) throw new MissingParameterException("companyName");
            if (taxpayerNumber == null) throw new MissingParameterException("taxpayerNumber");
            invoice.setCompanyName(companyName);
            invoice.setTaxpayerNumber(taxpayerNumber);
        }
        List list = new ArrayList();
        BigDecimal price = new BigDecimal(0.00);
        for (String id : ids) {
            InvoiceItem item = invoiceItemRepository.findByOilId(id);
            if (item != null) throw new NotRuleException("this [" + id + "] has been drawn");
            MyOilCoupon myOilCoupon = myOilCouponService.findOne(id);
            if (myOilCoupon == null) throw new NotFoundException(id);
            myOilCoupon = this.reckon(myOilCoupon, myOilCoupon.getOrderId());
            //把油券的订单号 放入到一个集合去
            list.add(myOilCoupon.getOrderId());
            BigDecimal p = new BigDecimal(myOilCoupon.getPayPrice());
            price = price.add(p);
        }
        //订单编号保存到List 然后去重
        Set<String> middleHashSet = new HashSet<String>(list);
        List<String> invoiceOrderList = new ArrayList<String>(middleHashSet);
        //开票原价(新加字段)
        int originalPrice = ids.length * 100;

        invoice.setOriginalPrice(originalPrice);
        invoice.setInvoiceOrder(invoiceOrderList.toString());
        invoice.setPrice(price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        invoice.setAccountId(accountId);
        invoice.setType(type);
        invoice.setOilType(oilType);
        invoice.setEmail(email);
        invoice.setPhone(phone);
        invoice.setStatus(0);
        invoice.setCreateDate(new Date());
        invoice.setInvoiceType(invoiceType);
        invoice.setRecipients(recipients);
        invoice.setConsigneeAddress(consigneeAddress);
        invoice.setConsigneePhone(consigneePhone);
        invoice.setDetailedAddress(detailedAddress);

        if (!StringUtils.isEmpty(telephone)) invoice.setTelephone(telephone);
        if (!StringUtils.isEmpty(companyAddress)) invoice.setCompanyAddress(companyAddress);
        if (!StringUtils.isEmpty(depositBank)) invoice.setDepositBank(depositBank);
        if (!StringUtils.isEmpty(bankAccount)) invoice.setBankAccount(bankAccount);

        Invoice in = invoiceRepository.save(invoice);
        for (String id : ids) {
            MyOilCoupon myOilCoupon = myOilCouponService.findOne(id);
            InvoiceItem item = new InvoiceItem();
            item.setInvoiceId(in.getId());
            item.setAccountId(accountId);
            item.setOilId(id);
            BigDecimal p = new BigDecimal(myOilCoupon.getPayPrice());
            item.setSalePrice(p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            item.setOrderId(myOilCoupon.getOrderId());
            invoiceItemRepository.save(item);
        }
        return in;
    }

    /**
     * 查询我开票历史
     *
     * @param request
     * @param nextPage
     * @param pageSize
     * @return
     * @throws NotRuleException
     * @throws NotFoundException
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> history(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer nextPage,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) throws NotRuleException, NotFoundException {
        String accountId = accountService.getAccountId(request);

        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable pageable = new PageRequest(nextPage, pageSize, sort);

        Page<Invoice> page = invoiceRepository.findByAccountId(accountId, pageable);
        if (page.getContent().size() == 0) throw new NotFoundException("Invoice");

        return page.getContent();
    }

}
