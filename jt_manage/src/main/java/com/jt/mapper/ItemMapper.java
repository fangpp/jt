package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemMapper extends BaseMapper<Item>{

    /*要求 越火爆的商品 排序靠前 以更新时间倒叙排列*/
    @Select("select * from tb_item order by updated desc limit #{startIndex},#{rows}")
    List<Item> findItemByPage(int startIndex, Integer rows);

    void deleteIds(Long[] ids);
}
