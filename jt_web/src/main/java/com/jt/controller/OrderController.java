package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.params.Params;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference(check = false)
    private DubboCartService cartService;

    /**
     * 需求: 跳转到订单确认页面
     * URL: http://www.jt.com/order/create.html
     * 参数: userId
     * 返回值:order-cart.jsp
     * 页面取值: ${carts}
     */
    @RequestMapping("/create")
    public String create(Model model){
        long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("carts", cartList);
        return "order-cart";
    }

    /**
     * 实现订单入库
     *  URL地址:  http://www.jt.com/order/submit
     *  参数:   整个form表单 order/orderItem/orderShipping
     *  返回值: SysResult对象   返回orderId即可
     */
    @RequestMapping("/submit")
    public SysResult submit(Order order){

        String orderId = "xxxxxx";
        //完成订单入库操作......
        return SysResult.success(orderId);
    }



}
