package com.demo.im.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("im_user")
public class UserDO {

    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;

    @TableField("account")
    private String account;

    @TableField("password")
    private String password;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField(exist = false)
    private String token;

}
