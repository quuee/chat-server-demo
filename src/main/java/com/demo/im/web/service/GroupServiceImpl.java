package com.demo.im.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.im.web.dao.GroupDOMapper;
import com.demo.im.web.model.GroupDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupDOMapper, GroupDO> implements GroupService {


    @Override
    public GroupDO findGroupAndMembers(Long groupId) {
        return baseMapper.selectGroupAndMembers(groupId);
    }

    @Override
    public List<GroupDO> findGroupsByUser(Long userId) {
        return baseMapper.selectGroupsByUser(userId);
    }
}
