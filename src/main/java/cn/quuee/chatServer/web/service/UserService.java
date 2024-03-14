package cn.quuee.chatServer.web.service;

import cn.quuee.chatServer.web.model.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<UserDO> {

    UserDO findUserByAccountPasswd(String account,String passwd);

    List<UserDO> contactList(Long userId);
}
