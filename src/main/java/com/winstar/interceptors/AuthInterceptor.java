package com.winstar.interceptors;


import com.winstar.exception.ServiceUnavailableException;
import com.winstar.user.entity.AccessToken;
import com.winstar.user.utils.ServiceManager;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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

    private List<String> excludeUrls = new ArrayList<>();

    public AuthInterceptor() {
        excludeUrls.add("/error");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (excludeUrls.contains(request.getRequestURI()) || request.getRequestURI().contains("/api/v1/cbc/account")) {
            return true;
        }

        String tokenId = request.getHeader("token_id");

        if (StringUtils.isEmpty(tokenId)) {
            unauthorized(response);
            return false;
        }

        AccessToken accessToken = ServiceManager.accessTokenRepository.findByTokenId(tokenId);
        if (checkAccount(response, accessToken))
            return false;

        request.setAttribute("accountId", accessToken.getAccountId());
        return true;
    }


    private boolean checkAccount(HttpServletResponse response, AccessToken accessToken) throws ServiceUnavailableException {

        if (null == accessToken || null == ServiceManager.accountRepository.findOne(accessToken.getAccountId())) {
            unauthorized(response);
            return true;
        }
        return false;
    }

    private void unauthorized(HttpServletResponse response) throws ServiceUnavailableException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> map = new HashMap<>();
        map.put("data", "未授权");
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

