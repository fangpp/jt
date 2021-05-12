package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.ref.ReferenceQueue;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    //不必先启动生产者,再启动消费者
    @Reference(check = false)
    private DubboCartService cartService;

    /**
     * URL:http://www.jt.com/cart/show.html
     * 跳转页面: cart.jsp
     * 页面取值: ${cartList}  查询购物车列表数据
     * 业务需求:  根据userId 查询购物车信息.
     */
    @RequestMapping("/show")
    public String show(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("JT_USER");
        long userId = user.getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("cartList",cartList);
        return "cart";
    }

    /**
     * 更新数量:
     * URL:http://www.jt.com/cart/update/num/1474391990/30
     * 参数: itemId/num
     * 返回值: void
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody //1.ajax返回  2.返回值转化为json
    public void updateCartNum(Cart cart){   //要求属性与名称一致.
        long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.updateCartNum(cart);
    }

    //http://www.jt.com/cart/delete/1474391990.html
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId){

        Long userId = UserThreadLocal.get().getId();
        //删除购物车
        return "redirect:/cart/show.html";
    }

    /**
     * 实现购物车新增操作
     * URL: http://www.jt.com/cart/add/562379.html
     * 参数: 562379 itemId / form表单数据
     * 返回值: 重定向到购物车展现页面中
     */
    @RequestMapping("/add/{itemId}")
    public String addCart(Cart cart){   //cart已经完成了数据的封装
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.addCart(cart);
        return "redirect:/cart/show.html";
    }







}
