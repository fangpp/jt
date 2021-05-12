package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service    //容器 生命周期较长的一个大型的map集合<itemCatServiceImpl,itemCatServiceImpl的对象>
public class ItemCatServiceImpl implements  ItemCatService{

    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired(required = false)
    private Jedis jedis;    //该参数不是最终结构,后期需要优化 该参数不是必须的


    @Override
    public ItemCat findItemCatById(Long id) {

        return itemCatMapper.selectById(id);
    }

    /**
     * 1.根据parentId查询商品分类列表信息  一级商品分类信息
     * 2.将商品分类列表转化为List<VO>对象
     * 3.返回vo的list集合
     * @param parentId
     * @return
     */
    @Override
    @CacheFind(key="ITEM_CAT_PARENTID")
    public List<EasyUITree> findItemCatList(Long parentId) {
        //1.根据父级id查询子级的信息
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);

        //2.将itemCat对象 转化为VO对象
        List<EasyUITree> voList = new ArrayList<>(itemCatList.size());
        for (ItemCat itemCat : itemCatList){
            long id = itemCat.getId();
            String text = itemCat.getName();
            //如果是父级则闭合,否则打开
            String state = itemCat.getIsParent() ? "closed" : "open";
            EasyUITree tree = new EasyUITree(id,text, state);
            voList.add(tree);
        }
        return voList;
    }

    /**
     *
     * 1.将商品分类信息添加到缓存中
     * 2.数据结构:  k-v结构   ITEM_CAT_PARENT::0 - JSON
     * 3.业务流程:
     *      1.先查缓存    true/false
     *      2.false  用户第一次查询数据库, 将结果保存到redis缓存中
     *      3.true   查询缓存
     *  AOP机制
     */
    @Override
    public List<EasyUITree> findItemCatCache(Long parentId) {
        long startTime = System.currentTimeMillis();
        List<EasyUITree> treeList = new ArrayList<>();
        //1.定义key
        String key = "ITEM_CAT_PARENT::" + parentId;
        //2.判断数据是否存在
        if(jedis.exists(key)){
            String json = jedis.get(key);
            treeList =
                    ObjectMapperUtil.toObject(json, treeList.getClass());
            long endTime = System.currentTimeMillis();
            System.out.println("redis查询! 耗时:"+(endTime-startTime));

        }else{
            //数据不存在,查询数据库
            treeList = findItemCatList(parentId);
            //将数据库记录,转化为JSON
            String json = ObjectMapperUtil.toJSON(treeList);
            // 将数据保存到redis中
            jedis.set(key, json);
            long endTime = System.currentTimeMillis();
            System.out.println("查询数据库!! 耗时:"+(endTime-startTime));
        }

        return treeList;
    }
}
