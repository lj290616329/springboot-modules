package com.tsingtec.tsingweb.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.tsingtec.tsingcore.entity.sys.Admin;
import com.tsingtec.tsingcore.entity.sys.Log;
import com.tsingtec.tsingcore.service.sys.LogService;
import com.tsingtec.tsingweb.aop.annotation.LogAnnotation;
import com.tsingtec.tsingweb.utils.HttpContextUtils;
import com.tsingtec.tsingcore.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    /**
     * 此处的切点是注解的方式
     * 只要出现 @LogAnnotation注解都会进入
     */
    @Pointcut("@annotation(com.tsingtec.tsingweb.aop.annotation.LogAnnotation)")
    public void logPointCut(){

    }

    /**
     * 环绕增强,相当于MethodInterceptor
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        try {
            saveLog(point, time);
        } catch (Exception e) {
            log.error("e={}",e);
        }

        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        System.out.println("进入日志保存");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Log sysLog = new Log();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);

        if(logAnnotation != null){
            //注解上的描述
            sysLog.setOperation(logAnnotation.title()+"-"+logAnnotation.action());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        log.info("请求{}.{}耗时{}毫秒",className,methodName,time);
        try {
            //请求的参数
            Object[] args = joinPoint.getArgs();
            String params=null;
            if(args.length!=0){
                params = JSON.toJSONString(args);
            }
            sysLog.setParams(params);
        } catch (Exception e) {
            log.debug("解析参数出错");
        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        log.info("Ip{}，接口地址{}，请求方式{}，入参：{}",sysLog.getIp(),request.getRequestURL(),request.getMethod(),sysLog.getParams());
        //用户名
        Admin admin = HttpContextUtils.getAdmin();
        Integer aid = admin.getId();
        String username = admin.getLoginName();
        sysLog.setUsername(username);
        sysLog.setAid(aid);
        sysLog.setTime((int) time);
        log.info(sysLog.toString());
        logService.insert(sysLog);
    }
}
