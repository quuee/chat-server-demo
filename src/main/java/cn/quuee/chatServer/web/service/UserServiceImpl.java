package cn.quuee.chatServer.web.service;

import cn.quuee.chatServer.web.model.UserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.quuee.chatServer.web.dao.ContactDOMapper;
import cn.quuee.chatServer.web.dao.UserDOMapper;
import cn.quuee.chatServer.web.model.ContactDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserDOMapper, UserDO> implements UserService {


    @Autowired
    private ContactDOMapper contactDOMapper;

    @Override
    public UserDO findUserByAccountPasswd(String account, String passwd) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getAccount,account).eq(UserDO::getPassword,passwd);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<UserDO> contactList(Long userId) {
        LambdaQueryWrapper<ContactDO> eq = new LambdaQueryWrapper<ContactDO>().eq(ContactDO::getUserId, userId);
        List<ContactDO> contactDOS = contactDOMapper.selectList(eq);
        List<Long> contactIds = contactDOS.stream().map(ContactDO::getContactId).collect(Collectors.toList());
        LambdaQueryWrapper<UserDO> in = new LambdaQueryWrapper<UserDO>().in(UserDO::getUserId, contactIds);
        List<UserDO> userDOS = baseMapper.selectList(in);
        return userDOS;
    }
}
