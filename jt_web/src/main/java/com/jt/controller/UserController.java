package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.ref.ReferenceQueue;

@Controller
@RequestMapping("/user")
public class UserController {

    @Reference(check = false)
    private DubboUserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 实现通用的页面跳转
     * 1). 当用户网址 http://www.jt.com/user/login.html   要求跳转到登录页面中  login.jsp
     * 2). 注册页面  http://www.jt.com/user/register.html  要求跳转到登录页面中  register.jsp
     */
    @RequestMapping("/{modleName}")
    public String module(@PathVariable String modleName){

        return modleName;
    }

    /**
     * 完成用户注册
     * 1.url地址:http://www.jt.com/user/doRegister
     * 2.参数:  password: admin123
     *          username: admin1234
     *          phone: 13111111112
     * 3.返回值:  SysResult对象  json
     */
    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult doRegister(User user){

        userService.saveUser(user);
        return SysResult.success();
    }

    /**
     * 完成用户单点登录流程
     * 1.URL地址:http://www.jt.com/user/doLogin?r=0.8136555319489749
     * 2.参数分析: username: admin123
     *            password: admin123456
     * 3.系统返回值: SysResult对象
     * Cookie作用: 浏览器用来保存服务器数据的一种方式
     * setPath():    代表cookie中一种权限的设定.
     * 例子:
     *      url地址: http://www.jt.com/addUser/xxxx
     *      url2地址 http://www.jt.com/xxxx
     *
     *      cookie.setPath(/)           url/url2可以获取cookie中的值
     *      cookie2.setPath(/addUser)    url可以获取cookie2中的值
     *
     * setDomain("jt.com");  表示cookie在jt.com结尾的域名下进行共享.
     *
     *
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response){
        //假设ticket正确返回
        String ticket = userService.doLogin(user);
        if(!StringUtils.hasLength(ticket)){
            //如果数据为null.则证明用户名和密码错误.
            return SysResult.fail();
        }
        //如果程序执行到这里,表示正确,则将密钥信息保存到cookie中
       /* Cookie cookie = new Cookie("JT_TICKET", ticket);
        cookie.setPath("/");
        cookie.setMaxAge(7*24*60*60);
        cookie.setDomain("jt.com");
        response.addCookie(cookie);*/
        CookieUtil.addCookie("JT_TICKET", ticket,
                        "jt.com",7*24*60*60, response);
        return SysResult.success();
    }

    /**
     * 实现用户退出操作
     * 1.url地址:http://www.jt.com/user/logout.html
     * 2.没有参数
     * 3.String  重定向到系统首页
     *
     * 思路:
     *   1.通过JT_TICKET获取cookie的值
     *   2.删除Redis数据.
     *   3.删除Cookie的数据.  setMaxAge(0) cookie属性数据一个都不能少
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
       String ticket = CookieUtil.getCookieValue("JT_TICKET", request);
       if(StringUtils.hasLength(ticket)){//cookie中有值
            //1.删除redis
           jedisCluster.del(ticket);
           //2.删除Cookie
           CookieUtil.deleteCookie(response, "JT_TICKET", "jt.com");
       }

        //重定向到系统首页
        return "redirect:/";
    }








}
