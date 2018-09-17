package com.winstar.interceptors;

import com.winstar.exception.NotFoundException;
import com.winstar.exception.NotRuleException;
import com.winstar.exception.ServiceUnavailableException;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.utils.ServiceManager;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth interceptor
 *
 * @author dpw
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private List<String> excludeUrls = new ArrayList<>();

    public AuthInterceptor() {

        excludeUrls.add("/error");
        excludeUrls.add("/api/v1/cbc/OneMoneyCouponRecord/save");
        excludeUrls.add("/api/v1/cbc/payCallback");
        excludeUrls.add("/api/v1/cbc/wxPay/notify");
        excludeUrls.add("/api/v1/cbc/creditPay/notify");
        excludeUrls.add("/api/v1/orders/invoice/callBack");
        excludeUrls.add("/api/v1/cbc/mycoupon/giveCoupon");
        excludeUrls.add("/api/v1/cbc/valuations");
        excludeUrls.add("/api/v1/cbc/time/nowTime");

        excludeUrls.add("/cbc-api/error");
        excludeUrls.add("/cbc-api/api/v1/cbc/OneMoneyCouponRecord/save");
        excludeUrls.add("/cbc-api/api/v1/cbc/payCallback");
        excludeUrls.add("/cbc-api/api/v1/cbc/wxPay/notify");
        excludeUrls.add("/cbc-api/api/v1/cbc/creditPay/notify");
        excludeUrls.add("/cbc-api/api/v1/orders/invoice/callBack");
        excludeUrls.add("/cbc-api/api/v1/cbc/mycoupon/giveCoupon");
        excludeUrls.add("/cbc-api/api/v1/cbc/valuations");
        excludeUrls.add("/cbc-api/api/v1/cbc/time/nowTime");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception{
        if (excludeUrls.contains(request.getRequestURI()) || request.getRequestURI().contains("/api/v1/cbc/account") || request.getRequestURI().contains("/api/v1/cbc/valuations") || request.getRequestURI().startsWith("/api/v1/cbc/verification")
                || request.getRequestURI().contains("/api/v1/cbc/obuActivity")) {
            return true;
        }

        String tokenId = request.getHeader("token_id");
        if(!StringUtils.isEmpty(request.getHeader("token-id"))){
            tokenId = request.getHeader("token-id");
        }

        logger.info("url:"+request.getRequestURI());
        logger.info("拦截器中的token_id" + tokenId);

        if (StringUtils.isEmpty(tokenId)) {
            unauthorized(response);
            return false;
        }

        AccessToken accessToken = ServiceManager.accessTokenRepository.findByTokenId(tokenId);
        if (checkAccount(response, accessToken)) {

            logger.info("url:"+request.getRequestURI());
            logger.info("拦截器中根据token_id获取到的AccessToken不存在"+tokenId);
            return false;
        }

        request.setAttribute("accountId", accessToken.getAccountId());
        return true;
    }

    private boolean checkAccount(HttpServletResponse response, AccessToken accessToken) throws ServiceUnavailableException,NotRuleException {

        if (null == accessToken || null == ServiceManager.accountRepository.findOne(accessToken.getAccountId())) {

            unauthorized(response);
            return true;
        }
        return false;
    }

    private void unauthorized(HttpServletResponse response) throws ServiceUnavailableException,NotRuleException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> map = new HashMap<>();
        map.put("code","user.isNot.unauthorized.NotRule");
        map.put("description","未授权");
        //将实体对象转换为JSON Object转换
        JSONObject responseJSONObject = JSONObject.fromObject(map);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            throw new ServiceUnavailableException();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}

