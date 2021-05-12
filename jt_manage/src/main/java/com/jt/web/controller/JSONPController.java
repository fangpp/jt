package com.jt.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPController {

    @RequestMapping("/web/testJSONP")
    public JSONPObject testJSONP(String callback){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("详情信息2222");

        return new JSONPObject(callback, itemDesc);
    }


    /**
     * JSONP请求 返回值要求:  callback(JSON)
     * 1.请求网址:  http://manage.jt.com/web/testJSONP?callback=xxxx
     * 2. 页面取值  itemDesc属性
     */
   /* @RequestMapping("/web/testJSONP")
    public String testJSONP(String callback){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("详情信息");
        String json = ObjectMapperUtil.toJSON(itemDesc);
        //jsonp步骤3 封装指定的格式
        return callback + "(" + json + ")";
    }*/
}
