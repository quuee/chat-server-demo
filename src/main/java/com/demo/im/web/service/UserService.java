package com.demo.im.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.im.web.model.UserDO;

import java.util.List;

public interface UserService extends IService<UserDO> {

    UserDO findUserByAccountPasswd(String account,String passwd);

    List<UserDO> contactList(Long userId);
}
