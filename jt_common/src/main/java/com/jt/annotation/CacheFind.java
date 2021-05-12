package com.jt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})       //标识注解使用在方法中
@Retention(RetentionPolicy.RUNTIME) //什么时期有效
public @interface CacheFind {
    //key-value方法的返回值
    String key();   //要求用户必须指定key
    int seconds() default -1;  //设定超时时间  -1 无需超时
}
