package com.tsingtec.tsingweb.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限验证失败的过滤器
 */
@Slf4j
public class ShiroLoginFilter extends FormAuthenticationFilter {

    //用户登录校验失败回调方法，也可以自己重写校验方法isAccessAllowed
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        log.error("没登录就要进来,好像不太好");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String header = httpServletRequest.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ;
        if (isAjax) {//如果是ajax返回指定格式数据
            httpServletResponse.setContentType("application/json; charset=utf-8");
            //返回禁止访问json字符串
            httpServletResponse.getWriter().write("{\"code\":-1,\"msg\":\"未登录用户！\"}");
        } else {//如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/index/login");
        }
        return false;
    }

    @SneakyThrows
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request,
                                     ServletResponse response){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String header = httpServletRequest.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ;
        if (isAjax) {//如果是ajax返回指定格式数据
            httpServletResponse.setContentType("application/json; charset=utf-8");
            //返回禁止访问json字符串
            httpServletResponse.getWriter().write("{\"code\":-1,\"msg\":\"未登录用户！\"}");
        } else {//如果是普通请求进行重定向
            httpServletResponse.sendRedirect("/index/login");
        }
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,
                                     Subject subject, ServletRequest request, ServletResponse response)
            throws Exception {
        return super.onLoginSuccess(token, subject, request, response);
    }
}