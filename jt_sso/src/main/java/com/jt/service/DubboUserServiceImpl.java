package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;


    /**
     * 1.对密码进行加密处理
     * 2.邮箱用电话代替
     * @param user
     */
    @Transactional
    @Override
    public void saveUser(User user) {
        byte[] bytes = user.getPassword().getBytes();
        String md5Pass = DigestUtils.md5DigestAsHex(bytes);
        user.setEmail(user.getPhone())
            .setPassword(md5Pass);
        userMapper.insert(user);
    }

    /**
     * 1.校验用户信息是否有效
     * 2.进行单点登录流程
     * @param user
     * @return
     */
    @Override
    public String doLogin(User user) {
        byte[] bytes = user.getPassword().getBytes();
        String md5Pass = DigestUtils.md5DigestAsHex(bytes);
        user.setPassword(md5Pass);
        User userDB = userMapper.selectOne(new QueryWrapper<>(user));
        if(userDB == null){
            //根据用户名和密码没有查询到
            return null;
        }
        //用户名和密码正确 开始启动单点登录的流程
        String ticket = UUID.randomUUID().toString();
        //为了防止泄密 进行脱敏处理
        userDB.setPassword("123456你猜猜??");
        String userJSON = ObjectMapperUtil.toJSON(userDB);
        jedisCluster.setex(ticket, 7*24*60*60,userJSON);
        //如果程序执行正确,则返回密钥
        return ticket;
    }
}
