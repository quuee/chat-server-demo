package com.demo.im.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.im.web.model.GroupDO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {

    GroupDO findGroupAndMembers(Long groupId);

    List<GroupDO> findGroupsByUser(Long userId);
}
