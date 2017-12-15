package com.winstar.invoice.utils;

import com.winstar.invoice.entity.Invoice;
import com.winstar.order.entity.OilOrder;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shoo on 2017/10/23 16:28.
 * @Describe：
 */
public class InvoiceUtil {

    /**
     * 初始化实体
     * @param invoice 发票
     * @param oilOrder 订单
     * @return 发票
     */
    public static Invoice initInvoice(Invoice invoice, OilOrder oilOrder){
        invoice.setOilTotalValue(oilOrder.getItemTotalValue());
        invoice.setPayPrice(oilOrder.getPayPrice());
        invoice.setOrderSerialNo(oilOrder.getSerialNumber());
        invoice.setCreateTime(new Date());
        invoice.setStatus(0);//待开票
        invoice.setIsDel("0");
        return invoice;
    }

    /**
     * 判断参数
     * @param invoice 发票
     * @return str
     */
    public static String judgeParas(Invoice invoice){

        if(invoice.getType()==null){
            return "type";
        }
        if(StringUtils.isEmpty(invoice.getEmail())){
            return "email";
        }
        if(StringUtils.isEmpty(invoice.getPhoneNo())){
            return "phoneNo";
        }
        if(invoice.getType()==0){
            if(StringUtils.isEmpty(invoice.getPersonName())){
                return "personName";
            }
            if(StringUtils.isEmpty(invoice.getIdentNumber())){
                return "identNumber";
            }
        }else if(invoice.getType()==1){
            if(StringUtils.isEmpty(invoice.getCompanyName())){
                return "companyName";
            }
            if(StringUtils.isEmpty(invoice.getRegistrationNo())){
                return "registrationNo";
            }
            if(StringUtils.isEmpty(invoice.getCompanyAddress())){
                return "companyAddress";
            }
            if(StringUtils.isEmpty(invoice.getDepositBank())){
                return "depositBank";
            }
            if(StringUtils.isEmpty(invoice.getBankAccount())){
                return "bankAccount";
            }
        }
        return "ok";
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }



}
