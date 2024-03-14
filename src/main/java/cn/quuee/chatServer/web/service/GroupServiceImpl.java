package cn.quuee.chatServer.web.service;

import cn.quuee.chatServer.web.dao.GroupDOMapper;
import cn.quuee.chatServer.web.model.GroupDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
