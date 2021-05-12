package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.UpdatableSqlQuery;

import java.util.List;

@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService{

    @Autowired
    private CartMapper cartMapper;


    @Override
    public List<Cart> findCartListByUserId(long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return cartMapper.selectList(queryWrapper);
    }

    /**
     * sql: update tb_cart set num=#{num} where user_id=#{userId} and item_id=#{itemId}
     * @param cart
     */
    @Override
    public void updateCartNum(Cart cart) {
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum());
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", cart.getUserId())
                     .eq("item_id", cart.getItemId());
        cartMapper.update(cartTemp,updateWrapper);
    }

    /**
     * 业务需求:
     *   1.如果已经加购 ,则只需要更新数量
     *   2.如果没有加购,则新增数据
     */
    @Override
    public void addCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", cart.getUserId())
                    .eq("item_id",cart.getItemId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if(cartDB == null){//如果为空 说明用户第一次加购
            cartMapper.insert(cart);
        }else{  //如果不为null,说明之前已经加购 更新数量
            int num = cartDB.getNum() + cart.getNum();
            //主键/更新数量
            Cart cartTemp = new Cart();
            cartTemp.setId(cartDB.getId()).setNum(num);
            cartMapper.updateById(cartTemp);
        }
    }
}
