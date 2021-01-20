package com.adc.da.login;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroUserFilter extends UserFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isAjax(request)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            ResponseMessage responseMessage = Result.error("-1", "登录认证失效，请重新登录!");

            httpServletResponse.getWriter().write(JSONObject.toJSON(responseMessage).toString());
        } else {
            /*
             * @Mark 非ajax请求重定向为登录页面
             */
            httpServletResponse.sendRedirect("/api/login");
        }
        return false;
    }

    private boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(header);
    }

}
