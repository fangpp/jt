package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_cart")
@Data
@Accessors(chain = true)
public class Cart extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;        //用户编号
    private Long itemId;        //商品编号
    private String itemTitle;   //商品标题
    private String itemImage;   //商品图片信息
    private Long itemPrice;     //商品价格
    private Integer num;        //商品数量

}
