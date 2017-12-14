package com.winstar.oil.utils;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ws.object.SvcInfo;
import ws.server.SvcServicePortType;

/**
 * Created by zl on 2017/7/20
 */
@Component
@ConfigurationProperties(prefix="info")
public class RequestSvcInfoUtils {

    private static String cardUrl;

    public static String getCardUrl() {
        return cardUrl;
    }

    public static void setCardUrl(String cardUrl) {
        RequestSvcInfoUtils.cardUrl = cardUrl;
    }

    public static SvcInfo getSvcInfo(SvcInfo req){   //测试txnId：029563380478、089874843927
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.getInInterceptors().add(new LoggingInInterceptor());
        factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
        factoryBean.setServiceClass(SvcServicePortType.class);
        factoryBean.setAddress(cardUrl);
        SvcServicePortType impl = (SvcServicePortType) factoryBean.create();
        return impl.transX(req);
    }

}
