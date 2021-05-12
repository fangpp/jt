package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Reference(check = false)
    private DubboItemService itemService;

    /**
     * URL地址: http://www.jt.com/items/562379.html
     * 参数:    562379 itemId号
     * 返回值:  item.jsp
     * 页面取值: ${item.title }   Item对象
     *          ${itemDesc.itemDesc }  ItemDesc商品详情  237行
     */
    @RequestMapping("/items/{itemId}")
    public String findItemById(@PathVariable Long itemId, Model model){

        Item item = itemService.findItemById(itemId);
        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }
}
