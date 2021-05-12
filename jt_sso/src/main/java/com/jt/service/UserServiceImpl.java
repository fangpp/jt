package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    //该类数据只取不改
    private static Map<Integer,String> paramMap = new HashMap<>();
    static {
        paramMap.put(1, "username");
        paramMap.put(2, "phone");
        paramMap.put(3, "email");
    }

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    //1 username、2 phone、3 email
    @Override
    public int checkUser(String param, Integer type) {

        QueryWrapper queryWrapper = new QueryWrapper();
        //动态获取校验的数据类型
        queryWrapper.eq(paramMap.get(type), param);
        return userMapper.selectCount(queryWrapper);
    }
}
