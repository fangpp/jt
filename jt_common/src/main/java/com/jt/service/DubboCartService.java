package com.jt.service;

import com.jt.pojo.Cart;

import java.util.List;

public interface DubboCartService {
    List<Cart> findCartListByUserId(long userId);

    void updateCartNum(Cart cart);

    void addCart(Cart cart);
}
