package com.tsingtec.tsingmini.config.interceptor;

import com.tsingtec.tsingcore.entity.mini.MaUser;
import com.tsingtec.tsingcore.exception.BusinessException;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import com.tsingtec.tsingcore.service.mini.MaUserService;
import com.tsingtec.tsingmini.aop.annotation.LoginToken;
import com.tsingtec.tsingmini.aop.annotation.PassToken;
import com.tsingtec.tsingmini.config.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 小程序token认证流程
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private MaUserService maUserService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken userLoginToken = method.getAnnotation(LoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new BusinessException(BaseExceptionType.TOKEN_ERROR);
                }
                // 获取 token 中的 user id
                String id = jwtUtil.getClaim(token,"id");
                MaUser maUser = maUserService.get(Integer.valueOf(id));
                if (maUser == null) {
                    throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"用户不存在，请重新登录");
                }
                if(!jwtUtil.verify(token)){
                    throw new BusinessException(BaseExceptionType.TOKEN_ERROR,"token过期,请重新获取");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}