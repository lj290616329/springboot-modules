package com.tsingtec.tsingweb.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limiter {

    /**
     * 从第一次访问接口的时间到cycle周期时间内，无法超过frequency次
     *
     * @return
     */
    int frequency() default 2;

    /**
     * 返回的错误信息
     *
     * @return
     */
    String message() default "请求过于频繁";

}