package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data   //toString 当前类的属性
@Accessors(chain = true)
public class ItemDesc extends BasePojo{

    @TableId
    private Long itemId;        //itemId与商品表的Id值一致.
    private String itemDesc;    //商品详情信息

}
