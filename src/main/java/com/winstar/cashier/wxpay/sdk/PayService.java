package com.winstar.cashier.wxpay.sdk;

import com.winstar.cashier.wxpay.common.IServiceRequest;

public class PayService {
	
	//统一下单接口地址
	public static String UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	//统一下单接口地址
	public static String QUERY_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	//退款接口地址
	public static String REFUND_ORDER = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	//退款查询接口
	public static String REFUND_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
	
	//发请求的HTTPS请求器
    private static IServiceRequest serviceRequest;

	public static String HttpsRequestClassName = "com.winstar.cashier.wxpay.common.HttpsRequest";
    
	/**
     * 请求统一下单接口
     * PayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
	public static String request(PayReqData data) throws Exception {
    	Class c = Class.forName(HttpsRequestClassName);
    	serviceRequest = (IServiceRequest)c.newInstance();
        String responseString = serviceRequest.sendPostNoSSL(UNIFIED_ORDER, data);
        return responseString;
    }
    
    /**
     * 请求查询订单接口
     * PayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String queryRequest(PayReqData data) throws Exception {
    	Class c = Class.forName(HttpsRequestClassName);
    	serviceRequest = (IServiceRequest)c.newInstance();
    	String responseString = serviceRequest.sendPostNoSSL(QUERY_ORDER, data);
    	return responseString;
    }

    /**
     * 请求退款订单接口
     * PayReqRefundData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String refund(PayReqRefundData data) throws Exception {
    	Class c = Class.forName(HttpsRequestClassName);
    	serviceRequest = (IServiceRequest)c.newInstance();
    	String responseString = serviceRequest.sendPost(REFUND_ORDER, data);
    	return responseString;
    }
    
    /**
     * 请求退款订单接口
     * PayReqRefundQueryData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String refundQuery(PayReqRefundQueryData data) throws Exception {
    	Class c = Class.forName(HttpsRequestClassName);
    	serviceRequest = (IServiceRequest)c.newInstance();
    	String responseString = serviceRequest.sendPost(REFUND_ORDER_QUERY, data);
    	return responseString;
    }
	
}
