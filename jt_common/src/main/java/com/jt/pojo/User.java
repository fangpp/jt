package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_user")
@Data
@Accessors(chain = true)
//8093  查询所有的用户列表信息JSON返回
//http://sso.jt.com/findAll  mapper~service~controlle~nginx
public class User extends BasePojo{

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;    //采用md5方式加密
    private String phone;
    private String email;       //暂时使用电话号码代替
}
