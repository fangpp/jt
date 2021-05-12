package com.jt.controller;

import com.jt.annotation.CacheFind;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/itemCat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 业务需求: 根据商品分类Id查询商品分类对象
     * URL地址: /itemCat/findItemCatById?id=497
     * 类型: Request Method: GET
     * 参数: id
     * 返回值: ItemCat对象
     */
    @RequestMapping("/itemCat/findItemCatById")
    public ItemCat findItemCatById(Long id){

        return itemCatService.findItemCatById(id);
    }

    /**
     * 查询商品分类树形结构控件
     * 1.url地址: http://localhost:8091/item/cat/list
     * 2.参数:    暂时没有
     * 3.返回值结果: List<EasyUITree>
     * 4.实现数据传递  id: xxxx
     */
    @RequestMapping("/item/cat/list")
    public List<EasyUITree> findItemCatList(Long id){

        //查询商品分类信息  1级菜单
        //如果用户没有点击按钮 将不会传递Id值,应该设定默认值
        long parentId = (id==null?0:id);
        return itemCatService.findItemCatList(parentId);
        //return itemCatService.findItemCatCache(parentId);
    }



}
