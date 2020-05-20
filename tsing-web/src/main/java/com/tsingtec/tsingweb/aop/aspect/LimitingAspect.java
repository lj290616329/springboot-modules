package com.tsingtec.tsingweb.aop.aspect;

import com.tsingtec.tsingcore.exception.BusinessException;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import com.tsingtec.tsingcore.utils.IPUtils;
import com.tsingtec.tsingweb.aop.annotation.Limiter;
import com.tsingtec.tsingweb.config.cache.EhCacheConfig;
import com.tsingtec.tsingweb.utils.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Component
public class LimitingAspect {
	private static final String LIMITING_KEY = "limiting:%s:%s";

    private Cache<String, AtomicInteger> limiter_cache;

    public LimitingAspect(EhCacheConfig ehCacheConfig){
        limiter_cache = ehCacheConfig.ehCacheManager().getCache("limiter");
    }

    @Pointcut("@annotation(limiter)")
    public void pointcut(Limiter limiter) {

    }

    @Around("pointcut(limiter)")
    public Object around(ProceedingJoinPoint pjp, Limiter limiter) throws Throwable {

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        //获取请求的ip和方法
        String ipAddress = IPUtils.getIpAddr(request);

        String methodName = pjp.getSignature().toLongString();

        //获取方法的访问周期和频率
        int frequency = limiter.frequency();

        //获取redis中周期内第一次访问方法的时间和执行的次数
        AtomicInteger count = limiter_cache.get(String.format(LIMITING_KEY, ipAddress, methodName));

        if(count == null){
            count = new AtomicInteger(1);
            limiter_cache.put(String.format(LIMITING_KEY, ipAddress, methodName),count);
            return pjp.proceed();
        }
        //判断是否2次
        if(count.incrementAndGet() >= frequency) {
            log.warn("ip为{}进行操作方法为{}，账号已冻结", ipAddress, methodName);
            throw new BusinessException(BaseExceptionType.USER_ERROR,"操作过于频繁,请稍后再试!");
        }
        return pjp.proceed();
    }
}