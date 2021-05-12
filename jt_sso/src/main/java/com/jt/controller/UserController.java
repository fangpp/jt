package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping("/findAll")
    public List<User> findAll(){

        return userService.findAll();
    }

    /***
     * 实现用户数据校验
     * 1.URL地址: http://sso.jt.com/user/check/admin123/1?r=0.1845737292036227&callback=jsonp1616466201812&_=1616466206686
     * 2. 参数:  /admin123/1
     * 3.返回值结果  SysResult对象
     * data: false  //返回数据true用户已存在，false用户不存在，可以
     * 4.JSONP方式说明:  返回值有特殊要求  callback(json)
     */
    @RequestMapping("/check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param, @PathVariable Integer type,String callback){
        //只讨论数据是否存在.
        int count = userService.checkUser(param,type);
        boolean flag = count > 0;
        return new JSONPObject(callback, SysResult.success(flag));
    }


    /**
     * 实现用户跨域数据的回显   JSONP
     * URL地址: http://sso.jt.com/user/query/edf7eb6a-3cb1-472e-9d27-b21d727b60c3?callback=jsonp1616574179860&_=1616574179904
     * 参数:  ticket信息拼接到url中   restFul风格
     * 返回值: StsResult对象
     */
    @RequestMapping("/query/{ticket}")
    public JSONPObject findUserByTicket(@PathVariable String ticket,String callback){

        String userJSON = jedisCluster.get(ticket);
        if(!StringUtils.hasLength(userJSON)){ //如果为null

            return new JSONPObject(callback, SysResult.fail());
        }
        //服务器需要向客户端传递数据.
       // User user = ObjectMapperUtil.toObject(userJSON, User.class)

       return new JSONPObject(callback, SysResult.success(userJSON));
    }




}
