package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

//spring中的通知 核心原理:Spring AOP机制
@RestControllerAdvice  //只对Controller代码层级有效.
public class SysResultException {

    //当程序发生异常时, 如果没有try-catch则直接向上抛出异常.
    //该注解只拦截运行时异常.
    //经过测试发现jsonp的请求会携带callback参数 如果有callback参数则认为是JSONP请求
    //如何获取callback参数
    @ExceptionHandler({RuntimeException.class})
    public Object exception(Exception e, HttpServletRequest request){
        e.printStackTrace();
        String callback = request.getParameter("callback");
        if(StringUtils.hasLength(callback)){

            return new JSONPObject(callback, SysResult.fail());
        }
        return SysResult.fail();
    }
}
