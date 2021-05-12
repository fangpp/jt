package com.jt.interceptor;

import com.jt.annotation.CacheFind;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 返回值: boolean  true 拦截器放行   false 拦截 重定向到登录页面
     * 业务说明: 如果用户不登录 则程序不放行.
     * 判断依据: 如何判断是否登录?
     *          1.判断是否有cookie
     *          2.判断redis中是否有记录.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.判断是否有cookie
        String ticket = CookieUtil.getCookieValue("JT_TICKET", request);
        if(StringUtils.hasLength(ticket)){
            //2.判断redis中是否有记录.
            if(jedisCluster.exists(ticket)){

                //3.动态获取用户信息
                String userJSON = jedisCluster.get(ticket);
                User user = ObjectMapperUtil.toObject(userJSON, User.class);
                //一般将公共对象通过request/session进行存储.
                request.getSession().setAttribute("JT_USER",user);

                //使用ThreadLocal方式存储数据
                UserThreadLocal.set(user);
                //表示放行
                return true;
            }
        }

        //重定向到系统登录页面  一般使用绝对路径
        response.sendRedirect("/user/login.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //防止服务器内存泄露,则将用完的对象销毁
        request.getSession().removeAttribute("JT_USER");
        UserThreadLocal.remove();
    }
}
