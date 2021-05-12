package com.jt.web.controller;

import com.jt.pojo.ItemDesc;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "http://www.jt.com")   //标识当前Controller中的方法 允许被其他服务器访问
                                         //预检: 在规定时间内同源策略不会再次拦截 提高效率
public class CorsController {

    /**
     * http://manage.jt.com/testCors
     * @return
     */
    @RequestMapping("testCors")
    public ItemDesc cors(){

        return new ItemDesc().setItemDesc("CORS测试");
    }
}
