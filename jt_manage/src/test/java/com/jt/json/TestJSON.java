package com.jt.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestJSON {

    /**
     * json格式:
     *  1.object格式  {key:value,key2:value2}
     *  2.数组格式    [value,value2,value3]
     *  3.嵌套格式    value可以进行嵌套.
     *
     * 任务:  实现对象与JSON串之间的转化.
     * 面试题: 常量是否有线程安全性问题??? 没有
    */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void objectToJSON() throws JsonProcessingException {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("json测试")
                .setCreated(new Date()).setUpdated(itemDesc.getCreated());
        //将对象转化为JSON 调用对象的get方法 形成属性/属性值
        String json = MAPPER.writeValueAsString(itemDesc);
        System.out.println(json);
        //将JSON串,转化为对象  反射机制 实例化对象 调用对象的set方法为属性赋值
        ItemDesc itemDesc2 = MAPPER.readValue(json,ItemDesc.class);
        System.out.println(itemDesc2.toString()+""+itemDesc2.getCreated());
    }

    public List<ItemDesc> getList(){
        List<ItemDesc> list = new ArrayList<>();
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("bbb")
                .setCreated(new Date()).setUpdated(new Date());
        ItemDesc itemDesc2 = new ItemDesc();
        itemDesc2.setItemId(200L).setItemDesc("bbb")
                .setCreated(new Date()).setUpdated(new Date());
        list.add(itemDesc);
        list.add(itemDesc2);
        return list;
    }

    @Test
    public void ListToJSON() throws JsonProcessingException {
        List<ItemDesc> list = getList();
        //将对象转化为JSON
        String json = MAPPER.writeValueAsString(list);
        System.out.println(json);
        //将JSON还原回对象
        List<ItemDesc> list2 = MAPPER.readValue(json, List.class);
        System.out.println(list2);
    }

}
