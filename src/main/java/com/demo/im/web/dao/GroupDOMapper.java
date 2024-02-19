package com.demo.im.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.im.web.model.GroupDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupDOMapper extends BaseMapper<GroupDO> {

    GroupDO selectGroupAndMembers(@Param("groupId") Long groupId);

    List<GroupDO> selectGroupsByUser(@Param("userId")Long userId);
}
