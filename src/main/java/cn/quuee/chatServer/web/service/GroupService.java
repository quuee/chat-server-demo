package cn.quuee.chatServer.web.service;

import cn.quuee.chatServer.web.model.GroupDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GroupService extends IService<GroupDO> {

    GroupDO findGroupAndMembers(Long groupId);

    List<GroupDO> findGroupsByUser(Long userId);
}
